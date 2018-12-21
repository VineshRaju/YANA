package app.vineshbuilds.news.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.vineshbuilds.news.home.repository.NewsProvider
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class HomeViewModel : ViewModel(), KoinComponent {
    private val newsProvider: NewsProvider by inject()

    fun refreshNews(): LiveData<NewsState> = newsProvider.getNews()
}
