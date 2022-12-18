//package com.pramod.kafka
//
//import zio.kafka.consumer.{Consumer, ConsumerSettings, Subscription}
//import zio.kafka.serde.Serde
//import zio.stream.ZSink
//import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
//
//object ZioApp extends ZIOAppDefault{
//
//  val consumerSettings = ConsumerSettings(List("localhost:29092"))
//    .withGroupId("updates-consumer")/**/
//
//  val managedConsumer = Consumer.make(consumerSettings)
//
//  val consumer = ZLayer.fromZIO(managedConsumer)
//
//  val footBallMatchesStream = Consumer.subscribeAnd(Subscription.topics("Test1"))
//    .plainStream(Serde.string, Serde.string)
//
//  val matchesPrintableStream = footBallMatchesStream.map(fb => (fb.value, fb.offset))
//    .tap{case (score, _) => zio.Console.printLine(s"| $score |")}
//    .map(_._2)
//    .aggregateAsync(Consumer.offsetBatches)
//
//  val streamEffect = matchesPrintableStream.run(ZSink.foreach(offset => offset.commit))
//
//  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
//    streamEffect.provideSomeLayer(consumer).exitCode
//
//  }
//}
