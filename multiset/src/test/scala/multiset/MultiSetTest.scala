package multiset

import org.scalatest.{BeforeAndAfter, FunSuite}
import com.google.common.truth.Truth.assertThat

class MultiSetTest extends FunSuite with BeforeAndAfter {

  private var multiset: MultiSet[String] = new MultiSet[String]()
  private var multiset2: MultiSet[String] = new MultiSet[String]()

  before {
    multiset = new MultiSet[String]()
    multiset.add("aa", 2)
    multiset.add("b")
    multiset.add("c", 2)
    multiset2 = new MultiSet[String]()
    multiset2.add("aa", 1)
    multiset2.add("b", 2)
    multiset2.add("d", 2)
  }

  test("getTest") {
    var result: Option[String] = multiset.get("aa")
    assert(result.isDefined && result.get == "aa")
    result = multiset.get("b")
    assert(result.isDefined && result.get == "b")
    result = multiset.get("c")
    assert(result.isDefined && result.get == "c")
    result = multiset.get("d")
    assert(result.isEmpty)
  }

  test("getNumberTest") {
    assert(multiset.getNumber("aa") == 2)
    assert(multiset.getNumber("b") == 1)
    assert(multiset.getNumber("c") == 2)
    assert(multiset.getNumber("d") == 0)
  }

  test("addTest") {
    multiset.add("e")
    assert(multiset.getNumber("e") == 1)
    multiset.add("f", 2)
    assert(multiset.getNumber("f") == 2)
    multiset.add("e", 2)
    assert(multiset.getNumber("e") == 3)
  }

  test("applyTest") {
    assert(multiset.apply("aa"))
    assert(multiset.apply("b"))
    assert(multiset.apply("c"))
    assert(!multiset.apply("d"))
  }

  test("mapTest") {
    val newMultiset: MultiSet[String] = new MultiSet[String]()
    newMultiset.add("aa!", 2)
    newMultiset.add("b!", 1)
    newMultiset.add("c!", 2)
    assertThat(multiset.map(x => x.concat("!"))).isEqualTo(newMultiset)
  }

  test("flatMapTest") {
    val newMultiset: MultiSet[Char] = new MultiSet[Char]()
    newMultiset.add('a', 4)
    newMultiset.add('b', 1)
    newMultiset.add('c', 2)
    assertThat(multiset.flatMap(x => x.chars().toArray)).isEqualTo(newMultiset)
  }

  test("filterTest") {
    val newMultiset: MultiSet[String] = new MultiSet[String]()
    newMultiset.add("b", 1)
    newMultiset.add("c", 2)
    assertThat(multiset.filter(x => x.length == 1)).isEqualTo(newMultiset)
  }

  test("intersectionTest") {
    val newMultiset: MultiSet[String] = new MultiSet[String]()
    newMultiset.add("aa", 1)
    newMultiset.add("b", 1)
    assertThat(multiset.&(multiset2)).isEqualTo(newMultiset)
  }

  test("unionTest") {
    val newMultiset: MultiSet[String] = new MultiSet[String]()
    newMultiset.add("aa", 3)
    newMultiset.add("b", 3)
    newMultiset.add("c", 2)
    newMultiset.add("d", 2)
    assertThat(multiset.|(multiset2)).isEqualTo(newMultiset)
  }

  test("equalsTest") {
    assert(multiset.equals(multiset))
  }

  test("comprehensionMapTest") {
    val multiset3: MultiSet[String] = new MultiSet[String]()
    multiset3.add("aa!", 2)
    multiset3.add("b!", 1)
    multiset3.add("c!", 2)
    val newMultiset = for {
      string <- multiset
    } yield string + "!"
    assertThat(multiset3).isEqualTo(newMultiset)
  }

  test("comprehensionFlatMapTest") {
    val multiset3: MultiSet[(String, String)] = new MultiSet[(String, String)]()
    multiset3.add(("aa", "aa"), 4)
    multiset3.add(("aa", "b"), 2)
    multiset3.add(("b", "aa"), 2)
    multiset3.add(("b", "b"), 1)
    multiset3.add(("c", "aa"), 4)
    multiset3.add(("c", "b"), 2)
    val newMultiset = for {
      first <- multiset
      second <- List("aa", "aa", "b")
    } yield (first, second)
    assertThat(multiset3).isEqualTo(newMultiset)
  }

  test("comprehensionFilterTest") {
    val multiset3: MultiSet[String] = new MultiSet[String]()
    multiset3.add("b", 1)
    multiset3.add("c", 2)
    val newMultiset = for {
      string <- multiset
      if string.length == 1
    } yield string
    assertThat(multiset3).isEqualTo(newMultiset)
  }

  test("patternMatchingTest") {
    multiset match {
      case MultiSet(a, b, c, d, e) => assertThat(MultiSet(a, b, c, d, e)).isEqualTo(multiset)
      case _ => fail("Matching failed.")
    }
  }
}
