package cz.mendelu.xpaseka.bodybeat.fragments.parks

import androidx.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.mendelu.xpaseka.bodybeat.model.Park
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class ParksViewModel : ViewModel() {
    // 10.0.2.2 is localhost address of PC not android emulator
    //private val baseUrl = "http://10.0.2.2:3000/parks/"
    private val baseUrl = "https://infinite-fjord-85163.herokuapp.com/parks/"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(baseUrl).build()

    interface ApiService {
        @GET(".")
        fun getAllData(): Call<List<Park>>
    }

    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }

    var parks: MutableList<Park> = mutableListOf()
}