package com.d121211017.stroyappsubmission.data.repository

import android.content.Context
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig

object Injection {
    fun injectRepository(context: Context) : StoryAppRepository {
        val userPreferences = UserPreferences.getInstance(context.datastore)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(apiService, userPreferences)
    }
}