package ru.spbau.katyakos.scala.operations.unary_operations

object Sinus extends UnaryOperation{
  override val operationSymbol: String = "sin"
  override val priority: Int = 10

  override def evaluate(x: Double): Double = scala.math.sin(x)
}
