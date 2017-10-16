package tg_bot.bot

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.models.Message
import tg_bot.database.CostsBotActor._
import tg_bot.database.structures.CostsList
import tg_bot.parser._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.util.Success

class CostsBot(val token: String,
                   val database: ActorRef) extends TelegramBot with Polling with Commands {

  private def listEffectingFunc(listName: String,
                                successFunc: String => Unit)
                               (implicit message: Message) = {
    (database ? GetList(message.chat.id, listName)).onComplete {
      case Success(Some(listText: String)) =>
        successFunc(listText)
      case _ =>
        reply("Such list doesn't exist.")(message)
    }
  }

  private def addList(listName: String, budget: Double)(implicit message: Message) = {
    (database ? GetList(message.chat.id, listName)).onComplete {
      case Success(Some(listText: String)) =>
        reply("Such list already exists!")(message)
      case _ =>
        Await.ready(Future(database ! NewList(message.chat.id, listName, budget)), 10.seconds)
        reply("List is ready to use.")(message)
    }
  }

  private def addCost(costName:String, price: Double, listName: String)(implicit message: Message) = {
    val success: String => Unit = _ => (
      database ! NewCost(message.chat.id, costName, price, listName),
      reply("Cost added.")(message)
      )
    listEffectingFunc(listName, success)
  }

  private def changeBudget(listName: String, budget: Double)(implicit message: Message) = {
    val success: String => Unit = _ => (
      database ! SetBudget(message.chat.id, listName, budget),
      reply("Budget changed.")(message)
      )
    listEffectingFunc(listName, success)
  }

  private def showList(listName: String)(implicit message: Message) = {
    val success: String => Unit = listText => reply(listText)(message)
    listEffectingFunc(listName, success)
  }

  private def showLists()(implicit message: Message) = {
    (database ? GetLists(message.chat.id)).onComplete {
      case Success(iterable: Iterable[CostsList]) =>
        var result: String = "Your lists:\n"
        for (list: CostsList <- iterable) {
          result += "Name: " + list.listName + ", Budget: " + list.budget + "\n"
        }
        reply(result)(message)
      case _ =>
        reply("DatabaseError")(message)
    }
  }

  private def sayBudget(listName: String)(implicit message: Message) = {
    (database ? GetBudget(message.chat.id, listName)).onComplete {
      case Success(Some(budgetText: String)) =>
        reply(budgetText)(message)
      case _ =>
        reply("Such list doesn't exist.")(message)
    }
  }

  private def clearLists()(implicit message: Message) = {
    database ! RemoveLists(message.chat.id)
    reply("Lists removed.")(message)
  }

  private implicit val timeout: Timeout = Timeout(5.second)

  onMessage {
    implicit message =>
      message.text.foreach { text =>
        MessageParser.parse(text) match {
          case AddList(listName, budget) =>
            addList(listName, budget)
          case AddCost(costName, price, listName) =>
            addCost(costName, price, listName)
          case ChangeBudget(listName, budget) =>
            changeBudget(listName, budget)
          case ShowList(listName) =>
            showList(listName)
          case ShowLists =>
            showLists()
          case SayBudget(listName) =>
            sayBudget(listName)
          case ClearLists =>
            clearLists()
          case Start =>
            reply("Commands:\n" +
              "Add list LIST_NAME with budget N\n" +
              "Add cost COST_NAME with price N to list LIST_NAME\n" +
              "My lists\n" +
              "Clear lists\n" +
              "Show list LIST_NAME\n" +
              "Say budget of LIST_NAME\n" +
              "Change budget of LIST_NAME to N\n")
          case WrongMessage =>
              reply("No such command")
          }
      }
  }
}