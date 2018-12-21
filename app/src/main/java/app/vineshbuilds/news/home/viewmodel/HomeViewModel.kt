package app.vineshbuilds.news.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.vineshbuilds.news.home.repository.NewsProvider
import java.util.concurrent.atomic.AtomicBoolean

class HomeViewModel(private val newsProvider: NewsProvider) : ViewModel() {
    private val shouldRefresh = AtomicBoolean(false)
    fun refreshNews(filter: String? = null): LiveData<ViewState> {
        return newsProvider.getNews().also {
            if (shouldRefresh.getAndSet(true)) newsProvider.refreshNews()
        }
    }

    fun getAgencies() = newsProvider.getAvailableAgencies()
}
