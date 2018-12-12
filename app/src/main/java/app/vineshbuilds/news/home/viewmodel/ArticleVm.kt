package app.vineshbuilds.news.home.viewmodel

import android.arch.lifecycle.ViewModel
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import java.text.SimpleDateFormat
import java.util.*

class ArticleVm(article: ArticleModel) : ViewModel() {
    val headline = article.headline
    val publishedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        .parse(article.publishedDate)
    val urlToStory = article.urlToStory
    val thumbUrl = article.thumbUrl
    val story = article.story
}
