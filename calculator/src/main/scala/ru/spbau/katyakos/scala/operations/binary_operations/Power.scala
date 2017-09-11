package ru.spbau.katyakos.scala.operations.binary_operations

/**
  * Created by KatyaKos on 11.09.2017.
  */
object Power extends BinaryOperation{
  override val operationSymbol: String = "^"
  override val priority: Int = -20

  override def evaluate(x: Double, y: Double): Double = scala.math.pow(x, y)
}
