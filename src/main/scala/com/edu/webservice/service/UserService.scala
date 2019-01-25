package com.edu.webservice.service

import cats.FlatMap
import com.edu.webservice.algebra.UserRepository
import com.edu.webservice.domain.User
import io.chrisdavenport.log4cats.Logger
import fs2.Stream
import cats.syntax.apply._

class UserService[F[_]: FlatMap](repo: UserRepository[F], logger: Logger[F]) {

  def find(name: String): F[Option[User]] =
    logger.info(s"invoke UserService.find method with param: $name") *> repo.find(name)

  def findAll(): Stream[F, User] =
    Stream.eval(logger.info("invoke UserService.findAll method without params")).drain ++
      repo.findAll()

  def update(user: User): F[User] =
    logger.info(s"invoke UserService.update method with param: $user") *> repo.update(user)

  def save(user: User): F[User] =
    logger.info(s"invoke UserService.save method with param: $user") *> repo.save(user)

  def delete(name: String): F[Boolean] =
    logger.info(s"invoke UserService.delete method with param: $name") *> repo.delete(User(name))
}