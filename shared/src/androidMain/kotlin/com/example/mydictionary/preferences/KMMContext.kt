package com.example.mydictionary.preferences

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.example.mydictionary.Util.SP_NAME


actual typealias KMMContext = Activity

actual fun KMMContext.putString(key: String, value: String) {
    val editor = this.getSharedPreferences(SP_NAME, MODE_PRIVATE).edit()
    editor.putString(key, value)
    editor.apply()
}

actual fun KMMContext.getString(key: String): String? {
    val preferences = this.getSharedPreferences(SP_NAME, MODE_PRIVATE)
    return preferences.getString(key, "hello")
}