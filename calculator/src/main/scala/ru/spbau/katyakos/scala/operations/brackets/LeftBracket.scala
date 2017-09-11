package ru.spbau.katyakos.scala.operations.brackets

object LeftBracket extends Bracket{
  override val operationSymbol: String = "("
  override val priority: Int = Int.MaxValue
}
