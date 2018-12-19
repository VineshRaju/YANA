package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import app.vineshbuilds.news.home.viewmodel.ArticleState

interface NewsProvider {
    fun getNews(): LiveData<ArticleState>
    fun onCleared()
}
