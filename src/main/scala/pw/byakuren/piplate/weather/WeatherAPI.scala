package pw.byakuren.piplate.weather

import play.api.libs.json.Json
import scalafx.application.Platform
import scalafx.beans.property.StringProperty

import java.net.URL
import java.util.concurrent.{ScheduledExecutorService, TimeUnit}

object WeatherAPI {

  def initialize(scheduler: ScheduledExecutorService, property: StringProperty, key: String, zip: String, units: String): Unit = {
    val url = new URL(f"http://api.openweathermap.org/data/2.5/weather?zip=$zip&appid=$key&units=$units")
    scheduler.scheduleAtFixedRate(() => {
      try {
        val report = call(url)
        Platform.runLater({
          property.set(report.toString)
          //todo icon(?)
        })
      } catch {
        case e: Exception =>
          Platform.runLater({
            property.setValue("API Error")
          })
          e.printStackTrace()
      }
    }, 1, 60, TimeUnit.SECONDS)

  }

  def call(url: URL): WeatherReport = {
    val con = url.openConnection()
    val is = con.getInputStream
    val json = Json.parse(is)
    new WeatherReport(json("main")("temp").toString.toDouble.toInt,
      json("main")("feels_like").toString.toDouble.toInt,
      json("main")("humidity").toString.toInt,
      json("weather")(0)("main").toString,
      json("name").toString,
      json("weather")(0)("icon").toString)
  }

  //api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}&appid={API key}
}
