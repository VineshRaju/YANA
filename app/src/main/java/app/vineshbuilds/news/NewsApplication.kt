package app.vineshbuilds.news

import android.app.Application
import app.vineshbuilds.news.di.appModule
import org.koin.android.ext.android.startKoin

class NewsApplication : Application() {
    companion object {
        lateinit var INSTANCE: NewsApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin(this, listOf(appModule))
    }
}
