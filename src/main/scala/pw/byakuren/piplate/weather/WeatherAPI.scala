package pw.byakuren.piplate.weather

import javafx.beans.property.ObjectProperty
import javafx.scene.control.ProgressIndicator
import javafx.scene.image.{Image, ImageView}
import play.api.libs.json.{JsString, Json}
import scalafx.application.Platform
import scalafx.beans.property.StringProperty

import java.net.URL
import java.util.concurrent.{ScheduledExecutorService, TimeUnit}

object WeatherAPI {

  def initialize(scheduler: ScheduledExecutorService, textProp: StringProperty, icon: ImageView,
                 key: String, zip: String, units: String): Unit = {
    val url = new URL(f"http://api.openweathermap.org/data/2.5/weather?zip=$zip&appid=$key&units=$units")
    scheduler.scheduleAtFixedRate(() => {
      try {
        Platform.runLater({
          val report = call(url, units)
          textProp.set(report.toString)
          System.out.println(report.icon)
          icon.setImage(new Image(s"http://openweathermap.org/img/wn/${report.icon}.png"))
        })
      } catch {
        case e: Exception =>
          Platform.runLater({
            textProp.setValue("API Error")
          })
          e.printStackTrace()
      }
    }, 1, 60, TimeUnit.SECONDS)

  }

  def call(url: URL, unit: String): WeatherReport = {
    val con = url.openConnection()
    val is = con.getInputStream
    val json = Json.parse(is)
    new WeatherReport(json("main")("temp").toString.toDouble.toInt,
      json("main")("feels_like").toString.toDouble.toInt,
      json("main")("humidity").toString.toInt,
      json("weather")(0)("main").asInstanceOf[JsString].value,
      json("name").asInstanceOf[JsString].value,
      json("weather")(0)("icon").asInstanceOf[JsString].value,
      unit)
  }

  //api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}&appid={API key}
}
