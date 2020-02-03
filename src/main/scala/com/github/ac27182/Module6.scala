package com.github.ac27182

import cats.instances.`package`.string

// semigroupal and applicative
object Module6 {
  object Section1 {
    import cats.syntax.either._ // for catchOnly

    def parseInt(str: String): Either[String, Int] =
      Either
        .catchOnly[NumberFormatException](str.toInt)
        .leftMap(_ => s"couldnt read $str")

    val e0 = for {
      a <- parseInt("a")
      b <- parseInt("b")
      c <- parseInt("c")
    } yield (a + b + c)

    // we need a WEAKER construct that doesnt guarentee sequencing

    import cats.Semigroupal
    import cats.instances.option._

    val e1 = Semigroupal[Option].product(Some(123), Some("abc"))

    val e2 = Semigroupal[Option].product(None, Some("abc"))

    val e3 = Semigroupal[Option].product(Some(123), None)

    import cats.instances.option._

    val e4 = Semigroupal.tuple3(Option(1), Option(2), Option(3))

    val e5 = Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int])

    val e6 = Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)

    val e7 = Semigroupal.map2(Option(1), Option.empty[Int])(_ * _)

    // cats.syntax.apply._ provides shorthand methods

    import cats.instances.option._
    import cats.syntax.apply._

    val e8 = (Option(123), Option("alexcameron")).tupled

    val e9 = (Option(123), Option("alexcameron"), Option(true)).tupled

    case class Cat(name: String, born: Int, colour: String)

    val e10 =
      (
        Option("garfield"),
        Option(1969),
        Option("orange and black")
      ) mapN Cat

    // mapN is type checked at compile time, this is good.

    import cats.Monoid
    import cats.instances.int._
    import cats.instances.invariant._
    import cats.instances.list._
    import cats.instances.string._
    import cats.syntax.apply._

    case class Cat0(
        name: String,
        yearOfBirth: Int,
        favouriteFoods: List[String]
    )

    val tupledToCat: (String, Int, List[String]) => Cat0 = Cat0.apply _

    val catToTuple: Cat0 => (String, Int, List[String]) = cat =>
      (cat.name, cat.yearOfBirth, cat.favouriteFoods)

    implicit val catMonoid: Monoid[Cat0] = (
      Monoid[String],
      Monoid[Int],
      Monoid[List[String]]
    ).imapN(tupledToCat)(catToTuple)

    import cats.syntax.semigroup._

    var garfield    = Cat0("garfield", 1978, List("lasang"))
    var heathcliffe = Cat0("hc", 1978, List("junk foood"))

    val comb = garfield |+| heathcliffe
  }

  // the semigroupal typeclass applied to different types
  object Section3 {
    import cats.Semigroupal
    import cats.instances.future._
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global

    val futurePair =
      Semigroupal[Future].product(Future("hello"), Future(123))

    val e0 =
      Await.result(futurePair, 1.second)

    import cats.syntax.apply._

    case class Cat(
        name: String,
        yob: Int,
        diet: List[String]
    )

    val e1 = (
      Future("garfield"),
      Future(1978),
      Future(List("lasagnue"))
    ) mapN (Cat)

    val e2 = Await.result(e1, 1.second)

    import cats.instances.either._ // for semigroupal

    type ErrorOr[A] = Either[Vector[String], A]

    val e3 = Semigroupal[ErrorOr] product (
      Left(Vector("Error 1")),
      Left(Vector("Error 2"))
    )

  }

  object Section4 {
    import cats.Semigroupal
    import cats.data.Validated
    import cats.instances.list._

    type AllErrorsOr[A] = Validated[List[String], A]

    val e0 = Semigroupal[AllErrorsOr]
      .product(
        Validated.invalid(List("e1")),
        Validated.invalid(List("e2"))
      )

    val v = Validated.Valid(123)
    val i = Validated.Invalid(List("baaad"))

    import cats.syntax.validated._

    val e2 = 123.valid[List[String]]
    val e3 = List("badness").invalid[Int]

    import cats.syntax.applicative._
    import cats.syntax.applicativeError._

    type ErrorsOr[A] = Validated[List[String], A]

    val e4 = 123.pure[ErrorsOr]

    val e5 = List("Badness").raiseError[ErrorsOr, Int]

    import cats.syntax.either._

  }

  // apply and applicative
  // see the hierachy of typeclasses
}
