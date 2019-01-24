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

  import User._

  override def save(user: User): F[User] =
    sql"insert into users (name, age, last_updatetime) values (${user.name}, ${user.age}, ${user.lastUpdatetime})"
      .update
      .withUniqueGeneratedKeys[User]("name", "age", "last_updatetime")
      .transact(xa)

  override def update(user: User): F[User] =
    sql"update users set age = ${user.age}, last_updatetime = ${user.lastUpdatetime} where name = ${user.name}"
      .update
      .withUniqueGeneratedKeys[User]("name", "age", "last_updatetime")
      .transact(xa)


  override def delete(user: User): F[Boolean] =
    sql"delete from users where name = ${user.name}"
      .update
      .run
      .transact(xa).map(x => if (x > 0) true else false)

  override def find(name: String): F[Option[User]] =
    sql"select * from users where name = $name"
      .query[User]
      .to[List]
      .map(_.headOption)
      .transact(xa)

  override def findAll(): Stream[F, User] =
    sql"select * from users".query[User].stream.transact(xa)
}
