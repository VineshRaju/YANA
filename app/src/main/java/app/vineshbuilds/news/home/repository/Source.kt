package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel
import app.vineshbuilds.news.home.viewmodel.ArticleState
import app.vineshbuilds.news.home.viewmodel.ArticleState.ArticlesFromCache
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.util.SharedPrefHelperImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Source {
    fun getArticles(): LiveData<ArticleState>
}

class OnlineNewsSource(private val newsService: NewsService) : Source {
    private val articles = MutableLiveData<ArticleState>()

    companion object {
        const val API_KEY = "c16da6826c8a4a588588e7fc1d2c0e24"
    }

    override fun getArticles(): LiveData<ArticleState> {
        val call = newsService.getHeadlines("in", API_KEY)
        call.enqueue(object : Callback<NewsModel> {
            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                articles.value = ArticleState.Error(t)
            }

            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                articles.value = response.body()?.let { newsModel ->
                    ArticleState.ArticlesFromNetwork(newsModel.articles.map { ArticleVm(it) })
                } ?: ArticleState.Empty()
            }
        })
        return articles
    }
}

class CachedNewsSource : Source {
    private val articles = MutableLiveData<ArticleState>()
    private val cacheStorage: StorageProvider

    private val app = NewsApplication.INSTANCE

    init {
        cacheStorage = CachedStorageProvider(app.moshi, SharedPrefHelperImpl())
    }

    override fun getArticles(): LiveData<ArticleState> {
        return articles.apply {
            value = ArticlesFromCache(cacheStorage.getArticles().map { ArticleVm(it) })
        }
    }

}

