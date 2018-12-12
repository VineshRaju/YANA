package app.vineshbuilds.news

import android.app.Application
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NewsApplication : Application() {
    lateinit var retrofit: Retrofit
    val moshi = Moshi.Builder().build()!!

    companion object {
        lateinit var INSTANCE: NewsApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
