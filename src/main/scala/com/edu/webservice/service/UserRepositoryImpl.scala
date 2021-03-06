package com.edu.webservice.service

import cats.Monad
import com.edu.webservice.algebra.UserRepository
import com.edu.webservice.domain.User
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import doobie.implicits._
import fs2.Stream
import cats.syntax.flatMap._
import cats.syntax.functor._
import doobie.util.log.LogHandler

class UserRepositoryImpl[F[_]: Monad](xa: Transactor[F], logger: Logger[F]) extends UserRepository[F] {

  import User._

  implicit val dbLoggerHandler = LogHandler.jdkLogHandler

  override def save(user: User): F[User] = for {
    _ <- logger.info(s"invoke UserRepository.save method with param: $user")
    res <- sql"insert into users (name, age, last_updatetime) values (${user.name}, ${user.age}, ${user.lastUpdatetime})"
          .update
          .withUniqueGeneratedKeys[User]("name", "age", "last_updatetime")
          .transact(xa)
  } yield res


  override def update(user: User): F[User] = for {
    _ <- logger.info(s"invoke UserRepository.save method with param: $user")
    res <- sql"update users set age = ${user.age}, last_updatetime = ${user.lastUpdatetime} where name = ${user.name}"
            .update
            .withUniqueGeneratedKeys[User]("name", "age", "last_updatetime")
            .transact(xa)
  } yield res

  override def delete(user: User): F[Boolean] = for {
    _ <- logger.info(s"invoke UserRepository.delete method with param: $user")
    res <- sql"delete from users where name = ${user.name}"
            .update
            .run
            .transact(xa).map(x => if (x > 0) true else false)
  } yield res


  override def find(name: String): F[Option[User]] = for {
    _ <- logger.info(s"invoke UserRepository.find method with param: $name")
    res <- sql"select * from users where name = $name"
            .query[User]
            .to[List]
            .map(_.headOption)
            .transact(xa)
  } yield res

  override def findAll(): Stream[F, User] = for {
    _ <- Stream.eval(logger.info("invoke UserRepository.findAll method without params"))
    res <- sql"select * from users".query[User].stream.transact(xa)
  } yield res

}
