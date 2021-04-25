package pw.byakuren.piplate

import scalafx.scene.Parent
import scalafx.application.JFXApp
import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}

object PipelineApp extends JFXApp {

  //load FXML

  //create threads for date/time

  //figure out how to load the center content, and make it useful

  //QR code for configuration?

  val root: Parent = FXMLView(getClass.getClassLoader.getResource("pipeline.fxml"), NoDependencyResolver)
  stage = new PrimaryStage() {
    scene = new Scene(root, 480, 320)
  }

}
