package com.d121211017.stroyappsubmission.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.paging.StoryPagingSource
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.data.remote.entity.LoginResult
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAppRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {


    fun getStoriesWithLocation() =
        apiService.getStoriesWithLocation()

    fun registerAccount(userName: String, userEmail : String, userPassword: String) =
        apiService.registerAccount(userName, userEmail, userPassword)


    fun loginAccount(userEmail: String, userPassword: String) =
        apiService.loginAccount(userEmail, userPassword)


    fun addUserStory(description: RequestBody, file: MultipartBody.Part) =
        apiService.addStory(description, file)


    //Datastore management
    suspend fun getUserToken(): String =
        userPreferences.getUserToken().first()


    suspend fun saveUserSession(userData: LoginResult){
        userPreferences.saveUserData(userData)
    }

    suspend fun clearUserSession(){
        userPreferences.clearUserSession()
    }

    //Paging

    fun myStoriesForPaging() : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { StoryPagingSource(apiService = apiService) }
        ).liveData
    }

    companion object {
        @Volatile
        private var INSTANCE : StoryAppRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ) : StoryAppRepository {
            return INSTANCE ?: synchronized(this){
                val instance = StoryAppRepository(apiService, userPreferences)
                INSTANCE = instance
                instance

            }
        }
    }
}
