package app.vineshbuilds.news

import app.vineshbuilds.news.home.repository.CachedStorageProvider
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.util.SharedPrefHelper
import com.squareup.moshi.Moshi
import org.amshove.kluent.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StroageProviderTests {
    private val testJson =
        "[{\"source\":{\"id\":10,\"name\":\"Nypost.com\"},\"author\":\"Ben Feuerherd\",\"title\":\"GoFundMe for border wall raises more than \$2M in just 3 days - New York Post \",\"description\":\"A supporter of President Trump’s border wall proposal has started a GoFundMe to help pay for the project — and the fund skyrocketed to more than \$2 million on Wednesday night after bein…\",\"url\":\"https://nypost.com/2018/12/20/gofundme-for-border-wall-raises-more-than-1m-in-just-3-days/\",\"urlToImage\":\"https://thenypost.files.wordpress.com/2018/12/trump-wall.jpg?quality=90&strip=all&w=1200\",\"publishedAt\":\"2018-12-20T05:28:00Z\",\"content\":\"A supporter of President Trump’s border wall proposal has started a GoFundMe to help pay for the project — and the fund skyrocketed to more than \$2 million on Wednesday night after being live for just three days. Brian Kolfage, a Trump voter and disabled Iraq… [+1201 chars]\"}]"

    private val testArticle = ArticleModel(
        "Headline",
        "Today",
        "https://example.com/story",
        "https://example.com/image",
        "story"
    )
    private val listOfArticles = listOf(testArticle, testArticle)

    @Mock
    lateinit var sharedPrefHelper: SharedPrefHelper

    @Test
    fun cachedStorageProvider_getArticles_readsJsonAndConvertsItToListOfArticleModels() {
        Mockito.`when`(sharedPrefHelper.getString(Mockito.anyString())).thenReturn(testJson)

        val moshi = Moshi.Builder().build()
        val storageProvider = CachedStorageProvider(moshi, sharedPrefHelper)
        val articles = storageProvider.getArticles()
        articles.size shouldBe 1
    }

    @Test
    fun cachedStorageProvider_putArticles_putsStringToSharedPref() {
        val moshi = Moshi.Builder().build()
        val storageProvider = CachedStorageProvider(moshi, sharedPrefHelper)
        storageProvider.saveArticles(listOfArticles)

        Mockito.verify(sharedPrefHelper).putString(Mockito.anyString(), Mockito.anyString())
    }
}
