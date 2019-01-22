package com.edu.webservice.algebra

import com.edu.webservice.domain.User
import fs2.Stream

trait UserRepository[F[_]] {

  def save(user: User): F[User]

  def update(user: User): F[User]

  def delete(user: User): F[Boolean]

  def find(name: String): F[Option[User]]

  def findAll(): Stream[F, User]
}
