package ru.spbau.katyakos.scala.operations.binary_operations

import ru.spbau.katyakos.scala.operations.Operation

trait BinaryOperation extends Operation{
  override val arity: Int = 2

  @throws[UnsupportedOperationException]
  override def evaluate(x: Double): Double = throw new UnsupportedOperationException()
}
