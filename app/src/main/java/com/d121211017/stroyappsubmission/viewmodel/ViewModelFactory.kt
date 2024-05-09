package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.repository.Injection

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                storyAppRepository = Injection.injectRepository(context = mApplication.applicationContext),
                application = mApplication
                ) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                storyAppRepository = Injection.injectRepository(context = mApplication.applicationContext),
                application = mApplication) as T
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