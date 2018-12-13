package app.vineshbuilds.news.home.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.util.SharedPrefHelper
import com.squareup.moshi.Types

class CachedNewsSource : Source {
    private val app = NewsApplication.INSTANCE
    private val articles = MutableLiveData<List<ArticleVm>?>()
    override fun refreshArticles(): LiveData<List<ArticleVm>?> {
        val cachedJson = SharedPrefHelper().getString("CACHE")
        return articles.apply {
            value = cachedJson?.let {
                val type = Types.newParameterizedType(List::class.java, ArticleModel::class.java)
                val adapter = app.moshi.adapter<List<ArticleModel>>(type)
                adapter.fromJson(it)!!.map { articleModel -> ArticleVm(articleModel) }
            }
        }
    }
}
