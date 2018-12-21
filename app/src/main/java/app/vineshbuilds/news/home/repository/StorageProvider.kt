package app.vineshbuilds.news.home.repository

import app.vineshbuilds.news.constants.KEY_CACHE
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.util.LocalStorageHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

interface StorageProvider {
    fun getArticles(): List<ArticleModel>
    fun saveArticles(articles: List<ArticleModel>)
}

class CachedStorageProvider(moshi: Moshi, private val sharedPrefHelper: LocalStorageHelper) :
    StorageProvider {
    private val typeAdapter = moshi.adapter<List<ArticleModel>>(
        Types.newParameterizedType(List::class.java, ArticleModel::class.java)
    )

    override fun getArticles(): List<ArticleModel> = sharedPrefHelper.getString(KEY_CACHE)?.let {
        typeAdapter.fromJson(it)
    } ?: emptyList()

    override fun saveArticles(articles: List<ArticleModel>) =
        sharedPrefHelper.putString(KEY_CACHE, typeAdapter.toJson(articles))
}
