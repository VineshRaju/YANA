package app.vineshbuilds.news.home.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.home.viewmodel.ArticleState
import app.vineshbuilds.news.home.viewmodel.ArticleState.ArticlesFromCache
import app.vineshbuilds.news.home.viewmodel.ArticleVm

class CachedNewsSource : Source {
    private val articles = MutableLiveData<ArticleState>()
    private val cacheStorage: StorageProvider

    init {
        cacheStorage = CachedStorageProvider()
    }

    override fun getArticles(): LiveData<ArticleState> {
        return articles.apply {
            value = ArticlesFromCache(cacheStorage.getArticles().map { ArticleVm(it) })
        }
    }
}
