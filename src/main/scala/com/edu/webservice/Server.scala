package com.edu.webservice

import cats.effect._
import com.edu.webservice.service.{UserHttpEndpoint, UserRepositoryImpl, UserService}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.server.blaze.BlazeServerBuilder
import cats.syntax.functor._
import cats.syntax.flatMap._
import org.http4s.implicits._
import org.http4s.server.Router

object Server extends IOApp {

  def transactor[F[_]: Async: ContextShift]: Resource[F, HikariTransactor[F]] = for {
    ce <- ExecutionContexts.fixedThreadPool[F](32)
    te <- ExecutionContexts.cachedThreadPool[F]
    xa <- HikariTransactor.newHikariTransactor[F](
      "org.postgresql.Driver",
      "jdbc:postgresql://127.0.0.1:5432/pg_test",
      "postgres",
      "mysecretpassword",
      ce,
      te)
  } yield xa

  def runServer[F[_]: Timer: ConcurrentEffect](xa: Transactor[F]): F[ExitCode] = {
    for {
      logger <- Slf4jLogger.create[F]
      repo = new UserRepositoryImpl[F](xa, logger)
      userService = new UserService[F](repo, logger)
      endpoint = new UserHttpEndpoint[F](userService, logger)
      httpApp = Router("/users" -> endpoint.service).orNotFound
      code <- BlazeServerBuilder[F]
        .withHttpApp(httpApp)
        .serve
        .compile
        .lastOrError
    } yield code
  }

  override def run(args: List[String]): IO[ExitCode] = transactor[IO].use(runServer[IO])

}
