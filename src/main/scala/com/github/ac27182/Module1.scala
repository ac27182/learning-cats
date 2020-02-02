package com.github.ac27182

object Module1 {

  sealed trait Json
  final case class JsonObject(get: Map[String, Json]) extends Json
  final case class JsonString(get: String)            extends Json
  final case class JsonNumber(get: Double)            extends Json
  case object JsNull                                  extends Json
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
      w.write((value))
  }

  // here json writer is our type class
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  final case class Person(name: String, email: String)
  final case class Animal(name: String, email: String)

  object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] =
      new JsonWriter[String] {
        def write(value: String): Json =
          JsonString(value)
      }
  }
  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json =
        w.write(value)
    }
  }

  implicit val personWriter: JsonWriter[Person] = {
    new JsonWriter[Person] {
      def write(value: Person): Json =
        JsonObject(
          Map(
            "name"  -> JsonString(value.name),
            "email" -> JsonString(value.email)
          )
        )
    }
  }
  val person =
    Person("alex", "a.cameron177@gmail.com")

  val animal =
    Animal("john", "bigjonny69@aol.com")

  val json0 = Json.toJson(person)

  import JsonSyntax.JsonWriterOps
  val json1 = person.toJson
  println(json1)
}

object TypeClasses extends App {
  sealed trait Animal
  final case class Dog(name: String)  extends Animal
  final case class Cat(name: String)  extends Animal
  final case class Bird(name: String) extends Animal

  // step one: define the type class
  trait BehavesLikeHuman[A] {
    def speak(a: A): Unit
  }

  // step two: the type class instances
  object BehavesLikeHumanInstances {
    implicit val dogBehavesLikeHuman =
      new BehavesLikeHuman[Dog] {
        def speak(dog: Dog): Unit =
          println(s"hello my name is ${dog.name}")
      }
  }

  // step three: the application programming interface (API)

  // val dog = Dog("Jerry")
  // dogBehavesLikeHuman.speak(dog)

  // step three)a)
  // define a companion object which contains your type class instances
  // refered to as an interface object
  object BehavesLikeHuman {
    def speak[A](
        a: A
    )(
        implicit behavesLikeHumanInstance: BehavesLikeHuman[A]
    ): Unit = {
      behavesLikeHumanInstance.speak(a)
    }
  }

  val dog0 = Dog("Jerry")
  // import BehavesLikeHumanInstances.dogBehavesLikeHuman
  // BehavesLikeHuman.speak(dog0)

  // step three)b)
  // we decare some type class "syntax"

  object BehavesLikeHumanSyntax {
    implicit class BehavesLikeHumanOps[A](value: A) {
      def speak(implicit behavesLikeHumanInstance: BehavesLikeHuman[A]): Unit =
        behavesLikeHumanInstance.speak(value)
    }
  }

  val dog1 = Dog("calum")
  import BehavesLikeHumanInstances.dogBehavesLikeHuman
  import BehavesLikeHumanSyntax.BehavesLikeHumanOps
  dog1.speak

  // example 1
  trait Weekday {
    def value: String =
      this match {
        case Saturday() => "Saturday"
        case Sunday()   => "Sunday"
        case Monday     => "Monday"
        case Tuesday    => "Tuesday"
        case Wednesday  => "Wednesday"
        case Thursday   => "Thursday"
        case Friday     => "Friday"
      }
  }
  final case class Saturday() extends Weekday
  final case class Sunday()   extends Weekday
  final case object Monday    extends Weekday
  final case object Tuesday   extends Weekday
  final case object Wednesday extends Weekday
  final case object Thursday  extends Weekday
  final case object Friday    extends Weekday

  // type class
  trait WeekendLogic[A] {
    def printLogic(a: A): Unit
  }

  // type class interface object
  object WeekendLogic {
    def printLogic[A](
        a: A
    )(implicit weekendLogicInstance: WeekendLogic[A]): Unit =
      weekendLogicInstance.printLogic(a)
  }

  // type class syntax
  object WeekendLogicSyntax {
    implicit class WeekendLogicOps[A](value: A) {
      def printLogic(implicit weekendLogicInstance: WeekendLogic[A]): Unit =
        weekendLogicInstance.printLogic(value)
    }
  }

  // type class instance
  object WeekendLogicInstance {
    implicit val weekendPrinter =
      new WeekendLogic[Saturday] {
        def printLogic(s: Saturday): Unit = println(s"${s.value}")
      }
    implicit val sundayPrinter =
      new WeekendLogic[Sunday] {
        def printLogic(s: Sunday): Unit = println(s"${s.value}")
      }
  }

  import WeekendLogicInstance.{weekendPrinter, sundayPrinter}
  // import WeekendLogicSyntax.WeekendLogicOps

  // what are the three constituant parts of a type class in scala?
  // 1. the type class defintion
  // 2. the type class instance
  // 3. the type class interface
  // 4. the type class syntax
  // definition, instance, interface

  // example 2
  sealed trait AccountDetails
  final case class Username(username: String)
  final case class Password(password: String)

  val username = Username("alexcameron")
  val password = Password("abc123!!")

  // type class definiton
  sealed trait AccountDetailsFunctionality[A] {
    def stdOut(a: A): Unit
  }

  // type class instance
  object AccountDetailsFunctionalityInstances {
    implicit val usernameStdOut = new AccountDetailsFunctionality[Username] {
      def stdOut(a: Username): Unit =
        println(s"users username is: ${a.username}")
    }
    implicit val passwordStdOut = new AccountDetailsFunctionality[Password] {
      def stdOut(a: Password): Unit =
        println(s"users password is: ${a.password}")
    }
  }

  // type class interface companion object
  object AccountDetailsFunctionality {
    def stdOut[A](a: A)(
        implicit accountDetailsFunctionalityInstances: AccountDetailsFunctionality[
          A
        ]
    ): Unit = accountDetailsFunctionalityInstances.stdOut(a)
  }

  // type class syntax
  object AccountDetailsFunctionalitySyntax {
    implicit class AccountDetailsFunctionalityOps[A](value: A) {
      def stdOut(
          implicit accountDetailsFunctionality: AccountDetailsFunctionality[A]
      ): Unit =
        accountDetailsFunctionality.stdOut(value)
    }
  }

  import AccountDetailsFunctionalityInstances._
  import AccountDetailsFunctionalitySyntax._

  username.stdOut
  password.stdOut

}
