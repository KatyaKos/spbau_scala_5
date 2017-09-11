package ru.spbau.katyakos.scala

import ru.spbau.katyakos.scala.operations._
import ru.spbau.katyakos.scala.operations.binary_operations._
import ru.spbau.katyakos.scala.operations.brackets.{LeftBracket, RightBracket}
import ru.spbau.katyakos.scala.operations.unary_operations.{Sinus, UnaryMinus}

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._

object Calculator {
  private val FLOAT_DIVIDER:String = "."
  private var valuesStack = new ListBuffer[Double]()
  private var operationsStack = new ListBuffer[Operation]()
  private val operations = Array(LeftBracket, RightBracket, Plus, Minus, Divide, Multiply, UnaryMinus, Power, Sinus)

  private def getNumber(line: String): (Double, Int) = {
    var i = 0
    while (line.charAt(i).isDigit) {
      i += 1
    }
    if (line.substring(i).startsWith(FLOAT_DIVIDER)) {
      i += FLOAT_DIVIDER.length
      while (line.charAt(i).isDigit) {
        i += 1
      }
    }
    return (line.substring(0, i).toDouble, i)
  }

  private def compute (op: Operation) = {
    if (op.arity == 1) {
      valuesStack += op.evaluate(valuesStack.remove(valuesStack.length - 1))
    } else if (op.arity == 2) {
      valuesStack += op.evaluate(valuesStack.remove(valuesStack.length - 2), valuesStack.remove(valuesStack.length - 1))
    } else {
      throw new UnsupportedOperationException()
    }
  }

  def evaluate(expression: String): Double = {
    val line:String = expression + ")"
    var flag:Boolean = false
    operationsStack += LeftBracket
    var i = 0
    while (i < line.length) {
//Gets number
      if (line.charAt(i).isDigit) {
        flag = true
        val (number, delta) = getNumber(line.substring(i))
        valuesStack += number
        i += delta
//Skips whitespaces (they divide arguments in n>3 arity functions)
      } else if (line.charAt(i).equals(' ')) {
        i += 1
//Gets operation and evaluates
      } else {
        var op:Operation = null
        breakable {
          for (operation <- operations) {
            val arity = operation.arity
            if ((arity == 0 || arity == 2 && flag || arity != 2 && !flag)
              && line.substring(i).startsWith(operation.operationSymbol)) {
              op = operation
              i += operation.operationSymbol.length
              break
            }
          }
        }
        if (op == null) throw new UnsupportedOperationException()
        if (!op.equals(RightBracket))
          flag = false

        breakable {
          while (!operationsStack.last.equals(LeftBracket) && operationsStack.last.priority >= op.priority) {
            if (op.arity != 0 && op.arity != 2 && operationsStack.last.arity != 2)
              break
            compute(operationsStack.remove(operationsStack.size - 1))
          }
        }

        if (op.equals(RightBracket)) operationsStack.remove(operationsStack.length - 1)
        else operationsStack += op
      }
    }
    if (operationsStack.nonEmpty)
      throw new IllegalStateException()
    valuesStack.last
  }

}