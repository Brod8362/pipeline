package pw.byakuren.piplate.util


import javafx.scene.control.Label

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Util {

  val timeFormatter12: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
  val timeFormatter24: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("LLLL d, uuuu")

  def getDateText(): String = {
    LocalDateTime.now.format(dateFormatter)
  }

  def getClockText(use24hour: Boolean): String = {
    val formatter = if (use24hour) timeFormatter24 else timeFormatter12
    LocalDateTime.now.format(formatter)
  }



}
