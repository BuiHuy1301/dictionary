package com.example.mydictionary.preferences

class KMMStorage(val context: KMMContext) {
    fun getList(key: String): ArrayList<String>{
        return ArrayList(context.getString(key)?.split("|")!!)
    }

    fun setList(key: String, value: String){
        context.putString(key, value)
    }
}