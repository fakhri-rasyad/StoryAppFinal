package com.d121211017.stroyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.data.paging.LoadingStateAdapter
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.databinding.ActivityStoryListBinding
import com.d121211017.stroyappsubmission.makeToast
import com.d121211017.stroyappsubmission.ui.adapter.RecyclerViewAdapter
import com.d121211017.stroyappsubmission.viewmodel.StoryListViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryListBinding
    private lateinit var viewModel : StoryListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.datastore)
        viewModel = getViewModel(this, pref)

        val layoutManager = LinearLayoutManager(this)
        val layoutDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.apply {
            storyRv.apply {
                this.layoutManager = layoutManager
                this.addItemDecoration(layoutDecoration)
            }
            addStoryButton.setOnClickListener {
                val intent = Intent(this@StoryListActivity, AddStoryActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.apply {
            isLoadingStories.observe(this@StoryListActivity){
                binding.pgBar.visibility = if(it) View.VISIBLE else View.INVISIBLE
            }
            storiesForPaging.observe(this@StoryListActivity){
                setRecyclerView(it)
            }
            onResponse.observe(this@StoryListActivity){
                makeToast(this@StoryListActivity, it.second)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.action_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                viewModel.clearUserSession()
                finish()
            }
            R.id.action_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        if(item.itemId == R.id.action_logout){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            viewModel.clearUserSession()
            finish()
        }

        return true
    }

    private fun setRecyclerView(pagingData: PagingData<ListStoryItem>) {
        val adapter = RecyclerViewAdapter()
        binding.storyRv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.submitData(lifecycle, pagingData)
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity, pref: UserPreferences) : StoryListViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application, pref)
        return ViewModelProvider(appCompatActivity, factory)[StoryListViewModel::class.java]
    }
}