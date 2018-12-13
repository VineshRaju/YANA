package app.vineshbuilds.news.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.vineshbuilds.news.home.repository.service.NewsService
import app.vineshbuilds.news.home.view.model.NewsModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsSource(private val newsService: NewsService) : Source {
    companion object {
        const val API_KEY = "c16da6826c8a4a588588e7fc1d2c0e24"
    }

    override fun refreshNews(): LiveData<List<ArticleVm>?> {
        val articles = MutableLiveData<List<ArticleVm>>()
        val call = newsService.getHeadlines("in", API_KEY)
        call.enqueue(object : Callback<NewsModel> {
            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                articles.value = null
            }

            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                articles.value = response.body()?.let { newsModel ->
                    newsModel.articles.map { ArticleVm(it) }
                }
            }
        })
        return articles
    }
}
