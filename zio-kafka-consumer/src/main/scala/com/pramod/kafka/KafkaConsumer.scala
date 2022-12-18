package com.pramod.kafka

import com.pramod.kafka.config.Config
import zio.{RIO, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.kafka.consumer.{Consumer, ConsumerSettings, Subscription}
import zio.kafka.serde.Serde

object KafkaConsumer extends ZIOAppDefault with Config{

  private val BOOSTRAP_SERVERS = List(conf.getString("host"))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      f <- consumeAndPrintEvents("test-consumer-group", KAFKA_TOPIC).fork
      _ <- f.join
    } yield ()
  }

  private def consumeAndPrintEvents(groupId: String, topic: String, topics: String*): RIO[Any, Unit] =
    Consumer.consumeWith(settings = ConsumerSettings(BOOSTRAP_SERVERS).withGroupId(groupId),
      subscription = Subscription.topics(topic, topics: _*),
      keyDeserializer = Serde.string,
      valueDeserializer = Serde.string
    )((k, v) => zio.Console.printLine((k, v)).orDie)
}