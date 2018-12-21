package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.NewsState
import app.vineshbuilds.news.home.viewmodel.NewsState.*
import app.vineshbuilds.news.util.SharedPrefHelperImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Source {
    fun getArticles(): LiveData<NewsState>
}

class OnlineNewsSource(private val newsService: NewsService) : Source {
    private val articleState = MutableLiveData<NewsState>()

    companion object {
        const val API_KEY = "c16da6826c8a4a588588e7fc1d2c0e24"
    }

    override fun getArticles(): LiveData<NewsState> {
        val call = newsService.getHeadlines("in", API_KEY)
        call.enqueue(object : Callback<NewsModel> {
            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                articleState.value = Error(t)
            }

            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                articleState.value = response.body()?.let { newsModel ->
                    val articles = newsModel.articles.map { ArticleVm(it) }
                    if (articles.isNotEmpty()) ArticlesFromNetwork(articles) else Empty
                } ?: Empty
            }
        })
        return articleState
    }
}

class CachedNewsSource : Source {
    private val articles = MutableLiveData<NewsState>()
    private val cacheStorage: StorageProvider

    private val app = NewsApplication.INSTANCE

    init {
        cacheStorage = CachedStorageProvider(app.moshi, SharedPrefHelperImpl)
    }

    override fun getArticles(): LiveData<NewsState> {
        return articles.apply {
            value = ArticlesFromCache(cacheStorage.getArticles().map { ArticleVm(it) })
        }
    }

}

