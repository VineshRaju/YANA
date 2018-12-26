package app.vineshbuilds.news.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.vineshbuilds.news.home.repository.NewsProvider

class HomeViewModel(private val newsProvider: NewsProvider) : ViewModel() {
    private var news: LiveData<ViewState>? = null

    fun refreshNews(): LiveData<ViewState> {
        if (news == null)
            news = newsProvider.getNews()
        else
            newsProvider.refreshNews()
        return news!!
    }
}
