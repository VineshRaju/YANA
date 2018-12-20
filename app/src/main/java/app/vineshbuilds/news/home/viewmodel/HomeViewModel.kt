package app.vineshbuilds.news.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.vineshbuilds.news.home.repository.NewsProvider
import app.vineshbuilds.news.home.repository.NewsProviderImpl

class HomeViewModel : ViewModel() {
    private val newsProvider: NewsProvider

    init {
        newsProvider = NewsProviderImpl()
    }

    fun getArticles(): LiveData<ArticleState> = newsProvider.getNews()

    override fun onCleared() {
        super.onCleared()
        newsProvider.onCleared()
    }
}
