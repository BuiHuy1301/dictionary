package com.example.mydictionary.android

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mydictionary.android.databinding.TranslationFragmentBinding
import com.example.mydictionary.android.listener.DataUpdater
import com.example.mydictionary.android.viewmodel.FragmentViewModel

class TranslationFragment(val title: String,val detail: String) : Fragment() {
    private lateinit var viewModel: FragmentViewModel
    private lateinit var binding : TranslationFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        binding = TranslationFragmentBinding.inflate(inflater, container, false)
        binding.detail.movementMethod = ScrollingMovementMethod()
        (activity as MainActivity).setDataUpdater(object : DataUpdater {
            override fun updateData(title: String, description: String) {
                Log.d("huybv", "updateData: $title")
                binding.detail.text = description
                binding.keyword.text = title
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.detail.text = detail
        binding.keyword.text = title
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setDataUpdater(null)
    }
}