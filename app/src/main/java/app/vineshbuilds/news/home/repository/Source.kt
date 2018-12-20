package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import app.vineshbuilds.news.home.viewmodel.ArticleState

interface Source {
    fun getArticles(): LiveData<ArticleState>
}
