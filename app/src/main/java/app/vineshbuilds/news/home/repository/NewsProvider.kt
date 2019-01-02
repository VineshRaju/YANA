package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.viewmodel.ViewState
import app.vineshbuilds.news.home.viewmodel.ViewState.FromNetwork

interface NewsProvider {
    fun getNews(): LiveData<ViewState>
    fun refreshNews()
    fun getAvailableAgencies(): LiveData<Set<String>>
}

class NewsProviderImpl(newsService: NewsService, storageProvider: StorageProvider) : NewsProvider {
    private val cachedNewsSource: Source = CachedNewsSource(storageProvider)
    private val onlineSource: Source = OnlineNewsSource(newsService)
    private val news = MediatorLiveData<ViewState>()
    private val agencies = MutableLiveData<Set<String>>()
    private val observer = Observer<ViewState> { viewState ->
        when (viewState) {
            is FromNetwork -> {
                storageProvider.saveArticles(viewState.articles.map { it.article })
                agencies.value = determineAvailableAgencies(viewState)
            }
        }
        news.value = viewState
    }

    init {
        news.addSource(cachedNewsSource.getArticles(), observer)
        news.addSource(onlineSource.getArticles(), observer)
    }

    private fun determineAvailableAgencies(viewState: FromNetwork): Set<String> =
        viewState.articles.fold(HashSet()) { set, articleVm ->
            set.apply { add(articleVm.agency) }
        }

    override fun getNews(): LiveData<ViewState> = news

    override fun refreshNews() {
        onlineSource.getArticles()
    }

    override fun getAvailableAgencies(): LiveData<Set<String>> = agencies
}
