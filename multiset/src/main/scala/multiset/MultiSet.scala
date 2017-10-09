package multiset

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by KatyaKos on 09.10.2017.
  */
class MultiSet[A] {

  private val multiset = mutable.HashMap.empty[A, Int]

  def add(element: A, number: Int = 1) = {
    if (number <= 0) {
      throw new IllegalArgumentException("Number of elements should be positive.")
    }
    val oldNumber: Int = multiset.getOrElse(element, 0)
    multiset.put(element, oldNumber + number)
  }

  def get(element: A): Option[A] = {
    if (multiset.contains(element)) {
      Some(element)
    } else {
      None
    }
  }

  def getNumber(element: A): Int = {
    multiset.getOrElse(element, 0)
  }

  def apply(elem: A): Boolean = {
    multiset.contains(elem)
  }

  def map[B](function: A => B): MultiSet[B] = {
    val newMultiset: MultiSet[B] = new MultiSet[B]()
    for ((element, number) <- multiset) {
      newMultiset.add(function(element), number)
    }
    newMultiset
  }

  def flatMap[B](function: A => Iterable[B]): MultiSet[B] = {
    val newMultiset: MultiSet[B] = new MultiSet[B]()
    for ((element, number) <- multiset) {
      val iterable: Iterable[B] = function(element)
      for (newElement <- iterable) {
        newMultiset.add(newElement, number)
      }
    }
    newMultiset
  }

  def filter(predicate: A => Boolean): MultiSet[A] = {
    val newMultiset: MultiSet[A] = new MultiSet[A]()
    for ((element, number) <- multiset) {
      if (predicate(element)) {
        newMultiset.add(element, number)
      }
    }
    newMultiset
  }

  def withFilter(predicate: A => Boolean): MultiSet[A] = {
    filter(predicate)
  }

  def &(multiset2: MultiSet[A]): MultiSet[A] = {
    val newMultiset: MultiSet[A] = new MultiSet[A]()
    for ((element, number) <- multiset) {
      val newNumber = Math.min(multiset2.getNumber(element), number)
      if (newNumber > 0) {
        newMultiset.add(element, newNumber)
      }
    }
    newMultiset
  }

  def |(multiset2: MultiSet[A]): MultiSet[A] = {
    val newMultiset: MultiSet[A] = new MultiSet[A]()
    for ((element, number) <- multiset) {
      newMultiset.add(element, number)
    }
    for ((element, number) <- multiset2.multiset) {
      newMultiset.add(element, number)
    }
    newMultiset
  }

  override def equals(multiset2: scala.Any): Boolean = {
    multiset2 match {
      case multiset2: MultiSet[A] => this.multiset == multiset2.multiset
      case _ => false
    }
  }
}

object MultiSet {

  def apply[A](elements: A*): MultiSet[A] = {
    val newMultiset: MultiSet[A] = new MultiSet[A]()
    for (element <- elements) {
      newMultiset.add(element)
    }
    newMultiset
  }

  def unapplySeq[A](multiset: MultiSet[A]): Option[Seq[A]] = {
    val elements = ListBuffer.empty[A]
    for ((element, number) <- multiset.multiset) {
      elements ++= List.fill(number)(element)
    }
    Some(elements)
  }
}