package ru.spbau.katyakos.scala.operations.binary_operations

import ru.spbau.katyakos.scala.operations.Operation

object Multiply extends BinaryOperation{
  override val operationSymbol: String = "*"
  override val priority: Int = -50

  override def evaluate(x: Double, y: Double): Double = x * y
}
