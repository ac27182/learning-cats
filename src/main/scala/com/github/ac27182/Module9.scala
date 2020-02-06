package com.github.ac27182

// map reduce
object Module9 extends App {

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  val future1 = Future(
    (1 to 10000).toList
      .foldLeft(0)(_ + _)
  )

  val future2 = Future(
    (10001 to 20000).toList
      .foldLeft(0)(_ + _)
  )

  val future3 = future1.map(_.toString)

  val future4 = for {
    a <- future1
    b <- future2
  } yield a + b

  val e0 = Future.sequence(List(Future(1), Future(2), Future(3)))

  import cats.instances.future._
  import cats.instances.list._
  import cats.syntax.traverse._

  val e1 = List(
    Future(1),
    Future(2),
    Future(3)
  ).sequence

  import scala.concurrent._
  import scala.concurrent.duration._

  val e2 = Await.result(
    Future(1),
    1.second
  )

  val e3 = Runtime.getRuntime.availableProcessors

  println(e3)

  val e4 = (1 to 60000).toList
    .grouped(6)
    .toList
}
