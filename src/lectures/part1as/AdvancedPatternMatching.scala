package lectures.part1as

/**
  * Created by Daniel.
  */
object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
//    case head => println(s"the only element is $head.")
    case head :: tail => println(s"the only element is $head.")
    case _ =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo."
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  /*
    Exercise.
   */

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] = Some(arg > -10 && arg < 10)
  }

  val n: Int = 8
  val mathProperty = n match {
    case singleDigit(x) => x
    case even() => "an even number"
    case _ => "no property"
  }

  println(mathProperty)

  // infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
//    case Or(number,string) => s"$number is written as $string"
    case number Or string => s"$number is written as $string"
//  not working for 3 parameters though
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  // defining +A allows us to send MyList[Nothing] in the place of MyList[Int]
  abstract class MyList[A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
//  case object Empty extends MyList[Nothing]
  case object Empty extends MyList[Int]
  case class Cons[A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(2, Cons(3, Cons(4, Empty)))
  val decomposed = myList match {
    case MyList(x, y, z, _*) => "starting with 1, 2 " + x + y + z
    case _ => "something else"
  }

  println(decomposed)

  // custom return types for unapply
  // isEmpty: Boolean, get: something.

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get= person.name
    }
  }

  println(bob match {
//  automatically get triggers
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  })

}
