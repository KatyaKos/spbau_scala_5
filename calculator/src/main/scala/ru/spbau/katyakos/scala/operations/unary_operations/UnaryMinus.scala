package ru.spbau.katyakos.scala.operations.unary_operations

object UnaryMinus extends UnaryOperation{
  override val operationSymbol: String = "-"
  override val priority: Int = 10

  override def evaluate(x: Double): Double = -x
}
