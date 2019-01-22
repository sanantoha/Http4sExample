package com.edu.webservice.codec

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

trait JsonCodecs[F[_]] {
  implicit def jsonDecoder[A <: Product : Decoder](implicit F: Sync[F]): EntityDecoder[F, A] = jsonOf[F, A]
  implicit def jsonEncoder[A <: Product : Encoder](implicit F: Sync[F]): EntityEncoder[F, A] = jsonEncoderOf[F, A]
}
