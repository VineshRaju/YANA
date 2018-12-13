package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.repository.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.util.SharedPrefHelper
import com.squareup.moshi.Types

class NewsProviderImpl : NewsProvider {
    private val cachedNewsSource: CachedNewsSource
    private val onlineSource: NewsSource
    private val app = NewsApplication.INSTANCE
    private val news = MutableLiveData<List<ArticleVm>?>()
    private val setNewsObserver = Observer<List<ArticleVm>?> {
        news.value = it
    }
    private val setNewsAndCacheObserver = Observer<List<ArticleVm>?> {
        news.value = it
        it?.let(this::cache)
    }

    init {
        onlineSource = NewsSource(app.retrofit.create(NewsService::class.java))
        cachedNewsSource = CachedNewsSource()
    }

    override fun getNews(): LiveData<List<ArticleVm>?> {
        cachedNewsSource.refreshArticles().observeForever(setNewsObserver)
        onlineSource.refreshArticles().observeForever(setNewsAndCacheObserver)
        return news
    }

    override fun onCleared() {
        cachedNewsSource.refreshArticles().removeObserver(setNewsObserver)
        onlineSource.refreshArticles().removeObserver(setNewsAndCacheObserver)
    }

    private fun cache(articleModels: List<ArticleVm>) {
        val articles = articleModels.map { it.article }
        val type = Types.newParameterizedType(List::class.java, ArticleModel::class.java)
        val adapter = app.moshi.adapter<List<ArticleModel>>(type)
        val json: String = adapter.toJson(articles)
        SharedPrefHelper().putString("CACHE", json)
    }
}
