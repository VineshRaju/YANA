package app.vineshbuilds.news.home.repository

import app.vineshbuilds.news.home.view.model.NewsModel

interface StorageProvider {
    fun getArticles(): List<NewsModel.ArticleModel>
    fun saveArticles(articles: List<NewsModel.ArticleModel>)
}
