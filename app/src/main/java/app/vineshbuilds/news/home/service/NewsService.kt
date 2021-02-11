package app.vineshbuilds.news.home.service

import app.vineshbuilds.news.home.view.model.NewsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    companion object {
        const val API_KEY = "c16da6826c8a4a588588e7fc1d2c0e24"
    }

    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsModel>
}
