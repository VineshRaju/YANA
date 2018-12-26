package app.vineshbuilds.news.di

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
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    //Moshi
    single { Moshi.Builder().build() }
    //Retrofit
    single { Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(get())).build() }
    //LocalStorageHelper
    single { LocalStorageHelperImpl as LocalStorageHelper }
    //NewsService
    single { get<Retrofit>().create(NewsService::class.java) }
    //StorageProvider
    single { CachedStorageProvider(get(), get()) as StorageProvider }
    //NewsProvider
    single { NewsProviderImpl(get()) as NewsProvider }

    viewModel { HomeViewModel(get()) }
}
