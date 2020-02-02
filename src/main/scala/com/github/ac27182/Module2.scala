package com.github.ac27182

// monoid and semigroupal typeclasses
object Module2 {
  // defining the monoid
  object Section1 {
    trait Monoid[A] {
      def combine(x: A, y: A): A
      def empty: A
    }
    // for this monoid to be correct it must adhere to the laws of identity and associativity
    def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean =
      m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)

    def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
      (m.combine(x, m.empty) == x) &&
        (m.combine(m.empty, x) == x)

    // its important to remember that unlawful monoidal instances are really dangerous, becuase they can yield unpredictable results

  }

  // defining the semigroup
  object Section2 {
    trait Semigroup[A] {
      def combine(x: A, y: A): A
    }
    trait Monoid[A] extends Semigroup[A] {
      def empty: A
    }

  }

  // monoids in cats
  object Section5 {
    // the monoid type class
    import cats.Monoid
    import cats.Semigroup

    import cats.instances.string._

    val e0 = Monoid[String].combine("hello", "world")
    val e1 = Monoid[String].empty

    val e2 = Semigroup[String].combine("hello", "world")

    // we may assemble a Monoidal instance of Option from cats instances
    import cats.instances.int._
    import cats.instances.double._
    import cats.instances.option._

    val e3 = Option(22)
    val e4 = Option(88)

    val e5 = Monoid[Option[Int]].combine(e3, e4)

    import cats.syntax.semigroup._

    val e6 = "hello" |+| "world" |+| "my" |+| "name" |+| "is" |+| "alex"

    def add0(i: List[Int]): Int =
      i.fold(Monoid[Int].empty)((acc, n) => acc |+| n)

    case class Order(totalCost: Double, quantity: Double)

    def add0(o: Order): Double =
      o.totalCost |+| o.quantity

  }

}
