package app.vineshbuilds.news

import app.vineshbuilds.news.di.appModule
import app.vineshbuilds.news.home.repository.CachedStorageProvider
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.util.LocalStorageHelper
import com.squareup.moshi.Moshi
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StorageProviderTest : KoinTest {
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
    lateinit var localStorageHelper: LocalStorageHelper

    private val moshi: Moshi by inject()

    @Before
    fun setup() {
        startKoin(listOf(appModule))
    }

    @After
    fun cleanUp() {
        stopKoin()
    }

    @Test
    fun cachedStorageProvider_getArticles_readsJsonAndConvertsItToListOfArticleModels() {
        Mockito.`when`(localStorageHelper.getString(Mockito.anyString())).thenReturn(testJson)

        val storageProvider = CachedStorageProvider(moshi, localStorageHelper)
        val articles = storageProvider.getArticles()
        articles.size shouldBe 1

    }

    @Test
    fun cachedStorageProvider_putArticles_putsStringToSharedPref() {
        val storageProvider = CachedStorageProvider(moshi, localStorageHelper)
        storageProvider.saveArticles(listOfArticles)

        Mockito.verify(localStorageHelper).putString(Mockito.anyString(), Mockito.anyString())
    }
}
