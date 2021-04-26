package pw.byakuren.piplate

import javafx.scene.control.Label
import pw.byakuren.piplate.util.Util.updateClockAndDate
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
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

  val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(20)

  val timeLabel: Label = stage.getScene.lookup("#time_label").asInstanceOf[Label]
  val dateLabel: Label = stage.getScene.lookup("#date_label").asInstanceOf[Label]

  executor.scheduleAtFixedRate(() => {
    updateClockAndDate(timeLabel, dateLabel, use24hour = true)
  }, 0, 1, TimeUnit.SECONDS)

}
