package tg_bot.parser

trait UserMessage

case class AddList(listName: String, budget: Double) extends UserMessage

case class AddCost(costName: String, price: Double, listName: String) extends UserMessage

case class ShowList(listName: String) extends UserMessage

case class SayBudget(listName: String) extends UserMessage

case class ChangeBudget(listName: String, budget: Double) extends UserMessage

case object ShowLists extends UserMessage

case object ClearLists extends UserMessage

case object WrongMessage extends UserMessage