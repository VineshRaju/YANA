package app.vineshbuilds.news.home.viewmodel

sealed class NewsState {
    class ArticlesFromNetwork(val articles: List<ArticleVm>) : NewsState()
    class ArticlesFromCache(val articles: List<ArticleVm>) : NewsState()
    class Error(val throwable: Throwable) : NewsState()
    object Empty : NewsState()
    object Loading : NewsState()
}
