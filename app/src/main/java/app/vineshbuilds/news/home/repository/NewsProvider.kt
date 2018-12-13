package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import app.vineshbuilds.news.home.viewmodel.ArticleVm

interface NewsProvider {
    fun getNews(): LiveData<List<ArticleVm>?>
    fun onCleared()
}
