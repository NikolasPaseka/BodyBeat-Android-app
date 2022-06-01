package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModel
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.mendelu.xpaseka.bodybeat.model.Park
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class UploadParkViewModel : ViewModel() {
    // 10.0.2.2 is localhost address of PC not android emulator
    private val base_url = "http://10.0.2.2:3000/createPark/"
    private val retrofit = Retrofit.Builder().baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    interface ApiService {
        @POST(".")
        fun postData(@Body park: Park): Call<Park>
    }

    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}