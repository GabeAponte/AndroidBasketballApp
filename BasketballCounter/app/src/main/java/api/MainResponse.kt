package api

/**
 * Response that gets the temp and humidity values from the main JSON object in the OpenWeatherAPI response json object
 */
class MainResponse {
    lateinit var temp: String
    lateinit var humidity: String
}