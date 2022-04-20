package com.example.mydictionary.android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydictionary.Util
import com.example.mydictionary.android.adapter.ItemClickListener
import com.example.mydictionary.android.adapter.SuggestionAdapter
import com.example.mydictionary.android.databinding.ActivityMainBinding
import com.example.mydictionary.android.viewmodel.DataUpdater
import com.example.mydictionary.android.viewmodel.DictionaryViewModel
import com.example.mydictionary.preferences.KMMStorage
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: DictionaryViewModel
    lateinit var binding: ActivityMainBinding
    var time = 0L
    var mDataUpdater: DataUpdater? = null
    lateinit var adapter: SuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[DictionaryViewModel::class.java]
        initData()
        setContentView(binding.root)
        time = System.currentTimeMillis()
        binding.suggestionList.layoutManager = LinearLayoutManager(this)
        binding.suggestionList.visibility = View.GONE
        registerListener()
        registerLiveDataObserver()
    }

    private fun initData() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.setupData(applicationContext)
        }
        val recentSearchFragment = RecentSearchFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, recentSearchFragment, "")
        transaction.commit()
        val list = ArrayList<String>()
        adapter = SuggestionAdapter(list)
        adapter.setItemClickListener(object : ItemClickListener {
            override fun onClickItem(string: String?) {
                binding.searchBar.setText(string)
                searchKeyWord(string)
            }

            override fun onClickFillButton(string: String?) {
                if (string != null) {
                    binding.searchBar.setText(string)
                    binding.searchBar.setSelection(string.length)
                }
            }

        })
        binding.suggestionList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Log.d("huybv", "onResume: ${System.currentTimeMillis() - time}")
    }

    private fun registerLiveDataObserver() {
        viewModel.result.observe(this) {
            Log.d("huybv", "registerLiveDataObserver: ${System.currentTimeMillis() - time}")
            val title: String
            val description: String
            viewModel.closeKeyboard(this)
            if (it == "/") {
                title = ""
                description =
                    "Translation of \"${binding.searchBar.text}\" is not available, please try again with another keyword"
            } else {
                title = binding.searchBar.text.toString()
                description = it
            }

            if (isFragmentAlive() && mDataUpdater != null) {
                mDataUpdater!!.updateData(title, description)
            } else {
                val translationFragment = TranslationFragment(title, description)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, translationFragment, Util.FRAGMENT_TAG)
                transaction.commit()
            }
        }

        viewModel.suggestionList.observe(this) {
            adapter.suggestionList = it
            adapter.notifyDataSetChanged()
            binding.suggestionList.visibility = View.VISIBLE
        }
    }

    fun searchKeyWord(keyword: String?) {
        viewModel.searchKeyword(keyword)
        binding.suggestionList.visibility = View.GONE
    }

    private fun registerListener() {
        binding.searchButton.setOnClickListener {
            time = System.currentTimeMillis()
            searchKeyWord(binding.searchBar.text.toString())
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                viewModel.getSuggestion(s.toString())
            }
        })

        binding.searchBar.setOnKeyListener { _, p1, _ ->
            if (p1 == KeyEvent.KEYCODE_ENTER) {
                viewModel.searchKeyword(binding.searchBar.text.toString())
                viewModel.closeKeyboard(this@MainActivity)
            }
            false
        }

        binding.suggestionList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                viewModel.closeKeyboard(this@MainActivity)
            }
        })
    }

    fun isFragmentAlive(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(Util.FRAGMENT_TAG)
        return fragment != null && fragment.isVisible
    }

    override fun onBackPressed() {
        if (binding.suggestionList.visibility == View.VISIBLE) {
            binding.suggestionList.visibility = View.GONE
        } else {
            finish()
        }
    }

    fun setDataUpdater(dataUpdater: DataUpdater?) {
        mDataUpdater = dataUpdater
    }
}
