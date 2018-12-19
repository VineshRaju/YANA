package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.repository.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.home.viewmodel.ArticleState
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.util.SharedPrefHelper
import com.squareup.moshi.Types

class NewsProviderImpl : NewsProvider {
    private val cachedNewsSource: CachedNewsSource
    private val onlineSource: NewsSource
    private val app = NewsApplication.INSTANCE
    private val news = MutableLiveData<ArticleState>()

    private val newsObserver = Observer<ArticleState> {
        news.value = when (it) {
            is ArticleState.Error, ArticleState.Empty -> it
            is ArticleState.ArticlesFromCache -> it
            is ArticleState.ArticlesFromNetwork -> it.also { fromNetwork -> cache(fromNetwork.articles) }
        }
    }

    init {
        onlineSource = NewsSource(app.retrofit.create(NewsService::class.java))
        cachedNewsSource = CachedNewsSource()
    }

    override fun getNews(): LiveData<ArticleState> {
        cachedNewsSource.refreshArticles().observeForever(newsObserver)
        onlineSource.refreshArticles().observeForever(newsObserver)
        return news
    }

    override fun onCleared() {
        cachedNewsSource.refreshArticles().removeObserver(newsObserver)
        onlineSource.refreshArticles().removeObserver(newsObserver)
    }

    private fun cache(articleModels: List<ArticleVm>) {
        val articles = articleModels.map { it.article }
        val type = Types.newParameterizedType(List::class.java, ArticleModel::class.java)
        val adapter = app.moshi.adapter<List<ArticleModel>>(type)
        val json: String = adapter.toJson(articles)
        SharedPrefHelper().putString("CACHE", json)
    }
}
