package com.d121211017.stroyappsubmission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.paging.StoryPagingSource
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.data.repository.StoryAppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StoryListViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {
    private val _isLoadingStoriesData = MutableLiveData<Boolean>()
    val isLoadingStories : LiveData<Boolean> = _isLoadingStoriesData

    private val _onResponseError = MutableLiveData<Pair<Boolean, String>>()
    val onResponse : LiveData<Pair<Boolean, String>> = _onResponseError

    val storiesForPaging  = myStoriesForPaging().cachedIn(viewModelScope)

    fun myStoriesForPaging() : LiveData<PagingData<ListStoryItem>>{
        val token = runBlocking { pref.getUserToken().first() }
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { StoryPagingSource(token = token) }
        ).liveData
    }

    fun clearUserSession(){
        viewModelScope.launch {
            storyAppRepository.clearUserSession()
        }
    }

}