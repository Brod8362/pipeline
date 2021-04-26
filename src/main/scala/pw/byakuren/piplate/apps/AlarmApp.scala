package pw.byakuren.piplate.apps

import com.moandjiezana.toml.Toml
import scalafx.application.Platform
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Button, Label}
import pw.byakuren.piplate.Pipeline
import pw.byakuren.piplate.util.Util
import scalafxml.core.{FXMLView, NoDependencyResolver}

import java.util.concurrent.{ScheduledFuture, TimeUnit}

class AlarmApp(toml: Toml) extends PipelineApp {

  var enabled = true
  var scheduledSnooze: Option[ScheduledFuture[_]] = None

  override val root: Node = {
    FXMLView(getClass.getClassLoader.getResource("apps/alarm.fxml"), NoDependencyResolver)
  }
  override val name: String = "alarm"

  var setTime: String = Option(toml.getString("alarm.set")) match {
    case Some(time) if time.matches("[0-2]\\d:[0-5]\\d") =>
      Pipeline.executor.scheduleWithFixedDelay(() => {
        if (Util.getClockText(true) == setTime && enabled) {
          fire()
        }
      }, 0, 1, TimeUnit.MINUTES)
      time
    case Some(_) => "invalid"
    case _ => "not set"
  }

  //snooze/cancel are only available when the alarm is going off
  val snoozeButton: Button = root.lookup("#snooze").asInstanceOf[Button]
  val cancelButton: Button = root.lookup("#cancel").asInstanceOf[Button]
  val toggleButton: Button = root.lookup("#toggle").asInstanceOf[Button]
  snoozeButton.setVisible(false)
  cancelButton.setVisible(false)

  val snoozeEvent = new EventHandler[ActionEvent] {
    override def handle(t: ActionEvent): Unit = snooze()
  }
  val toggleEvent = new EventHandler[ActionEvent] {
    override def handle(t: ActionEvent): Unit = toggle()
  }
  val cancelEvent = new EventHandler[ActionEvent] {
    override def handle(t: ActionEvent): Unit = cancel()
  }

  snoozeButton.setOnAction(snoozeEvent)
  toggleButton.setOnAction(toggleEvent)
  cancelButton.setOnAction(cancelEvent)

  root.lookup("#settime").asInstanceOf[Label].setText(setTime)

  def fire(): Unit = {
    //play a sound of some kind (youtube video?)
    Platform.runLater({
      snoozeButton.setVisible(true)
      cancelButton.setVisible(true)
      Pipeline.forceFocus(name)
    })
  }

  def snooze(): Unit = {
    Platform.runLater({
      snoozeButton.setVisible(false)
      val future = Pipeline.executor.schedule(new Runnable() {
        override def run(): Unit = {
          Platform.runLater(fire())
        }
      }, 10, TimeUnit.MINUTES)
      scheduledSnooze = Some(future)
    })
  }

  def cancel(): Unit = {
    Platform.runLater({
      snoozeButton.setVisible(false)
      cancelButton.setVisible(false)
      if (scheduledSnooze.isDefined) {
        scheduledSnooze.get.cancel(true)
        scheduledSnooze = None
      }
      //stop the sound
    })
  }

  def toggle(): Unit = {
    Platform.runLater({
      val label = root.lookup("#toggle_label").asInstanceOf[Label]
      if (enabled) {
        enabled = false
        label.setText("disabled")
      } else {
        enabled = true
        label.setText("enabled")
      }
    })
  }
}
