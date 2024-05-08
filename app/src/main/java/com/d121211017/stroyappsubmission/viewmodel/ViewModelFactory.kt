package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.data.local.UserPreferences

class ViewModelFactory private constructor(private val mApplication: Application, private val mPref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application, preferences: UserPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, preferences)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(mApplication, mPref) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(mApplication) as T
        } else if(modelClass.isAssignableFrom(StoryListViewModel::class.java)){
            return StoryListViewModel(mPref) as T
        } else if(modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(mApplication, mPref) as T
        } else if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mApplication, mPref) as T
        } else if(modelClass.isAssignableFrom(MapViewModel::class.java)){
            return MapViewModel(mApplication, mPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}