package app.vineshbuilds.news.home.viewmodel

import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import java.text.SimpleDateFormat
import java.util.*

class ArticleVm(val article: ArticleModel) : ViewModel() {
    val headline = article.headline
    private val _publishedDate =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(article.publishedDate)
    val publishedDate = DateFormat.format("'AT 'hh:mm a' 'dd/MM/yyyy", _publishedDate)
    val urlToStory = article.urlToStory
    val thumbUrl = article.thumbUrl
    val story = article.story
}
