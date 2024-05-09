package com.d121211017.stroyappsubmission.viewmodel

import androidx.lifecycle.ViewModel
import com.d121211017.stroyappsubmission.data.repository.StoryAppRepository
import kotlinx.coroutines.runBlocking

class MainViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {

    fun checkForUserPref() : Boolean{
        val token = runBlocking { storyAppRepository.getUserToken() }
        return token != ""
    }
}