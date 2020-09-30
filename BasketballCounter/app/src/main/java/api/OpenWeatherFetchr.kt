package api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import basketballCounter.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "OpenWeatherFetchr"

/**
 * Class that fetches information from the OpenWeatherAPI
 */
class OpenWeatherFetchr {
    private val openWeatherApi: OpenWeatherApi

    // Create a Retrofit Builder and GsonConverterFactory on initialization
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        openWeatherApi = retrofit.create(OpenWeatherApi::class.java)
    }

    /**
     * Creates a LiveData version of WeatherData and populates the fields with json values from the API call
     */
    fun fetchContents(): LiveData<WeatherData> {
        val responseLiveData: MutableLiveData<WeatherData> = MutableLiveData()
        val openWeatherRequest: Call<OpenWeatherResponse> = openWeatherApi.fetchContents()

        openWeatherRequest.enqueue(object : Callback<OpenWeatherResponse> {
            override fun onFailure(call: Call<OpenWeatherResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch weather data", t)
            }
            override fun onResponse(
                call: Call<OpenWeatherResponse>,
                response: Response<OpenWeatherResponse>
            ) {
                Log.d(TAG, "Response received")
                val openWeatherResponse: OpenWeatherResponse? = response.body()
                val mainResponse: MainResponse? = openWeatherResponse?.main
                val weatherData = WeatherData(openWeatherResponse?.name!!, mainResponse?.temp!!, mainResponse.humidity
                )

                responseLiveData.value = weatherData
            }
        })
        return responseLiveData
    }
}