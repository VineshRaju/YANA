package app.vineshbuilds.news.util

import android.content.Context
import app.vineshbuilds.news.constants.KEY_PREF_NAME

interface LocalStorageHelper {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
}

class LocalStorageHelperImpl(context: Context) : LocalStorageHelper {

    private val sharedPrefs = context.applicationContext.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) = sharedPrefs.edit().putString(key, value).apply()
    override fun getString(key: String): String? = sharedPrefs.getString(key, null)
}
