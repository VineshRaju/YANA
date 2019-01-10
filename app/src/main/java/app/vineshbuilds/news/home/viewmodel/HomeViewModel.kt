package app.vineshbuilds.news.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.vineshbuilds.news.home.repository.NewsProvider
import java.util.concurrent.atomic.AtomicBoolean

class HomeViewModel(private val newsProvider: NewsProvider) : ViewModel() {
    val filters = mutableListOf<FilterVm>()
    private val shouldRefresh = AtomicBoolean(false)

    fun refreshNews(): LiveData<ViewState> = newsProvider.getNews().also {
        if (shouldRefresh.getAndSet(true)) newsProvider.refreshNews()
    }

    fun addNewAgenciesIfAny(filters: List<String>) {
        this.filters.apply {
            val existingAgencies = this.map { existingFilters -> existingFilters.filterName }
            val newAgencies = filters.filterNot { existingAgencies.contains(it) }.map { FilterVm(it) }
            addAll(newAgencies)
        }
    }

    fun filterIfNecessary(articles: List<ArticleVm>): List<ArticleVm> {
        val activeFilters = filters.filter { it.isSelected }.map { it.filterName }
        return articles.filter { activeFilters.contains(it.agency) }
    }

    sealed class ViewState {
        object Empty : ViewState()
        object Loading : ViewState()
        class Error(val throwable: Throwable) : ViewState()
        sealed class Success(val articles: List<ArticleVm>) : ViewState() {
            class FromNetwork(articles: List<ArticleVm>) : Success(articles)
            class FromCache(articles: List<ArticleVm>) : Success(articles)

            fun getNewsAgencies() = articles.map { it.agency }.distinct()
        }
    }
}
