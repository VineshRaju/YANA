package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState.Success
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState.Success.FromNetwork

interface NewsProvider {
    fun getNews(): LiveData<ViewState>
    fun refreshNews()
}

class NewsProviderImpl(private val liveSource: LiveSource, private val localSource: LocalSource) : NewsProvider {
    private val news = MediatorLiveData<ViewState>()
    private val observer = Observer<ViewState> {
        when (it) {
            is Success -> {
                if (it is FromNetwork) localSource.saveArticles(it.articles)
            }
        }
        news.value = it
    }

    init {
        news.addSource(localSource.getArticles(), observer)
        news.addSource(liveSource.getArticles(), observer)
    }

    override fun getNews(): LiveData<ViewState> = news

    override fun refreshNews() {
        liveSource.getArticles()
    }
}
