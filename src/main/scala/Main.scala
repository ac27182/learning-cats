object Part1 extends App {

  sealed trait Json
  final case class JsonObject(get: Map[String, Json]) extends Json
  final case class JsonString(get: String) extends Json
  final case class JsonNumber(get: Double) extends Json
  case object JsNull extends Json
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
            "name" -> JsonString(value.name),
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

  // import JsonWriterInstances.stringWriter
  import JsonSyntax.JsonWriterOps
  val json1 = person.toJson
  println(json1)
}
