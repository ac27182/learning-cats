package com.github.ac27182

// monad transformers
object Module5 {
  object Section1 {
    import cats.Monad
    import cats.syntax.applicative._
    import cats.syntax.flatMap._

    // implementation wont compile
    // it is imposible to write a generaal definition of flatmap without knowing anything
    // about M1 or M2
    def compose[M1[_]: Monad, M2[_]: Monad] = ???

  }

  // transformer examples
  object Section2 {
    import cats.data.OptionT

    type ListOption[A] = OptionT[List, A]

    import cats.Monad
    import cats.instances.list._
    import cats.syntax.applicative._

    val result1: ListOption[Int] =
      OptionT(List(Option(10)))

    val result2: ListOption[Int] =
      32.pure[ListOption]

    // here map and flatmap combine the corresponding methods of list and option into single operations
    val e0 = result1.flatMap({ (x: Int) =>
      result2.map { (y: Int) =>
        x + y
      }
    })

  }

  //monad transformers in cats
  object Section3 {
    import cats.data.OptionT
    import cats.Monad
    import cats.syntax.applicative._
    // many monads and all transformers have at least 2 type parameters
    type ErrorOr[A]       = Either[String, A]
    type ErrorOrOption[A] = OptionT[ErrorOr, A]

    import cats.instances.either._

    val e0 = 10.pure[ErrorOrOption]
    val e1 = 32.pure[ErrorOrOption]
    val e2 = e0.flatMap(x => e1.map(y => x + y))

    case class EitherT[F[_], E, A](stack: F[Either[E, A]]) {}

    import scala.concurrent.Future
    import cats.data.{EitherT => EitherTT, OptionT}

    type FutureEither[A]       = EitherTT[Future, String, A]
    type FutureEitherOption[A] = OptionT[FutureEither, A]

    import cats.instances.future._
    import scala.concurrent.Await
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    val futureEitherOr: FutureEitherOption[Int] =
      for {
        a <- 10.pure[FutureEitherOption]
        b <- 32.pure[FutureEitherOption]
      } yield a + b

    // constructing and unpacking instances

    val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))
    val errorStack2 = 32.pure[ErrorOrOption]
    val e3          = errorStack1.value
    val e4          = errorStack2.value map (_ getOrElse -1)

    val i0    = futureEitherOr.value
    val stack = i0.value
    val a0    = Await.result(stack, 1.second)

    // usage patterns

    // creating a super stack
    sealed abstract class HttpError
    final case class NotFound(item: String)  extends HttpError
    final case class BadRequest(msg: String) extends HttpError

    type FutureEither0[A] = EitherT[Future, HttpError, A]

    // this pattern tends to break down in larger code bases

    import cats.data.Writer
    type Logged[A] = Writer[List[String], A]

    def parseNumber(str: String): Logged[Option[Int]] =
      util.Try(str.toInt).toOption match {
        case Some(value) => Writer(List("read"), Some(value))
        case None        => Writer(List("failed"), None)
      }

    def addAll(a: String, b: String, c: String): Logged[Option[Int]] = {
      import cats.data.OptionT
      import cats.implicits._

      val result = for {
        a <- OptionT(parseNumber(a))
        b <- OptionT(parseNumber(b))
        c <- OptionT(parseNumber(c))
      } yield a + b + c

      result.value
    }

    val result1 = addAll("1", "2", "3")
    val result2 = addAll("1", "a", "3")

    // EitherT[Option, String, A] is eqivilent to
    // Option[Either[String, A]]
  }
}
