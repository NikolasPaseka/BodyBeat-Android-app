package cz.mendelu.xpaseka.bodybeat.fragments.parks

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.model.Park
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class UploadParkViewModel : ViewModel() {
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

    var selectedImage: Drawable? = null

    var latitude: Double = 0.0
    var longitude: Double = 0.0
}