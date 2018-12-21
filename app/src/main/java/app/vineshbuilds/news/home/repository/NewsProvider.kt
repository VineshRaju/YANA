package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.viewmodel.NewsState
import app.vineshbuilds.news.home.viewmodel.NewsState.ArticlesFromNetwork
import app.vineshbuilds.news.util.SharedPrefHelperImpl

interface NewsProvider {
    fun getNews(): LiveData<NewsState>
}

class NewsProviderImpl : NewsProvider {
    private val cachedNewsSource: Source
    private val onlineSource: Source
    private val cacheStorage: StorageProvider
    private val news = MediatorLiveData<NewsState>()
    private val app = NewsApplication.INSTANCE
    init {
        onlineSource = OnlineNewsSource(app.retrofit.create(NewsService::class.java))
        cachedNewsSource = CachedNewsSource()
        cacheStorage = CachedStorageProvider(app.moshi, SharedPrefHelperImpl)
        news.addSource(cachedNewsSource.getArticles()) { news.value = it }
    }

    override fun getNews(): LiveData<NewsState> {
        onlineSource.getArticles().observeForever(newsObserver)
        return news
    }

    private val newsObserver = object : Observer<NewsState> {
        override fun onChanged(state: NewsState) {
            when (state) {
                is ArticlesFromNetwork -> cacheStorage.saveArticles(state.articles.map { it.article }).also {
                    news.value = state
                }
                else -> news.value = state
            }
            onlineSource.getArticles().removeObserver(this)
        }
    }
}
