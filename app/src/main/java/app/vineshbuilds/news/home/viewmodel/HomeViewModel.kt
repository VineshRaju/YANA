package app.vineshbuilds.news.home.viewmodel

import android.arch.lifecycle.ViewModel
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.repository.NewsSource
import app.vineshbuilds.news.home.repository.Source
import app.vineshbuilds.news.home.repository.service.NewsService

class HomeViewModel : ViewModel() {
    private val source: Source
    private val app = NewsApplication.INSTANCE

    init {
        val newsService = app.retrofit.create(NewsService::class.java)
        source = NewsSource(newsService)
    }

    fun refreshNews() = source.refreshNews()

    /* fun cacheNews(articles: List<ArticleModel>) {
         val type = Types.newParameterizedType(List::class.java, ArticleModel::class.java)
         val adapter = app.moshi.adapter<List<ArticleModel>>(type)
         val json: String = adapter.toJson(articles)
         SharedPrefHelper().putString("CACHE", json)
     }*/
}
