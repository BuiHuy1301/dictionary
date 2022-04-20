package com.example.mydictionary.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mydictionary.Util
import com.example.mydictionary.android.databinding.FragmentRecentSearchBinding
import com.example.mydictionary.android.viewmodel.FragmentViewModel
import com.example.mydictionary.preferences.KMMContext
import com.example.mydictionary.preferences.KMMStorage


class RecentSearchFragment : Fragment() {
    private lateinit var binding: FragmentRecentSearchBinding
    private lateinit var fragmentViewModel: FragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        fragmentViewModel.kmmStorage = KMMStorage(this.activity as MainActivity)
        fragmentViewModel.getList(Util.RECENT_LIST_STORAGE_KEY)
        registerObserver()
    }

    private fun registerObserver() {
        fragmentViewModel.recentUsedKeywordList.observe(this){
        }
    }

}