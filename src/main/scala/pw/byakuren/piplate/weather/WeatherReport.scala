package pw.byakuren.piplate.weather

case class WeatherReport(temperature: Int, feels_like: Int, humidity: Int, main: String, city_name: String, icon: String, unit: String) {

  override def toString: String = s"$temperature°${temperatureUnit} $main"

  def temperatureUnit: String = {
    unit match {
      case "metric" => "C"
      case "imperial" => "F"
      case "standard" => "K"
      case _ => "?"
    }
  }

}
