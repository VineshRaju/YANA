package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.viewmodel.ArticleState
import app.vineshbuilds.news.util.SharedPrefHelperImpl

interface NewsProvider {
    fun getNews(): LiveData<ArticleState>
    fun onCleared()
}

class NewsProviderImpl : NewsProvider {
    private val cachedNewsSource: Source
    private val onlineSource: Source
    private val cacheStorage: StorageProvider
    private val news = MutableLiveData<ArticleState>()
    private val app = NewsApplication.INSTANCE

    init {
        onlineSource = OnlineNewsSource(app.retrofit.create(NewsService::class.java))
        cachedNewsSource = CachedNewsSource()
        cacheStorage =
                CachedStorageProvider(app.moshi, SharedPrefHelperImpl())
    }

    private val newsObserver = Observer<ArticleState> { state ->
        news.value = when (state) {
            is ArticleState.Error, is ArticleState.Empty, is ArticleState.ArticlesFromCache -> state
            is ArticleState.ArticlesFromNetwork -> state.also { fromNetwork ->
                cacheStorage.saveArticles(fromNetwork.articles.map { it.article })
            }
        }
    }

    override fun getNews(): LiveData<ArticleState> {
        cachedNewsSource.getArticles().observeForever(newsObserver)
        onlineSource.getArticles().observeForever(newsObserver)
        return news
    }

    override fun onCleared() {
        cachedNewsSource.getArticles().removeObserver(newsObserver)
        onlineSource.getArticles().removeObserver(newsObserver)
    }
}
