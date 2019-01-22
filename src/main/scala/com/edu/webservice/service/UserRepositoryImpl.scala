package com.edu.webservice.service

import cats.Monad
import com.edu.webservice.algebra.UserRepository
import com.edu.webservice.domain.User
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import doobie.implicits._
import fs2.Stream
import cats.syntax.functor._

class UserRepositoryImpl[F[_]: Monad](xa: Transactor[F], logger: Logger[F]) extends UserRepository[F] {

  override def save(user: User): F[User] =
    sql"insert into users (name, age) values (${user.name}, ${user.age})"
      .update
      .withUniqueGeneratedKeys[User]("name", "age")
      .transact(xa)

  override def update(user: User): F[User] =
    sql"update users set age = ${user.age} where name = ${user.name}"
      .update
      .withUniqueGeneratedKeys[User]("name", "age")
      .transact(xa)


  override def delete(user: User): F[Boolean] =
    sql"delete from users where name = ${user.name}"
      .update
      .run
      .transact(xa).map(x => if (x > 0) true else false)

  override def find(name: String): F[Option[User]] =
    sql"select name, age from users where name = $name"
      .query[User]
      .to[List]
      .map(_.headOption)
      .transact(xa)

  override def findAll(): Stream[F, User] =
    sql"select name, age from users".query[User].stream.transact(xa)
}
