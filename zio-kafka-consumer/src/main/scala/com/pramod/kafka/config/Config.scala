package com.pramod.kafka.config

import com.typesafe.config.ConfigFactory

trait Config {
  lazy val conf = ConfigFactory.load()
  lazy val KAFKA_HOST = conf.getString("host")
  lazy val KAFKA_TOPIC = conf.getString("topic")
}