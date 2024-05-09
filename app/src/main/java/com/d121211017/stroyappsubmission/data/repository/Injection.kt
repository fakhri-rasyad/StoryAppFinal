package com.d121211017.stroyappsubmission.data.repository

import android.content.Context
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun injectRepository(context: Context) : StoryAppRepository {
        val userPreferences = UserPreferences.getInstance(context.datastore)
        val token = runBlocking { userPreferences.getUserToken().first()}
        val apiService = ApiConfig.getApiService(token = token)
        return StoryAppRepository(apiService, userPreferences)
    }
}