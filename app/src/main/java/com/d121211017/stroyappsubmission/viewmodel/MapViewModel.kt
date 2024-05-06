package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.remote.entity.GetStories
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import com.d121211017.stroyappsubmission.getErrorResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(application: Application, pref: UserPreferences) : ViewModel() {
    private val userPref = pref
    private val applicationContext = application.applicationContext

    private val _storiesWithLocation = MutableLiveData<List<ListStoryItem>>()
    val storiesWithLocation : LiveData<List<ListStoryItem>> = _storiesWithLocation

    private val _errorResponse = MutableLiveData<Pair<Boolean, String>>()
    val errorResponse : LiveData<Pair<Boolean, String>> = _errorResponse

    init {
        getStoriesWithLocation()
    }

    private fun getStoriesWithLocation(){
        val token = runBlocking { userPref.getUserToken().first() }
        val client = ApiConfig.getApiService(token = token).getStoriesWithLocation(location = 1)

        client.enqueue(object : Callback<GetStories>{
            override fun onResponse(p0: Call<GetStories>, p1: Response<GetStories>) {
                val responseBody = p1.body()
                if(p1.isSuccessful && responseBody != null){
                    val listItem = p1.body()!!.listStory ?: ArrayList()
                    _storiesWithLocation.postValue(listItem)
                } else {
                    val errorResponse = getErrorResponse(p1.errorBody()!!.string())
                    _errorResponse.postValue(Pair(errorResponse.error, errorResponse.message!!))
                }
            }

            override fun onFailure(p0: Call<GetStories>, p1: Throwable) {
                _errorResponse.postValue(Pair(true, applicationContext.getString(R.string.unknown_error)))
            }
        })
    }
}