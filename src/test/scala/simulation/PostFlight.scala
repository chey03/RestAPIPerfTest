package simulation

import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import scala.util.Random
import scala.concurrent.duration._


class PostFlight extends Simulation{

  //val httpConf = http.baseUrl("http://localhost:8080/")
  val httpConf = http.baseUrl("http://192.168.56.1:8088/")
    .header("Accept", "application/json")

  var id = (101 to 100000).iterator

  val rnd = new Random()
  val hr = rnd.nextInt(24)
  val mm= rnd.nextInt(60)
  val ss = rnd.nextInt(60)
  val time = LocalTime.now()
  val pattern = DateTimeFormatter.ofPattern("HH:MM:SS")

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }
/*
  def getRandomTime(startTime:LocalTime, random: Random): String = {
    startTime.minusHours(random.nextInt(30)).format(pattern)
  }*/


  val customFeeder = Iterator.continually(Map(
    "Id" -> id.next(),
    "number" -> rnd.nextInt(1000000),
    "airline" -> ("TestAirline-" + randomString(5)),
    "departure_time" -> ( hr+ ":" + mm + ":" + ss) ,
    "arrival_time" -> ( hr+ ":" + mm + ":" + ss) ,
    "price" -> ("$" + rnd.nextInt(1000))
  ))

  def postNewFlight() = {
    repeat(1) {
      feed(customFeeder)
        .exec(http("Post New Flight")
          .post("flight/")
          .body(ElFileBody("bodies/postbody.json")).asJson
          .check(status.is(200)))
        .pause(0)
    }
  }

  val scn = scenario("Post API")
    .exec(postNewFlight())

  setUp(
    scn.inject(
     // atOnceUsers(10),
     // rampUsers(50) during (60 second)
      constantConcurrentUsers(10) during (60 seconds), // 1
      rampConcurrentUsers(10) to (20) during (60 seconds)) // 2)
    .protocols(httpConf)
  ).maxDuration(60 seconds)

}
