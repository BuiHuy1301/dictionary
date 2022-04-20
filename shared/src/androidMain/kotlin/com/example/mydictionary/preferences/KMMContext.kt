package com.example.mydictionary.preferences

import android.app.Activity
import android.content.Context.MODE_PRIVATE

const val SP_NAME = "dictionary_app"

actual typealias KMMContext = Activity

actual fun KMMContext.putString(key: String, value: String) {
    val editor = this.getSharedPreferences(SP_NAME, MODE_PRIVATE).edit()
    editor.putString(key, value)
    editor.apply()
}

actual fun KMMContext.getString(key: String): String? {
    val preferences = this.getSharedPreferences(SP_NAME, MODE_PRIVATE)
    return preferences.getString(key, "")
}