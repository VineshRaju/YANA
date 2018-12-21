package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.ViewState
import app.vineshbuilds.news.home.viewmodel.ViewState.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Source : KoinComponent {
    fun getArticles(): LiveData<ViewState>
}

class OnlineNewsSource : Source {
    private val newsService: NewsService by inject()
    private val newsState = MutableLiveData<ViewState>()

    override fun getArticles(): LiveData<ViewState> {
        val call = newsService.getHeadlines("in")
        call.enqueue(object : Callback<NewsModel> {
            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                newsState.value = Error(t)
            }

            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                newsState.value = response.body()?.let { newsModel ->
                    val articles = newsModel.articles.map { ArticleVm(it) }
                    if (articles.isNotEmpty()) FromNetwork(articles) else Empty
                } ?: Empty
            }
        })
        return newsState
    }
}

class CachedNewsSource : Source {
    private val cacheStorage: StorageProvider by inject()
    private val newsState = MutableLiveData<ViewState>()

    override fun getArticles(): LiveData<ViewState> = newsState.apply {
        val articles = cacheStorage.getArticles()
        value = if (articles.isNotEmpty())
            FromCache(articles.map { ArticleVm(it) })
        else Empty
    }
}

