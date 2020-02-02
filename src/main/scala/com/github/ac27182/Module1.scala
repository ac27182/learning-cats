package com.github.ac27182

import java.{util => ju}

object Module1 {
  object Section4 {

    import cats.Show
    // without typeclass instances we cannot use the apply method, so we need to import some
    import cats.instances.int._
    import cats.instances.string._

    val showInt    = Show.apply[Int]
    val showString = Show.apply[String]

    // we can make show easier to use by importing the interface syntax from cats.syntax.show
    import cats.syntax.show._

    val shownInt: String =
      123.show

    val shownString: String =
      "abc".show

    // imports every typeclass
    import cats._

    // imports all typeclass isntances
    import cats.instances.all._

    // imports all synatx in one go
    import cats.syntax.all._

    // imports all of the standart typeclass intstances AND all of the syntax
    import cats.implicits._

    // custom instances

    import java.util.Date
    implicit val dateShow: Show[Date] = new Show[Date] {
      def show(t: Date): String = s"> s${t} <"
    }
    implicit class DateShowSyntax(d: Date) {
      def show = dateShow.show(d)
    }

    val e0 = new Date()
    val e1 = e0.show

    implicit val dateShow0: Show[Date] = Show.show(date => s"> s${date} <")
  }

  // using the Eq
  object Section2 {
    // Eq is use for type safe equality in scala

    import cats.Eq
    import cats.instances.int._
    val eqInt = Eq[Int]

    val e0: Boolean =
      eqInt.eqv(10, 100)

    // with thow a compile error
    // val e1: Boolean =
    //   eqInt.eqv(10, 10L)

    import cats.syntax.eq._
    val e1: Boolean =
      1 === 0

    val e2: Boolean =
      10 =!= 100

    import cats.instances.int._
    import cats.instances.option._

    // will throw a compile/runtime error, we are comparing some not option
    // val e3 =
    //   Some(1) === None

    val e3 =
      (Some(1): Option[Int]) === (None: Option[Int])

    // or equivilently
    val e4 =
      Option(1) === Option.empty[Int]

    import cats.syntax.option._
    val e5 =
      1.some === none[Int]

    // comparing custom types
    // this is useful for testing

    import java.util.Date
    import cats.instances.long._

    implicit val dateEq: Eq[Date] =
      Eq.instance[Date]((date1, date2) => date1.getTime === date2.getTime)

    val t0 = new Date()
    val t1 = new Date()

    val e6 = t0 === t1
    val e7 = t0 =!= t1

    // exercise
    final case class Cat(name: String, age: Int, color: String)

    val cat00 = Cat("alan", 38, "black")
    val cat11 = Cat("steve", 69, "red")

    import cats.instances.string._
    implicit val catEq: Eq[Cat] =
      Eq.instance[Cat]((cat0, cat1) => {
        val Cat(n0, a0, c0) = cat0
        val Cat(n1, a1, c1) = cat1

        n0 === n1 && // comparing name
        a0 === a1 && // comparing age
        c0 === c1    // comparing color
      })

    val e8 = cat00 === cat11

  }

  object Section6 {
    // controlling instance selection

    trait F[+A] // the + means covariant
    // F[A] <: F[B] <=> B <: A
    trait List[+A]
    trait Option[+A]

    sealed trait Shape
    case class Circle(radius: Double) extends Shape
    val circles: List[Circle] = ???
    val shapes: List[Shape]   = circles

    trait G[-A] // the - means contravariance
    // G[B] <: G[A] <=> A <: B

    val shape: Shape   = ???
    val circle: Circle = ???
    // val shapeWriter: JsonWriter[Shape] = ???

    trait H[A] // means invariance
    // F[A] !<: F[B] && F[B] !<: F[A]

  }

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
