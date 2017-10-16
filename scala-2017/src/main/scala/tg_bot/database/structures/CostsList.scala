package tg_bot.database.structures

import scala.collection.mutable

class CostsList(val listName: String, var budget: Double) {

  var costs: mutable.HashMap[String, Double] = mutable.HashMap.empty

  var totalPrice: Double = 0.0

  def addCost(name: String, price: Double) = {
    costs.put(name, price)
    totalPrice += price
  }

  def printList(): String = {
    var result: String =  "List name: " + listName + "\n" +
     getBudget() + "Costs list:\n"
    for ((name, price) <- costs.toList) {
      result += "    Name: " + name +
        ", Price: " + price + "\n"
    }
    result
  }

  def getBudget(): String = {
    return "Budget: " + budget + "\n" +
      "Already spent: " + totalPrice + "\n" +
      "Left to spend: " + (budget - totalPrice) + "\n"
  }

  def changeBudget(newBudget: Double) = {
    budget = newBudget
  }

}
