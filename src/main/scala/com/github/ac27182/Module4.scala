package com.github.ac27182

import scala.concurrent.Future
import com.github.ac27182.Module4.Section4.UserNotFound
import com.github.ac27182.Module4.Section4.PasswordIncorrect
import com.github.ac27182.Module4.Section4.UnexpectedError
import com.github.ac27182.Module4.Section9.Branch
import com.github.ac27182.Module4.Section9.Leaf

// monads
object Module4 {
  object Section1 {

    def parseInt(str: String): Option[Int] = scala.util.Try(str.toInt).toOption

    def divide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

    def stringDivideBy(aStr: String, bStr: String): Option[Int] =
      parseInt(aStr).flatMap(aNum =>
        parseInt(bStr).flatMap(bNum => divide(aNum, bNum))
      )
    def stringDivideBy0(aStr: String, bStr: String): Option[Int] =
      for {
        aNum <- parseInt(aStr)
        bNum <- parseInt(bStr)
        ans  <- divide(aNum, bNum)
      } yield ans

    // future is a monad

    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration

    def doSomethingLongRunning: Future[Int]     = ???
    def doSomethingElseLongRunning: Future[Int] = ???
    def doSomethingVeryLongRunning: Future[Int] =
      for {
        result1 <- doSomethingLongRunning
        result2 <- doSomethingElseLongRunning
      } yield result2 + result1

    // simplified monad
    trait Monad[F[_]] {
      def pure[A](value: A): F[A]
      def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
    }

  }
  // monads in cats
  object Section2 {
    import cats.Monad
    import cats.instances.option._
    import cats.instances.list._

    val opt1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)

    val list1 = Monad[List].pure(3)
    val list2 = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
    val list3 = Monad[List].map(list2)(a => a + 123)

    // cats provides instances for all the monads in the standard library
    import cats.instances.option._

    val e0 = Monad[Option]
      .flatMap(Option(1))(a => Option(a * 2))

    import cats.instances.list._

    val e1 = Monad[List]
      .flatMap(List(1, 2, 3))(a => List(a, a * 10))

    import cats.instances.vector._

    val e2 = Monad[Vector]
      .flatMap(Vector(1, 2, 3, 4))(i => Vector(i, i * 10))

    import cats.instances.future._
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global
    val futureMonad = Monad[Future]

    val future =
      futureMonad.flatMap(futureMonad.pure(1))(x => futureMonad.pure(x + 1))

    val e3 = Await.result(future, 1.second)

    import cats.instances.option._
    import cats.instances.list._
    import cats.syntax.applicative._

    val e4 = 1.pure[Option]
    val e5 = 1.pure[List]

    import cats.Monad
    import cats.syntax.functor._
    import cats.syntax.flatMap._
    // import cats.implicits._
    // import scala.language.higherKinds
    def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
      a.flatMap(x => b.map(y => x * x + y * y))

    import cats.instances.option._
    import cats.instances.list._

    val e6 = sumSquare[Option](Option(3), Option(6))

