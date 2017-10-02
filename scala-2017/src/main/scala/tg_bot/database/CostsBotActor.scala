package tg_bot.database

import akka.persistence.PersistentActor
import tg_bot.database.structures.CostsList

import scala.collection.mutable

class CostsBotActor extends PersistentActor {

  import CostsBotActor._

  val database: mutable.HashMap[Long, mutable.HashMap[String, CostsList]] = mutable.HashMap.empty

  def receiveEvent(event: Event): Unit = {
    event match {
      case NewList(id, name, budget) =>
        database.getOrElseUpdate(id, mutable.HashMap.empty)(name) = new CostsList(name, budget)
      case NewCost(id, costName, price, listName) =>
        database.getOrElseUpdate(id, mutable.HashMap.empty)(listName)
          .addCost(costName, price)
      case SetBudget(id, name, budget) =>
        database.getOrElseUpdate(id, mutable.HashMap.empty)(name)
          .changeBudget(budget)
      case _ =>
    }
  }

  override def receiveRecover: Receive = {
    case evt: Event => receiveEvent(evt)
  }

  override def receiveCommand: Receive = {
    case evt: Event => persist(evt)(receiveEvent)
    case GetLists(id) =>
      sender ! database.getOrElseUpdate(id, mutable.HashMap.empty)
    case GetList(id, name) =>
      val userLists = database.getOrElseUpdate(id, mutable.HashMap.empty)
      sender ! (if (userLists.contains(name)) Some(userLists(name).printList()) else None)
    case GetBudget(id, name) =>
      val userLists = database.getOrElseUpdate(id, mutable.HashMap.empty)
      sender ! (if (userLists.contains(name)) Some(userLists(name).getBudget()) else None)
    case RemoveLists(id) =>
      database.clear()
  }

  override def persistenceId = "koshchenko-costs-bot-database"
}

object CostsBotActor {

  trait Event

  case class NewList(id: Long, listName: String, budget: Double) extends Event

  case class NewCost(id: Long, costName: String, price: Double, listName: String) extends Event

  case class GetList(id: Long, listName: String)

  case class GetBudget(id: Long, listName: String)

  case class SetBudget(id: Long, listName: String, budget: Double) extends Event

  case class GetLists(id: Long)

  case class RemoveLists(id: Long)
}