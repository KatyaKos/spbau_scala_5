package tg_bot.parser

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

class MessageParser extends RegexParsers {
  override def skipWhitespace = true

  override val whiteSpace: Regex = "[ \t\r\f]+".r

  val nameParser: Parser[String] = raw"\S+".r
  val doubleParser: Parser[Double] = "[0-9]*\\.?[0-9]+".r ^^ {
    _.toDouble
  }

  val addList: Parser[AddList] =
    "[Aa]dd list".r ~> nameParser ~ ("with budget" ~> doubleParser) ^^ {
      case listName ~ budget => AddList(listName, budget)
    }

  val addCost: Parser[AddCost] =
    "[Aa]dd cost".r ~> nameParser ~ ("with price" ~> doubleParser) ~ ("to list" ~> nameParser) ^^ {
      case costName ~ price ~ listName => AddCost(costName, price, listName)
    }

  val getLists: Parser[UserMessage] = "[Mm]y lists".r ^^ { _ => ShowLists }

  val clearLists: Parser[UserMessage] = "[Cc]lear lists".r ^^ { _ => ClearLists }

  val start: Parser[UserMessage] = "/start".r ^^ { _ => Start}

  val showList: Parser[ShowList] =
    "[Ss]how list".r ~> nameParser ^^ {
      listName => ShowList(listName)
    }

  val sayBudget: Parser[SayBudget] =
    "[Ss]ay budget of".r ~> nameParser ^^ {
      listName => SayBudget(listName)
    }

  val changeBudget: Parser[ChangeBudget] =
    "[Cc]hange budget of".r ~> nameParser ~ ("to" ~> doubleParser) ^^ {
      case listName ~ budget => ChangeBudget(listName, budget)
    }

  val userMessage: Parser[UserMessage] =
    addList | addCost | getLists | showList | sayBudget | changeBudget | clearLists | start
}

object MessageParser extends MessageParser {
  def parse(text: String): UserMessage = {
    parse(userMessage, text) match {
      case Success(message, _) => message
      case _ => WrongMessage
    }
  }
}