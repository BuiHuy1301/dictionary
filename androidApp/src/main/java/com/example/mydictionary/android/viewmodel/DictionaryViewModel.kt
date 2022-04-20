package com.example.mydictionary.android.viewmodel

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydictionary.FileReader
import com.example.mydictionary.Util
import com.example.mydictionary.preferences.KMMStorage
import java.lang.StringBuilder

class DictionaryViewModel : ViewModel() {
    val fileReader: FileReader = FileReader.FileReaderBuilder.getInstance()
    lateinit var kmmStorage: KMMStorage
    var result = MutableLiveData<String>()
    var suggestionList = MutableLiveData<ArrayList<String>>()
    var keyword = String()
    var translation = String()

    fun setupData(context: Context) {
        fileReader.createInputStream(context)
    }

    fun getSuggestion(text: String) {
        if (text.isNotEmpty()) {
            suggestionList.value = fileReader.querySuggestions(text.lowercase())
        }
    }

    fun searchKeyword(text: String?) {
        if (text != null && text.isNotEmpty()) {
            val value = fileReader.searchKeyword(text)
            val trans = value.substring(getStartIndex(value))
            if(trans == "/") {
                keyword = ""
                result.value =  "Translation of \"$text\" is not available, please try again with another keyword"
            } else{
                keyword = text
                result.value = trans
            }

        }
    }

    private fun getStartIndex(value: String): Int {
        var startIndex = value.indexOf("/")
        if (startIndex < 0) {
            startIndex = value.indexOf("*")
            if (startIndex < 0) {
                startIndex = value.indexOf("-")
            }
        }
        return startIndex
    }

    fun closeKeyboard(activity: AppCompatActivity) {
        val view = activity.currentFocus
        if (view != null) {
            val manager = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun putStringToSP(newValue: String) {
        val list = kmmStorage.getList(Util.RECENT_LIST_STORAGE_KEY)
        if (list.size >= 20) {
            list.removeAt(19)
        }
        if(list.contains("")){
            list.remove("")
        }
        if(list.contains(newValue)){
            list.remove(newValue)
        }
        list.add(0, newValue)
        val sb = StringBuilder()
        list.forEachIndexed { index, value ->
//            Log.d("huybv", "putStringToSP: $index value = $value" )
            sb.append(value)
            if (index < list.size - 1) {
                sb.append("|")
            }
        }
        kmmStorage.setList(Util.RECENT_LIST_STORAGE_KEY, sb.toString())
    }
}