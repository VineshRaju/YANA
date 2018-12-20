package app.vineshbuilds.news.home.repository

import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.util.SharedPrefHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

interface StorageProvider {
    fun getArticles(): List<ArticleModel>
    fun saveArticles(articles: List<ArticleModel>)
}

class CachedStorageProvider(moshi: Moshi, private val sharedPrefHelper: SharedPrefHelper) :
    StorageProvider {

    private val CACHE = "CACHE"

    private val typeAdapter = moshi.adapter<List<ArticleModel>>(
        Types.newParameterizedType(List::class.java, ArticleModel::class.java)
    )

    override fun getArticles(): List<ArticleModel> = sharedPrefHelper.getString(CACHE)?.let {
        typeAdapter.fromJson(it)
    } ?: emptyList()

    override fun saveArticles(articles: List<ArticleModel>) =
        sharedPrefHelper.putString(CACHE, typeAdapter.toJson(articles))
}