    val e7 = sumSquare[List](List(1, 2, 3, 4, 5), List(6, 5, 4, 3, 2, 1))

  }
  // the identitiy monad
  object Section3 {
    import cats.Monad
    import cats.syntax.functor._
    import cats.syntax.flatMap._

    def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
      for {
        x <- a
        y <- b
      } yield x * x + y * y

    // Id allows us to call monadic methods using plain values
    import cats.Id

    val e0 = sumSquare(3: Id[Int], 4: Id[Int])
  }

  // the Either Monad
  object Section4 {
    val either0: Either[String, Int] = Right(10)
    val either1: Either[String, Int] = Right(69)

    val e0 = for {
      a <- either0.right
      b <- either1.right
    } yield a + b

    import cats.syntax.either._

    val a = 3.asRight[String]

    // catchOnly, and catchNonFatal are very good for capturing exceptions

    val e1 = Either.catchOnly[NumberFormatException]("foo".toInt)
    val e2 = Either.catchNonFatal(sys.error("Badness"))

    import cats.syntax.either._

    val e3 = "Error".asLeft[Int] getOrElse 0

    // ensure allows one to check whether a value satisfies apredicate

    val e4 = -1.asRight[String].ensure("must be non-negative")(_ > 0)

    // recover and recover with provide useful error handling sytax

    val e5  = "error".asLeft[Int].recover { case str: String => -1 }
    val e6  = "error".asLeft[Int].recoverWith { case str: String => Right(-1) }
    val e7  = "error".asLeft[Int].leftMap(_.reverse)
    val e8  = 6.asRight[String].bimap(_.reverse, _ * 7)
    val e9  = "bar".asLeft[Int].bimap(_.reverse, _ * 7)
    val e10 = 123.asRight[String].swap

    // when error handling we can have a left of throwable, or some ADT to handle the error

    sealed trait LoginError extends Product with Serializable

    final case class UserNotFound(username: String)      extends LoginError
    final case class PasswordIncorrect(username: String) extends LoginError
    final case object UnexpectedError                    extends LoginError

    case class User(username: String, password: String)

    type LoginResult = Either[LoginError, User]

    // here we have a series of well defined errors, and a catch all error case object

    def handleError(error: LoginError): Unit = error match {
      case UserNotFound(username)      => println("user not found")
      case PasswordIncorrect(username) => println("password incorrect")
      case UnexpectedError             => println("unexpected error")
    }

    val r0: LoginResult = User("dave", "passwordd").asRight
    val r1: LoginResult = UserNotFound("alex").asLeft
    val r2              = r0.fold(handleError, println)
    val r3              = r1.fold(handleError, println)

  }

  // the monad error
  object Section5 {
    import cats.MonadError
    import cats.instances.either._

    type ErrorOr[A] = Either[String, A]

    val monadError = MonadError[ErrorOr, String]

    val success = monadError.pure(42)

    val failure = monadError.raiseError("Badness")

    val e0 = monadError.handleError(failure) {
      case "Badness" => monadError.pure("everythings going to be okay")
      case other     => monadError.raiseError("nothing is okay...")
    }

    import cats.syntax.either._

    val e1 = monadError.ensure(success)("Number too low")(_ > 1000)

    import cats.syntax.applicative._
    import cats.syntax.applicativeError._

    import cats.syntax.monadError._

    val success0 = 42.pure[ErrorOr]
    val failure0 = "Badness".raiseError[ErrorOr, Int]

  }

  // the eval monad
  object Section6 {

    // eager and memoized
    val x = math.random

    // lazy and not memoized
    def y = math.random

    // lazy and memoized
    lazy val z = math.random

    import cats.Eval

    val now    = Eval.now(math.random * 100)
    val later  = Eval.later(math.random + 2000)
    val always = Eval.always(math.random + 3000)

    // we extract the value of an eval by calling its value

    val e0 = now.value
    val e1 = later.value
    val e2 = always.value

    val greeting = Eval
      .always {
        println("step 1"); "Hello"
      }
      .map { str =>
        println("step 2"); s">$str"
      }

    // stack safe factorial
    def factorial(n: BigInt): Eval[BigInt] =
      if (n == 1) {
        Eval.now(n)
      } else {
        Eval.defer(factorial(n - 1).map(_ * n))
      }

  }

  // the writer monad
  object Section7 {

    import cats.data.Writer
    import cats.data.WriterT
    import cats.instances.vector._

    val e0 = Writer(Vector("log1", "log2"), 1999)

    import cats.syntax.applicative._
    type Logged[A] = Writer[Vector[String], A]

    val e1 = 123.pure[Logged]

    import cats.syntax.writer._

    val e2 = Vector("msg1", "msg2", "msg3").tell

    import cats.syntax.writer._

    val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
    val b = 123.writer(Vector("msg1", "msg2", "msg3"))

    val aResult: Int         = a.value
    val aLog: Vector[String] = a.written
    val (log, result)        = b.run

    val writer1 = for {
      a <- 10.pure[Logged]
      _ <- Vector("x", "y", "z").tell
      b <- 32.writer(Vector("a", "b", "c"))
    } yield a + b

    val e3 = writer1.run

    val writer2 = writer1.mapWritten(_.map(_.toUpperCase))

    val e4 = writer2.run

    val writer3 = writer1.bimap(log => log.map(_.toUpperCase), res => res * 100)

    val e5 = writer3.run

  }
  // the reader monad
  object Section8 {
    import cats.data.Reader
    case class Cat(name: String, favouriteFood: String)

    val catName: Reader[Cat, String] = Reader(cat => cat.name)
    val e0                           = catName.run(Cat("garfield", "lasagn"))

    val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello $name")
    val e1                              = greetKitty.run(Cat("peter barakan", "sushi"))

    val feedKitty: Reader[Cat, String] =
      Reader(cat => s"have a nice bewl -f ${cat.favouriteFood}")

    val greetAndFeed: Reader[Cat, String] = for {
      greet <- greetKitty
      feed  <- feedKitty
    } yield s"$greet $feed"
  }

  // the state monad
  object Section9 {
    // creating and unpacking state
    // instances of state represent S => (S,A)
    // where S is the state type, and A is the result

    import cats.data.State

    val s0: State[Int, String] =
      State[Int, String](state => (state, s"the state is $state"))

    // .. an instance of state does the following
    // transforms an input state to an output state
    // computes a result

    val (state, result) = s0.run(10).value
    val s1              = s0.runS(10).value
    val s2              = s0.runA(10).value

    // composing and transofrming state
    val step1 = State[Int, String](num => (num + 1, s"> step1: $num"))
    val step2 = State[Int, String](num => (num * 100, s"> step2: $num"))

    val both = for {
      a <- step1
      b <- step2
    } yield (a, b)

    val (state0, result0) = both.run(20).value

    import cats.Monad
    import scala.annotation.tailrec

    val optionMonad = new Monad[Option] {
      def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
        fa flatMap f
      def pure[A](x: A): Option[A] = Some(x)
      @tailrec
      def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
        f(a) match {
          case Some(value) =>
            value match {
              case Left(value)  => tailRecM(value)(f)
              case Right(value) => Some(value)
            }
          case None => None
        }
    }

    sealed trait Tree[+A]
    final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
    final case class Leaf[A](value: A)                        extends Tree[A]

    val treeMonad = new Monad[Tree] {

      def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
        fa match {
          case Branch(left, right) =>
            Branch(flatMap(left)(f), flatMap(right)(f))
          case Leaf(value) => f(value)
        }

      def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
        flatMap(f(a)) {
          case Left(value)  => tailRecM(value)(f)
          case Right(value) => Leaf(value)
        }

      def pure[A](x: A): Tree[A] = Leaf(x)

    }
  }
}
