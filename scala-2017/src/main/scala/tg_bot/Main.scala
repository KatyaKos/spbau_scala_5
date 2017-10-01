package tg_bot

import akka.actor.{ActorSystem, Props}
import tg_bot.bot.CostsBot
import tg_bot.database.CostsBotActor

object Main extends App {
  val token = "455793960:AAEQB5g2nL4NZCISU7S4AuCi1t8xTiYhjW8"

  val system = ActorSystem()
  val database = system.actorOf(Props(classOf[CostsBotActor]))

  private val bot = new CostsBot(token, database)

  bot.run()
}