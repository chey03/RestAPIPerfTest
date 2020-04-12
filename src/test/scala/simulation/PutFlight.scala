package simulation


import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import scala.util.Random
import scala.concurrent.duration._


class PutFlight extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/")
    .header("Accept", "application/json")

  var id = (1 to 1000).iterator

  val rnd = new Random()
 // val new_val = id.next()


  val customFeeder = Iterator.continually(Map(

    "price" -> ("$" + rnd.nextInt(1000))
  ))

  def putNewFlight() = {
    repeat(1) {
      feed(customFeeder)
        .exec(http("Post New Flight")
          .put(s"flight/${id}")
          .body(ElFileBody("bodies/putbody.json")).asJson
          .check(status.is(200)))
        .pause(1)
    }
  }

  val scn = scenario("PUT API")
    .exec(putNewFlight())

  setUp(
    scn.inject(
       atOnceUsers(1),
       rampUsers(2) during (5 second))
      //constantConcurrentUsers(10) during (60 seconds), // 1
      //rampConcurrentUsers(10) to (20) during (60 seconds)) // 2)
      .protocols(httpConf)
  )



}
