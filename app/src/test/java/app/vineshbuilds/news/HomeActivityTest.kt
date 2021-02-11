package app.vineshbuilds.news

import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeActivityTest {
    @Mock
    lateinit var homeViewModel: HomeViewModel

    private val mockModule = module {
        viewModel(override = true) { homeViewModel }
    }

    private val testArticle = ArticleModel(
        ArticleModel.Agency("", "example"),
        "Today",
        "https://example.com/story",
        "https://example.com/image",
        "story"
    )
    private val listOfArticles = listOf(testArticle, testArticle)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(listOf(mockModule))
    }

    @Test
    fun test_stuff() {

    }

    @After
    fun cleanUp() {
        stopKoin()
    }
}
