package pw.byakuren.piplate.apps
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import pw.byakuren.piplate.Pipeline
import pw.byakuren.piplate.Pipeline.{dateProperty, executor, getClass, timeProperty, toml}
import pw.byakuren.piplate.util.Util.{getClockText, getDateText}
import scalafx.application.Platform
import scalafx.scene.Parent
import scalafxml.core.{FXMLView, NoDependencyResolver}

import java.util.concurrent.TimeUnit

class ClockApp extends PipelineApp {

  Pipeline.executor.scheduleWithFixedDelay(() => {
    Platform.runLater(() => {
      root.asInstanceOf[StackPane].getChildren.get(0).asInstanceOf[Label].setText(getClockText(Pipeline.use24))
    })
  }, 0, 5, TimeUnit.SECONDS)
  //lmao i stole this code from the main screen

  override val root: Node = {
    FXMLView(getClass.getClassLoader.getResource("apps/bigclock.fxml"), NoDependencyResolver)
  }
  override val name: String = "clock"
}
