package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Source {
    fun getArticles(): LiveData<ViewState>
}

class LiveSource(private val newsService: NewsService) : Source {
    private val newsState = MutableLiveData<ViewState>()

    override fun getArticles(): LiveData<ViewState> {
        val call = newsService.getHeadlines("in")
        call.enqueue(object : Callback<NewsModel> {
            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                newsState.value = Error(t)
            }

            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                newsState.value = response.body()?.let { newsModel ->
                    val articles = newsModel.articles.map { ArticleVm(it) }
                    if (articles.isNotEmpty()) Success.FromNetwork(articles) else Empty
                } ?: Empty
            }
        })
        return newsState
    }
}

class LocalSource(private val storageProvider: StorageProvider) : Source {
    private val newsState = MutableLiveData<ViewState>()

    override fun getArticles(): LiveData<ViewState> = newsState.apply {
        val articles = storageProvider.getArticles()
        value = if (articles.isNotEmpty())
            Success.FromCache(articles.map { ArticleVm(it) })
        else Empty
    }

    fun saveArticles(articles: List<ArticleVm>) = storageProvider.saveArticles(articles.map { it.article })
}

