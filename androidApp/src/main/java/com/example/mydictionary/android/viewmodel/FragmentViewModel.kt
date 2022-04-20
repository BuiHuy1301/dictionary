package com.example.mydictionary.android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydictionary.preferences.KMMStorage


class FragmentViewModel : ViewModel() {
    var kmmStorage: KMMStorage? = null
    var recentUsedKeywordList = MutableLiveData<ArrayList<String>>()

    fun getList(key: String) {
        recentUsedKeywordList.value = kmmStorage?.getList(key)
    }

    fun setRecentList(key: String, value: String) {
        kmmStorage?.setList(key, value)
    }
}