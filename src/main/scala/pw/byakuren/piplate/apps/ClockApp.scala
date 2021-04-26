package pw.byakuren.piplate.apps
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import pw.byakuren.piplate.Pipeline
import scalafx.Includes.jfxProperty2sfx
import scalafxml.core.{FXMLView, NoDependencyResolver}

class ClockApp extends PipelineApp {

  override val root: Node = {
    FXMLView(getClass.getClassLoader.getResource("apps/bigclock.fxml"), NoDependencyResolver)
  }

  root.asInstanceOf[StackPane].getChildren.get(0).asInstanceOf[Label].textProperty <== Pipeline.timeProperty

  override val name: String = "clock"
}
