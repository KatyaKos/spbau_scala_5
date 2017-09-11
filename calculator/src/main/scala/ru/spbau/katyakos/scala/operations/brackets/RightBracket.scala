package ru.spbau.katyakos.scala.operations.brackets

object RightBracket extends Bracket{
    override val operationSymbol: String = ")"
    override val priority: Int = Int.MinValue
}
