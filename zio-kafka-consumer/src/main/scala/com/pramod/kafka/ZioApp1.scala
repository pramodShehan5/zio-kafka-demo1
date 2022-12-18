package com.pramod.kafka

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerRecord
import zio.{Clock, Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer, durationInt}
import zio.kafka.consumer.{Consumer, ConsumerSettings, Subscription}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde
import zio.stream.ZStream

object ZioApp1 extends ZIOAppDefault {
  val conf = ConfigFactory.load()
  private val BOOSTRAP_SERVERS = List(conf.getString("host"))
  private val KAFKA_TOPIC      = "Test3"

//  private val producer: ZLayer[Any, Throwable, Producer] =
//    ZLayer.scoped(
//      Producer.make(
//        ProducerSettings(BOOSTRAP_SERVERS)
//      )
//    )

  private val consumer: ZLayer[Any, Throwable, Consumer] =
    ZLayer.scoped(
      Consumer.make(
        ConsumerSettings(BOOSTRAP_SERVERS)
          .withGroupId("streaming-kafka-app")
      )
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
//    val p: ZStream[Producer, Throwable, Nothing] =
//      ZStream
//        .repeatZIO(Clock.currentDateTime)
//        .schedule(Schedule.spaced(1.second))
//        .map(time => new ProducerRecord(KAFKA_TOPIC, time.getMinute, s"$time -- Hello, World!"))
//        .via(Producer.produceAll(Serde.int, Serde.string))
//        .drain
    val c: ZStream[Consumer, Throwable, Nothing] =
      Consumer
        .subscribeAnd(Subscription.topics(KAFKA_TOPIC))
        .plainStream(Serde.int, Serde.string)
        .tap(e => zio.Console.printLine(e.value))
        .map(_.offset)
        .aggregateAsync(Consumer.offsetBatches)
        .mapZIO(_.commit)
        .drain
    c.runDrain.provide(consumer)
  }
}
