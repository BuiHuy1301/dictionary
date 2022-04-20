package com.example.mydictionary.android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydictionary.preferences.KMMStorage


class FragmentViewModel : ViewModel() {
    var kmmStorage: KMMStorage? = null
    var recentUsedKeywordList = MutableLiveData<List<String>>()

    fun getList(key: String) {
        recentUsedKeywordList.value = kmmStorage?.getList(key)?.split("|")
    }

    fun setRecentList(key: String, value: String) {
        kmmStorage?.setList(key, value)
    }
}