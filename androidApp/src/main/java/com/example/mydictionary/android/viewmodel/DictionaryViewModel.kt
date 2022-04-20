package com.example.mydictionary.android.viewmodel

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydictionary.FileReader

class DictionaryViewModel: ViewModel() {
    val fileReader : FileReader = FileReader.FileReaderBuilder.getInstance()

    var result = MutableLiveData<String>()
    var suggestionList = MutableLiveData<ArrayList<String>>()

    fun setupData(context: Context) {
        fileReader.createInputStream(context)
    }

    fun getSuggestion(text: String) {
        if (text.isNotEmpty()) {
           suggestionList.value = fileReader.querySuggestions(text.lowercase())
        }
    }

    fun searchKeyword(text: String?){
        if(text != null && text.isNotEmpty()){
            val value = fileReader.searchKeyword(text)
            result.value = value.substring(getStartIndex(value))
        }
    }

    private fun getStartIndex(value: String): Int {
        var startIndex = value.indexOf("/")
        if(startIndex < 0){
            startIndex = value.indexOf("*")
            if(startIndex < 0){
                startIndex = value.indexOf("-")
            }
        }
        return startIndex
    }

    fun closeKeyboard(activity: AppCompatActivity){
        val view = activity.currentFocus

        if (view != null) {
            val manager = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}