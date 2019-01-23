package com.edu.webservice.service

import com.edu.webservice.algebra.UserRepository
import com.edu.webservice.domain.User
import io.chrisdavenport.log4cats.Logger
import fs2.Stream

class UserService[F[_]](repo: UserRepository[F], logger: Logger[F]) {

  def find(name: String): F[Option[User]] = repo.find(name)

  def findAll(): Stream[F, User] = repo.findAll()

  def update(user: User): F[User] = repo.update(user)

  def save(user: User): F[User] = repo.save(user)

  def delete(name: String): F[Boolean] = repo.delete(User(name, -1))
}
