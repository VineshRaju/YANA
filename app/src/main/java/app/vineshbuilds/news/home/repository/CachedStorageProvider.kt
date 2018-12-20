package app.vineshbuilds.news.home.repository

import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.util.SharedPrefHelper
import com.squareup.moshi.Types

class CachedStorageProvider : StorageProvider {
    companion object {
        private const val CACHE = "CACHE"
    }

    private val typeAdapter = NewsApplication.INSTANCE.moshi.adapter<List<ArticleModel>>(
        Types.newParameterizedType(List::class.java, ArticleModel::class.java)
    )

    override fun getArticles(): List<ArticleModel> = SharedPrefHelper().getString(CACHE)?.let {
        typeAdapter.fromJson(it)
    } ?: emptyList()


    override fun saveArticles(articles: List<ArticleModel>) =
        SharedPrefHelper().putString(CACHE, typeAdapter.toJson(articles))

}
