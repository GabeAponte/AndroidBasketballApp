package api

/**
 * Response for the OpenWeather Json object that gets the value for name and the Json Object of main
 */
class OpenWeatherResponse {
    lateinit var main: MainResponse
    lateinit var name: String
}