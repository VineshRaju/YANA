package app.vineshbuilds.news

import android.app.Application
import app.vineshbuilds.news.di.AppComponent
import app.vineshbuilds.news.di.DaggerAppComponent
import app.vineshbuilds.news.di.StorageModule

class NewsApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().storageModule(StorageModule(this)).build()
    }
}
