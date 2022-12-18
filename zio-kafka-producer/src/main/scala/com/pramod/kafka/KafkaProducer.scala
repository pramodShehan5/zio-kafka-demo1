package com.pramod.kafka

import com.pramod.kafka.config.Config
import com.pramod.kafka.util.StringGenerator.TenStringMaker
import org.apache.kafka.clients.producer.RecordMetadata
import zio.{Clock, RIO, Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer, durationInt}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde


object KafkaProducer extends ZIOAppDefault with Config {
  private val BOOSTRAP_SERVERS = List(KAFKA_HOST)

  private val producer: ZLayer[Any, Throwable, Producer] =
    ZLayer.scoped(
      Producer.make(
        ProducerSettings(BOOSTRAP_SERVERS)
      )
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <-
        Clock.currentDateTime.flatMap { time =>
            produce(KAFKA_TOPIC, s"${10.makeString}", s"$time --- ${5.makeString}")
          }
          .schedule(Schedule.spaced(1.second))
          .provide(producer)
    } yield ()

  }

  private def produce(topic: String,
                      key: String,
                      value: String): RIO[Any with Producer, RecordMetadata] =
    Producer.produce[Any, String, String](
      topic = topic,
      key = key,
      value = value,
      keySerializer = Serde.string,
      valueSerializer = Serde.string
    )
}