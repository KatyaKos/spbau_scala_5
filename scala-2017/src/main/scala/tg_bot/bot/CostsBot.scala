package tg_bot.bot

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import tg_bot.database.CostsBotActor._
import tg_bot.database.structures.CostsList
import tg_bot.parser._

import scala.collection.mutable
import scala.concurrent.duration.DurationInt
import scala.util.Success

class CostsBot(val token: String,
                   val database: ActorRef) extends TelegramBot with Polling with Commands {
  onMessage {
    implicit message =>
      message.text.foreach { text =>
        implicit val timeout: Timeout = Timeout(5.second)
        val chatId = message.chat.id
        MessageParser.parse(text) match {
          case AddList(listName, budget) =>
            (database ? GetList(chatId, listName)).onComplete {
              case Success(Some(_)) =>
                reply("Such list already exists!")
              case _ =>
                database ! NewList(chatId, listName, budget)
                reply("List is ready to use.")
            }
          case AddCost(costName, price, listName) =>
            (database ? GetList(chatId, listName)).onComplete {
              case Success(Some(_)) =>
                database ! NewCost(chatId, costName, price, listName)
                reply("Cost added.")
              case _ =>
                reply("Such list doesn't exist.")
            }
          case ChangeBudget(listName, budget) =>
            (database ? GetList(chatId, listName)).onComplete {
              case Success(Some(_)) =>
                database ! SetBudget(chatId, listName, budget)
                reply("Budget changed.")
              case _ =>
                reply("Such list doesn't exist.")
            }
          case ShowList(listName) =>
            (database ? GetList(chatId, listName)).onComplete {
              case Success(Some(listText: String)) =>
                reply(listText)
              case _ =>
                reply("Such list doesn't exist.")
            }
          case ShowLists =>
            (database ? GetLists(chatId)).onComplete {
              case Success(map: mutable.HashMap[String, CostsList]) =>
                var result: String = "Your lists:\n"
                for (list: CostsList <- map.values) {
                  result += "Name: " + list.listName + ", Budget: " + list.budget + "\n"
                }
                reply(result)
              case _ =>
                reply("DatabaseError")
            }
          case SayBudget(listName) =>
            (database ? GetBudget(chatId, listName)).onComplete {
              case Success(Some(budgetText: String)) =>
                reply(budgetText)
              case _ =>
                reply("Such list doesn't exist.")
            }
          case ClearLists =>
            database ! RemoveLists(chatId)
            reply("Lists removed.")
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