package ru.spbau.katyakos.scala

import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {
    val expression = StdIn.readLine()
    println(Calculator.evaluate(expression))
  }
}
