package com.pramod.kafka.util

import scala.util.Random

object StringGenerator {
  implicit class TenStringMaker(stringLength: Int = 10) {

    val alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    def makeString(): String = {
      (1 to stringLength).map(_ => alpha(Random.nextInt(alpha.length))).mkString
    }
  }
}