package simulation

import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder


class Getflight extends Simulation {


  //1 Conf details

  val base = http.baseUrl(  "http://localhost:8080")
    .header("Accept", "application/json")

  // 2 Scenario Definition

  val sc =  scenario("GetAll")
    .exec(http("getallflight details")
    .get("/flights")
    .check(status.is(200)))

      .exec(http("GetById")
      .get("/flight/1")
      .check(status.is(200)))


  //3 load scenario

  setUp(
    sc.inject(


      atOnceUsers(5),
      rampUsers(10) during (60 )
    ).
      protocols(base)
  )

}
