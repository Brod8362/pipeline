package pw.byakuren.piplate.apps
import javafx.scene.Node
import javafx.scene.layout.Pane

class BlankApp extends PipelineApp{
  override val root: Node = new Pane()
  override val name: String = "blank screen"
}
