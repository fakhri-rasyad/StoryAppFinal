package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.remote.entity.GetStories
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import com.d121211017.stroyappsubmission.getErrorResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryListViewModel(application: Application, pref : UserPreferences) : ViewModel() {
    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList : LiveData<List<ListStoryItem>> = _storyList

    private val _isLoadingStoriesData = MutableLiveData<Boolean>()
    val isLoadingStories : LiveData<Boolean> = _isLoadingStoriesData

    private val _onResponseError = MutableLiveData<Pair<Boolean, String>>()
    val onResponse : LiveData<Pair<Boolean, String>> = _onResponseError

    private val userPreferences = pref
    private val applicationContext : Context = application.applicationContext

    init {
        getStoryList()
    }

    fun getStoryList(){
        val token = runBlocking { userPreferences.getUserToken().first() }
        val client = ApiConfig.getApiService(token = token).getStories()

        _isLoadingStoriesData.postValue(true)

        client.enqueue(object : Callback<GetStories> {
            override fun onResponse(p0: Call<GetStories>, p1: Response<GetStories>) {
                _isLoadingStoriesData.postValue(false)
                val responseBody = p1.body()
                if (p1.isSuccessful && responseBody != null){
                    val listStory = p1.body()!!.listStory ?: ArrayList()
                    _storyList.postValue(listStory)
                } else {
                    val errorResponse = getErrorResponse(p1.errorBody()!!.string())
                    _onResponseError.postValue(Pair(errorResponse.error, errorResponse.message!!))
                }
            }
            override fun onFailure(p0: Call<GetStories>, p1: Throwable) {
                _onResponseError.postValue(Pair(false, applicationContext.getString(R.string.unknown_error)))
            }
        })
    }

    fun clearUserSession(){
        viewModelScope.launch {
            userPreferences.clearUserSession()
        }
    }

}