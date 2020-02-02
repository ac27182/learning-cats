package com.github.ac27182

object Module3 extends App {
  object Section1 {
    val e0 = List(1, 2, 3, 4, 5).map(n => n + 1)

    val e1 = List(1, 2, 3, 4, 5)
      .map(n => n + 1)
      .map(n => n * 2)
      .map(n => s">$n<")

  }

  // more examples of functors
  object Section2 {

    // future
    import scala.concurrent.{Future, Await}
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    val future: Future[String] =
      Future(123)
        .map(n => n + 1)
        .map(n => s">$n")

    val e0 = Await.result(future, 1.second)

    // single argument functions
    import cats.instances.function._
    import cats.syntax.functor._

    val f1: Int => Double    = (x: Int) => x.toDouble
    val f2: Double => Double = (y: Double) => y * 2

    val e1 = (f1 map f2)(10)
    val e2 = (f1 andThen f2)(100)
    val e3 = f2(f1(100))
    val e4 = f1.map(f => f2(f))(10)

  }

  object Section3 {

    trait Functor[F[_]] {
      def map[A, B](fa: F[A])(f: A => B): F[B]
    }

  }
  // higher kinded types and type constuctors revision
  object Section4 {
    // kinds are like types of types
    // List type constructor
    // List[A] type
    // f function
    // f(x) value
  }

  // functors in cats
  object Section5 {
    import cats.Functor
    import cats.instances.list._
    import cats.instances.option._

    val e0 = List(1, 2, 3, 4)
    val e1 = Functor[List].map(e0)(_ * 2)
    val e2 = Option(1243)
    val e3 = Functor[Option].map(e2)(_.toString)

    // a functor contains a `lift` method, which takes a function of type A => B, to F[A] => F[B]

    val f0: Int => Int = x => x + 1
    val lift0          = Functor[Option].lift(f0)

    import cats.instances.function._
    import cats.syntax.functor._
    import cats.syntax.semigroup._
    import cats.instances.string._
    import cats.instances.double._
    import cats.instances.option._
    import cats.instances.int._

    val f1: Int => Int    = x => x + 1
    val f2: Int => Int    = x => x + 2
    val f3: Int => String = x => x + "!"
    val f4: Int => String = f1.map(f2).map(f3)

    def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
      start.map(n => n + 1 * 2)

    val g0 = doMath(Option(20))
    val g1 = doMath(List(1, 2, 3, 4, 5))

    // implicit class FunctorOps[F[_], A](src: F[A]) {
    //   def map[B](func: A => B)(implicit functor: Functor[F]): F[B] =
    //     functor.map(src)(func)
    // }

    // functor instances for custom types
    implicit val optionFunctor: Functor[Option] = new Functor[Option] {
      def map[A, B](value: Option[A])(func: A => B): Option[B] = value.map(func)
    }

    import scala.concurrent.{Future, ExecutionContext}
    implicit def futureFunctor(implicit ex: ExecutionContext): Functor[Future] =
      new Functor[Future] {
        def map[A, B](value: Future[A])(func: A => B): Future[B] =
          value.map(func)
      }
  }

  // contravariant and invariant functors
  object Section6 {

    // covariance functor => contramap

    // invariant functors => imap

  }

  object Section7 {
    import cats.Contravariant
    import cats.Show
    import cats.instances.string._

    val showString = Show[String]

    val showSymbol = Contravariant[Show].contramap(showString)((sym: Symbol) =>
      s"'${sym.name}'"
    )
    val e0 = showSymbol.show('dave)

    import cats.syntax.contravariant._

    val e1 =
      showString
        .contramap[Symbol](_.name)
        .show('dave)

  }

}
