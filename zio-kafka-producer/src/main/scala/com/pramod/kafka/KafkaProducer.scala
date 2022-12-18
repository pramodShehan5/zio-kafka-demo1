package com.pramod.kafka

import com.pramod.kafka.config.Config
import org.apache.kafka.clients.producer.ProducerRecord
import zio.{Clock, Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer, durationInt}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde
import zio.stream.ZStream

import scala.util.Random

object KafkaProducer extends ZIOAppDefault with Config {
  private val BOOSTRAP_SERVERS = List(KAFKA_HOST)

  private val producer: ZLayer[Any, Throwable, Producer] =
    ZLayer.scoped(
      Producer.make(
        ProducerSettings(BOOSTRAP_SERVERS)
      )
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val p: ZStream[Producer, Throwable, Nothing] =
      ZStream
        .repeatZIO(Clock.currentDateTime)
        .schedule(Schedule.spaced(1.millisecond))
        .map(time => new ProducerRecord(KAFKA_TOPIC, time.getMinute, s"$time -- Hello, World! ${Random.nextInt(100000)}"))
        .via(Producer.produceAll(Serde.int, Serde.string))
        .drain
    p.runDrain.provide(producer)

  }
}