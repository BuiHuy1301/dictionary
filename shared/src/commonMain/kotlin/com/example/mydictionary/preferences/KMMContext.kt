package com.example.mydictionary.preferences

expect class KMMContext

expect fun KMMContext.putString(key: String, value: String)

expect fun KMMContext.getString(key: String): String?

