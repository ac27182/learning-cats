package com.github.ac27182

// foldable and traverse
object Module7 {
  // Foldable
  object Section1 {

    // in a fold we supply an accumulator and a binary function to combine it with each item in the sequence
    def show[A](list: List[A]): String =
      list.foldLeft("nil")((accum, item) => s"$item then $accum")

    val e0 = show(Nil)

    val e1 = show(List(1, 2, 3))

    import cats.Foldable
    import cats.instances.list._

    val ints = List(1, 2, 3)

    val e2 = Foldable[List].foldLeft(ints, 0)(_ + _)

    import cats.instances.option._

    val maybeInt = Option(123)

    val e3 = Foldable[Option].foldLeft(maybeInt, 10)(_ * _)

    import cats.Eval
    import cats.Foldable

    def bigData = (1 to 100000).toStream

    val e4 = bigData.foldRight(0L)(_ + _)
    // stack overflow...

    import cats.instances.stream._

    val eval: Eval[Long] =
      Foldable[Stream].foldRight(bigData, Eval.now(0L))((num, eval) =>
        eval.map(_ + num)
      )

    val e5 = eval.value

    val e6 = Foldable[Option].nonEmpty(Option(42))

    val e7 = Foldable[List].find(List(1, 2, 3))(_ % 2 == 0)

    import cats.instances.int._

    val e8 = Foldable[List].combineAll(List(1, 2, 3))

    import cats.instances.string._

    val e9 = Foldable[List].foldMap(List(1, 2, 3))(_.toString)

    import cats.instances.vector._

    val i0 = List(Vector(1, 2, 3), Vector(4, 5, 6))

    val i1 = (Foldable[List] compose Foldable[Vector]) combineAll i0

    import cats.syntax.foldable._

    val e10 = List(1, 2, 3).combineAll

    val e11 = List(1, 2, 3, 4, 5) foldMap (_.toString)

  }

  // Traverse
  object Section2 {
    // traversing wtih futures

    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global
    val hostnames = List(
      "www.google.com",
      "www.wejo.com",
      "www.github.com"
    )

    def getUptime(hostname: String): Future[Int] =
      Future(hostname.length * 60)

    val allUptimes: Future[List[Int]] =
      hostnames.foldLeft(Future(List.empty[Int])) { (accum, host) =>
        val uptime = getUptime(host)
        for {
          accum  <- accum
          uptime <- uptime
        } yield accum :+ uptime
      }

    val e0 = Await.result(allUptimes, 1.second)

    val allUptimes0: Future[List[Int]] = Future.traverse(hostnames)(getUptime)

    // traversing with applicatives
    val e1 = Future(List.empty[Int])

    import cats.Applicative
    import cats.instances.future._
    import cats.syntax.applicative._

    val e2 = List.empty[Int].pure[Future]

    // traverse in cats

    import cats.Traverse
    import cats.instances.future._
    import cats.instances.list._

    val totalUptime: Future[List[Int]] =
      Traverse[List].traverse(hostnames)(getUptime)

    val e3 = Await.result(totalUptime, 1.second)

    val numbers = List(Future(1), Future(2), Future(3))

    val numbers2: Future[List[Int]] = Traverse[List].sequence(numbers)

    val e4 = Await.result(numbers2, 1.second)

    import cats.syntax.traverse._

    val e5 = Await.result(hostnames.traverse(getUptime), 1.second)

    val e6 = Await.result(numbers.sequence, 1.second)
  }
}
