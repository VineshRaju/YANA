package app.vineshbuilds.news.home.repository

import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.util.LocalStorageHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface StorageProvider : KoinComponent {
    fun getArticles(): List<ArticleModel>
    fun saveArticles(articles: List<ArticleModel>)
}

class CachedStorageProvider(private val sharedPrefHelper: LocalStorageHelper) : StorageProvider {
    private val moshi: Moshi by inject()
    private val typeAdapter = moshi.adapter<List<ArticleModel>>(
        Types.newParameterizedType(List::class.java, ArticleModel::class.java)
    )

    override fun getArticles(): List<ArticleModel> = sharedPrefHelper.getString(CACHE)?.let {
        typeAdapter.fromJson(it)
    } ?: emptyList()

    override fun saveArticles(articles: List<ArticleModel>) =
        sharedPrefHelper.putString(CACHE, typeAdapter.toJson(articles))

    companion object {
        private const val CACHE = "CACHE"
    }
}
