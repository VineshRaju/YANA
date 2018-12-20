package app.vineshbuilds.news.home.viewmodel

sealed class ArticleState {
    class ArticlesFromNetwork(val articles: List<ArticleVm>) : ArticleState()
    class ArticlesFromCache(val articles: List<ArticleVm>) : ArticleState()
    class Error(val throwable: Throwable) : ArticleState()
    class Empty() : ArticleState()
}
