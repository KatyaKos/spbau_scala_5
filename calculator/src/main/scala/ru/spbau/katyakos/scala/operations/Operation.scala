package ru.spbau.katyakos.scala.operations

trait Operation {
  val arity:Int
  val operationSymbol:String
  val priority:Int
  def evaluate(x:Double):Double
  def evaluate(x:Double, y:Double):Double
  def evaluate(args:Array[Double]):Double = ???
}
