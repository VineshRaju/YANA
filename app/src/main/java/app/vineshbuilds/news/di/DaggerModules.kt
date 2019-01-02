package app.vineshbuilds.news.di

import android.content.Context
import app.vineshbuilds.news.constants.BASE_URL
import app.vineshbuilds.news.home.repository.CachedStorageProvider
import app.vineshbuilds.news.home.repository.NewsProvider
import app.vineshbuilds.news.home.repository.NewsProviderImpl
import app.vineshbuilds.news.home.repository.StorageProvider
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import app.vineshbuilds.news.util.LocalStorageHelper
import app.vineshbuilds.news.util.LocalStorageHelperImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class AppModule {
    @Provides
    fun provideNewsProvider(newsService: NewsService, storageProvider: StorageProvider): NewsProvider =
        NewsProviderImpl(newsService, storageProvider)

    @Provides
    fun provideHomeViewModel(newsProvider: NewsProvider) = HomeViewModel(newsProvider)
}

@Module
class NetworkModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(moshi)).build()

    @Provides
    fun provideNewsService(retrofit: Retrofit): NewsService = retrofit.create(NewsService::class.java)
}

@Module
class StorageModule(private val context: Context) {
    @Provides
    fun getAppContext(): Context = context.applicationContext

    @Provides
    fun provideLocalStorageHelper(context: Context): LocalStorageHelper = LocalStorageHelperImpl(context)

    @Provides
    fun provideStorageProvider(moshi: Moshi, storageHelper: LocalStorageHelper): StorageProvider =
        CachedStorageProvider(moshi, storageHelper)
}
