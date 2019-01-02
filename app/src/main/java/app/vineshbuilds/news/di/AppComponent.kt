package app.vineshbuilds.news.di

import app.vineshbuilds.news.home.repository.NewsProvider
import app.vineshbuilds.news.home.repository.StorageProvider
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.view.HomeActivity
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import com.squareup.moshi.Moshi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, StorageModule::class])
interface AppComponent {
    fun inject(activity: HomeActivity)
    fun getNewsService(): NewsService
    fun getStorageProvider(): StorageProvider
    fun getNewsProvider(): NewsProvider
    fun getHomeViewModel(): HomeViewModel
    fun getMoshi(): Moshi
}
