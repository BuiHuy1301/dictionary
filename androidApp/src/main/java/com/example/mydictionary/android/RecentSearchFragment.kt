package com.example.mydictionary.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydictionary.Util
import com.example.mydictionary.android.listener.ItemFragmentClickListener
import com.example.mydictionary.android.adapter.RecentSearchAdapter
import com.example.mydictionary.android.databinding.FragmentRecentSearchBinding
import com.example.mydictionary.android.viewmodel.FragmentViewModel
import com.example.mydictionary.preferences.KMMStorage


class RecentSearchFragment : Fragment() {
    private lateinit var binding: FragmentRecentSearchBinding
    private lateinit var fragmentViewModel: FragmentViewModel
    private lateinit var adapter: RecentSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentSearchBinding.inflate(inflater, container, false)
        initValue()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        registerObserver()
    }

    private fun initValue() {
        adapter = RecentSearchAdapter(ArrayList())
        adapter.setItemClickListener(object : ItemFragmentClickListener {
            override fun onClickItem(string: String?) {
                (activity as MainActivity).searchKeyWord(string)
            }

            override fun onClickFillButton(string: String?) {
                (activity as MainActivity).fillTextToSearchBar(string)
            }

        })
        binding.recentSearchList.adapter = adapter
        binding.recentSearchList.layoutManager = LinearLayoutManager(activity)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun registerObserver() {
        fragmentViewModel.recentUsedKeywordList.observe(this){

            adapter.list = it
            adapter.notifyDataSetChanged()
            Log.d("huybv", "registerObserver: ${adapter.list.size}")
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.kmmStorage = KMMStorage(this.activity as MainActivity)
        fragmentViewModel.getList(Util.RECENT_LIST_STORAGE_KEY)
    }

}