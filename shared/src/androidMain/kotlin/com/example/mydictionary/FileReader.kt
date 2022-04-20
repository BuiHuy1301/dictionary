package com.example.mydictionary

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.example.mydictionary.Util.DETAIL_TRANSLATION_FILE_NAME
import com.example.mydictionary.Util.KEYWORD_FILE_NAME
import com.example.mydictionary.Util.SHORT_TRANSLATION_FILE_NAME
import java.io.IOException
import java.io.InputStream

actual class FileReader{
    var inputStream: AssetManager? = null
    var keywordList: ArrayList<String>? = null
    var detailTranslationList: ArrayList<String>? = null
    var shortTranslationList : ArrayList<String>? = null

    fun createInputStream(context: Context){
        inputStream = context.assets
        keywordList = queryFile(inputStream?.open(KEYWORD_FILE_NAME))
        detailTranslationList = queryFile(inputStream?.open(DETAIL_TRANSLATION_FILE_NAME))
        shortTranslationList = queryFile(inputStream?.open(SHORT_TRANSLATION_FILE_NAME))
    }

    fun querySuggestions(keyword: String): ArrayList<String>? {
        var suggestions: ArrayList<String>? = null
        try {
            keywordList?:let {
                keywordList = queryFile(inputStream?.open(KEYWORD_FILE_NAME))
            }
            suggestions = SuggestionsProvider().getSuggestions(keywordList!!, keyword)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return suggestions
    }

    fun queryFile(inputStream: InputStream?): ArrayList<String> {
        Log.d("huybv", "queryFile: ")
        val lineList = ArrayList<String>()
        inputStream?.bufferedReader()?.forEachLine { lineList.add(it) }
        Log.d("huybv", "queryFile: ${lineList.size}")
        return lineList
    }

    fun searchKeyword(keyword: String) : String {
        try {
            detailTranslationList ?: let {
                detailTranslationList = queryFile(inputStream?.open(DETAIL_TRANSLATION_FILE_NAME))
            }
            shortTranslationList ?: let {
                shortTranslationList = queryFile(inputStream?.open(SHORT_TRANSLATION_FILE_NAME))
            }
            return SuggestionsProvider().findTranslation(detailTranslationList!!, shortTranslationList!!, keyword)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    object FileReaderBuilder{
        var instances: FileReader? = null
        fun getInstance(): FileReader{
            if(instances == null){
                instances = FileReader()
            }
            return instances!!
        }
    }

}