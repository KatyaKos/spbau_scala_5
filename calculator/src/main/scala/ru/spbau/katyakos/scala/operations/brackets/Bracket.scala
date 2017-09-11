package ru.spbau.katyakos.scala.operations.brackets

import ru.spbau.katyakos.scala.operations.Operation

/**
  * Created by KatyaKos on 10.09.2017.
  */
trait Bracket extends Operation{
  override val arity: Int = 0
  val operationSymbol: String
  val priority: Int

  @throws[UnsupportedOperationException]
  override def evaluate(x: Double): Double = throw new UnsupportedOperationException()

  @throws[UnsupportedOperationException]
  override def evaluate(x: Double, y: Double): Double = throw new UnsupportedOperationException()

}
