package pw.byakuren.piplate.util


import javafx.scene.control.Label

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Util {

  val timeFormatter12: DateTimeFormatter = DateTimeFormatter.ofPattern("h:m a")
  val timeFormatter24: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m")
  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("LLLL d, uuuu")

  def updateClockAndDate(clock: Label, date: Label, use24hour: Boolean): Unit = {
    val now = LocalDateTime.now()
    val formatter = if (use24hour) timeFormatter24 else timeFormatter12
    clock.setText(now.format(formatter))
    date.setText(now.format(dateFormatter))
  }

}
