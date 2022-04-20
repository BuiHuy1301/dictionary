package com.example.mydictionary.preferences

class KMMStorage(val context: KMMContext) {
    fun getList(key: String): String?{
        return context.getString(key)
    }

    fun setList(key: String, value: String){
        context.putString(key, value)
    }
}