package com.edu.webservice.domain

import java.time.{Instant, LocalDateTime, ZoneOffset}

import doobie.util.Meta
import cats.syntax.option._

final case class User(name: String, age: Option[Int] = none, lastUpdatetime: Option[LocalDateTime] = none)

object User {

  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Instant].imap(LocalDateTime.ofInstant(_, ZoneOffset.UTC))(_.toInstant(ZoneOffset.UTC))
}