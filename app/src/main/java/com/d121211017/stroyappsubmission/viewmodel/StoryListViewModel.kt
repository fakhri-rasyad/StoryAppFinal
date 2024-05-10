package com.d121211017.stroyappsubmission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.d121211017.stroyappsubmission.data.repository.StoryAppRepository
import kotlinx.coroutines.launch

class StoryListViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {
    private val _isLoadingStoriesData = MutableLiveData<Boolean>()
    val isLoadingStories : LiveData<Boolean> = _isLoadingStoriesData

    private val _onResponseError = MutableLiveData<Pair<Boolean, String>>()
    val onResponse : LiveData<Pair<Boolean, String>> = _onResponseError

    val storiesForPaging  = storyAppRepository.myStoriesForPaging().cachedIn(viewModelScope)

    fun clearUserSession(){
        viewModelScope.launch {
            storyAppRepository.clearUserSession()
        }
    }

}