package pw.byakuren.piplate

import javafx.animation.Timeline
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.Label
import javafx.util.Duration
import pw.byakuren.piplate.util.Util.{getClockText, getDateText}
import scalafx.Includes._
import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.StringProperty
import scalafx.scene.{Parent, Scene}
import scalafxml.core.{FXMLView, NoDependencyResolver}

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

object PipelineApp extends JFXApp {


  //load FXML

  //create threads for date/time

  //figure out how to load the center content, and make it useful

  //QR code for configuration?

  val root: Parent = FXMLView(getClass.getClassLoader.getResource("pipeline.fxml"), NoDependencyResolver)
  stage = new PrimaryStage() {
    scene = new Scene(root, 480, 320)
  }

  val timeLabel: Label = stage.getScene.lookup("#time_label").asInstanceOf[Label]
  val dateLabel: Label = stage.getScene.lookup("#date_label").asInstanceOf[Label]

  val timeProperty: StringProperty = StringProperty("--:--")
  val dateProperty: StringProperty = StringProperty("-")

  timeLabel.textProperty <== timeProperty
  dateLabel.textProperty <== dateProperty

  val executor = Executors.newScheduledThreadPool(20)

  executor.scheduleWithFixedDelay(() => {
    Platform.runLater(() => {
      timeProperty.set(getClockText(true))
      dateProperty.set(getDateText())
    })
  }, 0, 1, TimeUnit.SECONDS)

}
