package ru.spbau.katyakos.scala

import org.junit.Assert._
import org.junit.Test

class CalculatorTest{
  val EPS = 1e-6

  @Test
  def calculatorTest(): Unit = {
    assertTrue(Calculator.evaluate("3/4-2+3+-2*2") == -2.25)
    assertTrue(Calculator.evaluate("-1+3--5") == 7)
    assertTrue(Math.abs(Calculator.evaluate("(sin(3+6*(1-1)))+sin(-1)") - (-0.70035)) < EPS)
    assertTrue(Math.abs(Calculator.evaluate("sinsin--1+-sinsin---1") - 1.491248) < EPS)
  }
}
