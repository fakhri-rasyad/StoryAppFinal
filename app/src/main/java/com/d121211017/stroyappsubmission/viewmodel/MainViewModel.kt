package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainViewModel(application: Application, pref: UserPreferences) : ViewModel() {

    private val userPreferences = pref
    fun checkForUserPref() : Boolean{
        val token = runBlocking {userPreferences.getUserToken().first()}
        return token != ""
    }
}