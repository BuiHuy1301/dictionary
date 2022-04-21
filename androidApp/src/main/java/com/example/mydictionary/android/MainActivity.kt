package com.example.mydictionary.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydictionary.Util
import com.example.mydictionary.android.listener.ItemClickListener
import com.example.mydictionary.android.adapter.SuggestionAdapter
import com.example.mydictionary.android.databinding.ActivityMainBinding
import com.example.mydictionary.android.listener.DataUpdater
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
        binding.suggestionRv.layoutManager = LinearLayoutManager(this)
        registerListener()
        registerLiveDataObserver()
    }

    private fun initData() {
        time = System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.setupData(applicationContext)
            viewModel.kmmStorage = KMMStorage(this@MainActivity)
            viewModel.checkSPExist(this@MainActivity)
        }
        val list = ArrayList<String>()
        adapter = SuggestionAdapter(list)
        adapter.setItemClickListener(object : ItemClickListener {
            override fun onClickItem(string: String?) {
                searchKeyWord(string)
            }

            override fun onClickFillButton(string: String?) {
                fillTextToSearchBar(string)
            }
        })
        binding.suggestionRv.adapter = adapter
        changeFragment(RecentSearchFragment(), "")
    }

    fun fillTextToSearchBar(string: String?) {
        if (string != null) {
            binding.searchBar.setText(string)
            binding.searchBar.setSelection(string.length)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.suggestionRv.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun registerLiveDataObserver() {
        viewModel.result.observe(this) {
            viewModel.closeKeyboard(this)

            if (isFragmentAlive() && mDataUpdater != null) {
                mDataUpdater!!.updateData(viewModel.keyword, it)
            } else {
                changeFragment(TranslationFragment(viewModel.keyword, it), Util.FRAGMENT_TAG)
            }
        }

        viewModel.suggestionList.observe(this) {
            adapter.keywordList = it
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                binding.suggestionRv.visibility = View.GONE
            } else {
                binding.suggestionRv.visibility = View.VISIBLE
            }
        }
    }


    private fun changeFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, tag)
        transaction.commit()
    }

    fun searchKeyWord(keyword: String?) {
        binding.searchBar.setText(keyword)
        viewModel.searchKeyword(keyword)
        binding.suggestionRv.visibility = View.GONE
        if (keyword != null) {
            viewModel.putStringToSP(keyword)
        }
    }

    private fun registerListener() {
        binding.searchButton.setOnClickListener {
            time = System.currentTimeMillis()
            searchKeyWord(binding.searchBar.text.toString())
        }

        binding.searchBar.setOnClickListener {
            if (binding.searchBar.text.isNotEmpty()
                && viewModel.suggestionList.value != null
                && viewModel.suggestionList.value!!.isNotEmpty()
            ) {
                binding.suggestionRv.visibility = View.VISIBLE
            }
        }

        binding.mainView.setOnClickListener {
            if (binding.suggestionRv.visibility == View.VISIBLE) {
                binding.suggestionRv.visibility = View.GONE
            }
            viewModel.closeKeyboard(this)
        }
        binding.searchBar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (binding.searchBar.text.isEmpty()){
                    binding.suggestionRv.visibility = View.GONE
                }
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
                viewModel.closeKeyboard(this)
            }
            false
        }

        binding.suggestionRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        when {

            binding.suggestionRv.visibility == View.VISIBLE -> {
                binding.suggestionRv.visibility = View.GONE
            }

            isFragmentAlive() -> {
                changeFragment(RecentSearchFragment(), "")
            }

            else -> {
                finish()
            }
        }
    }

    fun setDataUpdater(dataUpdater: DataUpdater?) {
        mDataUpdater = dataUpdater
    }
}