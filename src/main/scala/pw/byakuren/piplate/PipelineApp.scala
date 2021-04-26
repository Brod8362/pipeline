package pw.byakuren.piplate

import com.moandjiezana.toml.Toml
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import pw.byakuren.piplate.util.Util.{getClockText, getDateText}
import pw.byakuren.piplate.weather.WeatherAPI
import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.beans.property.StringProperty
import scalafx.scene.{Parent, Scene}
import scalafxml.core.{FXMLView, NoDependencyResolver}
import java.io.FileOutputStream
import java.util.concurrent.{Executors, TimeUnit}

object PipelineApp extends JFXApp {


  //load FXML

  //create threads for date/time

  //figure out how to load the center content, and make it useful

  //QR code for configuration?

  val root: Parent = FXMLView(getClass.getClassLoader.getResource("pipeline.fxml"), NoDependencyResolver)
  stage = new PrimaryStage() {
    scene = new Scene(root, 480, 320)
  }

  val configFile = new java.io.File("config.toml")
  if (!configFile.exists()) {
    val resource = scala.io.Source.fromURL(getClass.getClassLoader.getResource("config.toml"))
    val fos = new FileOutputStream("config.toml")
    fos.write(resource.mkString.getBytes)
    fos.close()
  }
  val toml = new Toml().read(configFile)

  val timeLabel: Label = stage.getScene.lookup("#time_label").asInstanceOf[Label]
  val dateLabel: Label = stage.getScene.lookup("#date_label").asInstanceOf[Label]
  val weatherLabel: Label = stage.getScene.lookup("#weather_label").asInstanceOf[Label]

  val timeProperty: StringProperty = StringProperty("--:--")
  val dateProperty: StringProperty = StringProperty("-")

  timeLabel.textProperty <== timeProperty
  dateLabel.textProperty <== dateProperty

  val executor = Executors.newScheduledThreadPool(20)

  executor.scheduleWithFixedDelay(() => {
    Platform.runLater(() => {
      val use24 = toml.getString("time.format") == "24"
      timeProperty.update(getClockText(use24))
      dateProperty.update(getDateText())
    })
  }, 0, 5, TimeUnit.SECONDS)

  val weatherApiKey: Option[String] = Option(toml.getString("weather.key"))
  weatherApiKey match {
    case Some(key) if key != "" =>
      val zip = toml.getString("weather.zip")
      val units = toml.getString("weather.units")
      val icon = weatherLabel.getGraphic.asInstanceOf[ImageView]
      WeatherAPI.initialize(executor, weatherLabel.textProperty(), icon, key, zip, units)

    case _ => weatherLabel.setText("No API Key")
  }

}
