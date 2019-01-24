package com.edu.webservice.service

import cats.effect.Sync
import io.chrisdavenport.log4cats.Logger
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.syntax.functor._
import cats.syntax.flatMap._
import com.edu.webservice.domain.User
import io.circe.generic.auto._
import cats.syntax.applicativeError._
import com.edu.webservice.codec.JsonCodecs

class UserHttpEndpoint[F[_]: Sync](userService: UserService[F], logger: Logger[F]) extends Http4sDsl[F] with JsonCodecs[F] {

  val service: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / name => for {
      _ <- logger.info(s"invoke GET method with parameter: $name")
      res <- if (name == "ping") Ok("pong")
             else userService.find(name).flatMap(_.fold(NotFound(s"User with name $name"))(Ok(_)))
    } yield res


    case GET -> Root =>
      Ok(userService.findAll())

    case req @ POST -> Root =>
      req.decode[User]{ user =>
        userService.save(user).flatMap(user => Created(s"User ${user.name} was created"))
      }.handleErrorWith {
        e: Throwable => Conflict(s"Error: ${e.getMessage}")
      }

    case req @ PUT -> Root =>
      req.decode[User]{ user =>
        userService.update(user).flatMap(user => Ok(s"User ${user.name} was updated"))
      }.handleErrorWith(_ => NoContent())

    case DELETE -> Root / name =>
      userService.delete(name).flatMap(b =>
        if (b) Ok(s"User with name $name was deleted")
        else NotFound(s"User with name $name not found")
      )
  }
}
