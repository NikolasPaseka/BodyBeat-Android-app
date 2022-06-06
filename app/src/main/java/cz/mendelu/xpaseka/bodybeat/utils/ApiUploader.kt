package cz.mendelu.xpaseka.bodybeat.utils

import android.util.Log
import cz.mendelu.xpaseka.bodybeat.model.Park
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class ApiUploader {
    // 10.0.2.2 is localhost address of PC not android emulator
    //private val baseUrl = "http://10.0.2.2:3000/createPark/"
    private val baseUrl = "https://infinite-fjord-85163.herokuapp.com/createPark/"
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    interface ApiService {
        @POST(".")
        fun postData(@Body park: Park): Call<Park>
    }

    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }

    fun uploadToApi(name: String, url: String, latitude: Double, longitude: Double) {
        //val call = viewModel.retrofitService.postData(Park(binding.parkName.text, viewModel.latitude, viewModel.longitude, url))
        Log.i("upload", "uploading")
        val uploader = ApiUploader()
        val call = uploader.retrofitService.postData(Park(name, latitude, longitude, url))
        call.enqueue(object : retrofit2.Callback<Park> {
            override fun onResponse(call: Call<Park>, response: Response<Park>) {
            }

            override fun onFailure(call: Call<Park>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}