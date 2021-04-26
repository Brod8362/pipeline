package pw.byakuren.piplate

import com.moandjiezana.toml.Toml
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import pw.byakuren.piplate.apps.{BlankApp, ClockApp, PipelineApp}
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

object Pipeline extends JFXApp {


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

  val use24 = toml.getString("time.format") == "24"
  executor.scheduleWithFixedDelay(() => {
    Platform.runLater(() => {
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

  val main: BorderPane = stage.getScene.lookup("#border_pane").asInstanceOf[BorderPane]
  val apps = Seq(new BlankApp(), new ClockApp())
  var appIndex = 0
  setCenter(apps(appIndex))

  val prevEvent = new EventHandler[ActionEvent] {
    override def handle(t: ActionEvent): Unit = {
      appIndex-=1
      if (appIndex<0) appIndex = apps.size-1
      setCenter(apps(appIndex))
    }
  }

  val nextEvent = new EventHandler[ActionEvent] {
    override def handle(t: ActionEvent): Unit = {
      appIndex+=1
      appIndex%=apps.size
      setCenter(apps(appIndex))
    }
  }

  stage.getScene.lookup("#scroll_left").asInstanceOf[Button].setOnAction(prevEvent)
  stage.getScene.lookup("#scroll_right").asInstanceOf[Button].setOnAction(nextEvent)

  def setCenter(app: PipelineApp): Unit = {
    main.setCenter(apps(appIndex).root)
    stage.getScene.lookup("#title_label").asInstanceOf[Label].setText(app.name)
  }

  def forceFocus(`new`: String): Int = {
    val prev = appIndex
    for (i <- apps.indices) {
      if (apps(i).name == `new`) {
        appIndex=i
      }
    }
    setCenter(apps(appIndex))
    if (prev==appIndex) -1 else appIndex
  }
}
