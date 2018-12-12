package app.vineshbuilds.news.util

import android.content.Context
import app.vineshbuilds.news.NewsApplication

class SharedPrefHelper {
    private val app = NewsApplication.INSTANCE
    private val sharedPrefs = app.getSharedPreferences("NewsPrefs", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPrefs.getString(key, null)
    }
}
