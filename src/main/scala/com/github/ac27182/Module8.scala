package com.github.ac27182

// case study: testing asynchronous code
object Module8 {

  // prelude
  object Section0 {
    import scala.concurrent.Future

    trait UptimeClient {
      def getUptime(hostname: String): Future[Int]

    }

    import cats.instances.future._
    import cats.instances.list._
    import cats.syntax.traverse._
    import scala.concurrent.ExecutionContext.Implicits.global

    class UptimeService(client: UptimeClient) {
      def getTotalUptime(hostnames: List[String]): Future[Int] =
        hostnames.traverse(client.getUptime) map (_.sum)
    }

    class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient {
      def getUptime(hostname: String): Future[Int] =
        Future.successful(hosts getOrElse (hostname, 0))
    }

    def testTotalUptime() = {
      val hosts    = Map("host1" -> 10, "host2" -> 6)
      val client   = new TestUptimeClient(hosts)
      val service  = new UptimeService(client)
      val actual   = service getTotalUptime hosts.keys.toList
      val expected = hosts.values.sum
      // assert(actual == expected)
    }
  }

}
