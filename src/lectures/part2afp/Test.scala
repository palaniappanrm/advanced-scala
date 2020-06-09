package lectures.part2afp

object Test extends App {

  class test[A]{
    def print[B >: A](element :B):Unit = println(element)
  }

  var firstTest = new test[String]
  firstTest.print(100.0)

}
