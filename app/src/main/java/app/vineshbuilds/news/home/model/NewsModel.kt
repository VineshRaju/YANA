package app.vineshbuilds.news.home.view.model

import com.squareup.moshi.Json

data class NewsModel(
    @field:Json(name = "articles")
    val articles: List<ArticleModel>
) {
    data class ArticleModel(
        @field:Json(name = "source")
        val agency: Agency,
        @field:Json(name = "title")
        val headline: String,
        @field:Json(name = "publishedAt")
        val publishedDate: String,
        @field:Json(name = "url")
        val urlToStory: String,
        @field:Json(name = "urlToImage")
        val thumbUrl: String
    ) {
        data class Agency(
            @field:Json(name = "id")
            val id: String,
            @field:Json(name = "name")
            val name: String
        )
    }
}
