package app.vineshbuilds.news

import android.app.Application
import app.vineshbuilds.news.di.appModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree



class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
