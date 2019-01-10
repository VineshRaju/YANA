package app.vineshbuilds.news.di

import app.vineshbuilds.news.constants.BASE_URL
import app.vineshbuilds.news.home.repository.*
import app.vineshbuilds.news.home.service.NewsService
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import app.vineshbuilds.news.util.LocalStorageHelper
import app.vineshbuilds.news.util.LocalStorageHelperImpl
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    viewModel(override = true) { HomeViewModel(get()) }
    single<LocalStorageHelper> { LocalStorageHelperImpl(androidContext()) }
    single<StorageProvider> { PrefCacheProvider(get(), get()) }
    single<NewsProvider> { NewsProviderImpl(get(), get()) }
    single { LiveSource(get()) }
    single { LocalSource(get()) }
    single { Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(get())).build() }
    single { get<Retrofit>().create(NewsService::class.java) }
    single { Moshi.Builder().build() }
}
