package app.vineshbuilds.news.home.service

import app.vineshbuilds.news.home.view.model.NewsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsModel>
}
