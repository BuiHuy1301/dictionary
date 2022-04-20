package com.example.mydictionary.preferences

actual class KMMContext

actual fun KMMContext.putString(
    key: String,
    value: String
) {
}

actual fun KMMContext.getString(key: String): String? {
    TODO("Not yet implemented")
}
