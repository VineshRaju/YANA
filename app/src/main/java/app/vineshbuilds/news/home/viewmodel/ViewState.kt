package app.vineshbuilds.news.home.viewmodel

sealed class ViewState {
    class FromNetwork(val articles: List<ArticleVm>) : ViewState()
    class FromCache(val articles: List<ArticleVm>) : ViewState()
    class Error(val throwable: Throwable) : ViewState()
    object Empty : ViewState()
    object Loading : ViewState()
}
