package ru.spbau.katyakos.scala.operations.binary_operations

import ru.spbau.katyakos.scala.operations.Operation

object Plus extends BinaryOperation{
  override val operationSymbol: String = "+"
  override val priority: Int = -100

  override def evaluate(x: Double, y: Double): Double = x + y
}
