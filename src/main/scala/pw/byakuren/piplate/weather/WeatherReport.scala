package pw.byakuren.piplate.weather

class WeatherReport(temperature: Int, feels_like: Int, humidity: Int, main: String, city_name: String, icon: String) {

  override def toString: String = s"$temperature° $main"

}
