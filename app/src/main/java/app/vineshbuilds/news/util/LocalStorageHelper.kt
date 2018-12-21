package app.vineshbuilds.news.util

import android.content.Context
import app.vineshbuilds.news.NewsApplication
import app.vineshbuilds.news.constants.KEY_PREF_NAME

interface LocalStorageHelper {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
}

object LocalStorageHelperImpl : LocalStorageHelper {
    private val app = NewsApplication.INSTANCE

    private val sharedPrefs = app.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) = sharedPrefs.edit().putString(key, value).apply()
    override fun getString(key: String): String? = sharedPrefs.getString(key, null)
}
