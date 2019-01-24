package com.edu.webservice.service

import cats.FlatMap
import com.edu.webservice.algebra.UserRepository
import com.edu.webservice.domain.User
import io.chrisdavenport.log4cats.Logger
import fs2.Stream
import cats.syntax.flatMap._
import cats.syntax.functor._

class UserService[F[_]: FlatMap](repo: UserRepository[F], logger: Logger[F]) {

  def find(name: String): F[Option[User]] = for {
    _ <- logger.info(s"invoke UserService.find method with param: $name")
    res <- repo.find(name)
  } yield res

  def findAll(): Stream[F, User] = repo.findAll()

  def update(user: User): F[User] = repo.update(user)

  def save(user: User): F[User] = repo.save(user)

  def delete(name: String): F[Boolean] = repo.delete(User(name))
}
