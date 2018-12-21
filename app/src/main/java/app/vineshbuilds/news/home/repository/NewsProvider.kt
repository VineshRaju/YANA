package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.home.viewmodel.NewsState
import app.vineshbuilds.news.home.viewmodel.NewsState.ArticlesFromNetwork
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface NewsProvider : KoinComponent {
    fun getNews(): LiveData<NewsState>
}

class NewsProviderImpl : NewsProvider {
    private val cacheStorage: StorageProvider by inject()
    private val cachedNewsSource: Source = CachedNewsSource()
    private val onlineSource: Source = OnlineNewsSource()
    private val news = MediatorLiveData<NewsState>()

    init {
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
