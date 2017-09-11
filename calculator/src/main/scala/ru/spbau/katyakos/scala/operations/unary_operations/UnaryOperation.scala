package ru.spbau.katyakos.scala.operations.unary_operations

import ru.spbau.katyakos.scala.operations.Operation

trait UnaryOperation extends Operation{
  override val arity: Int = 1
  @throws[UnsupportedOperationException]
  override def evaluate(x: Double, y: Double): Double = throw new UnsupportedOperationException()
}
