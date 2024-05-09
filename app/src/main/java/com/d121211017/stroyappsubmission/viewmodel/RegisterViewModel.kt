package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.remote.entity.SimpleResponse
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import com.d121211017.stroyappsubmission.data.repository.StoryAppRepository
import com.d121211017.stroyappsubmission.getErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val storyAppRepository: StoryAppRepository, private val application: Application): ViewModel() {

    private val _isLoadingRegistration = MutableLiveData<Boolean>()
    val loadingRegistration : LiveData<Boolean> = _isLoadingRegistration

    private val _isRegistrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess : LiveData<Boolean> = _isRegistrationSuccess

    private val _handleResponseError = MutableLiveData<Pair<Boolean, String>>()
    val handleResponseError : LiveData<Pair<Boolean, String>> = _handleResponseError

    private val _isEmailValid = MutableLiveData<Boolean>()

    private val _isPasswordValid = MutableLiveData<Boolean>()

    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_isEmailValid) { value = it && (_isPasswordValid.value ?: false) }
        addSource(_isPasswordValid) { value = it && (_isEmailValid.value ?: false) }
    }
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private var name = ""
    private var email = ""
    private var password = ""

    fun postRegistration(
    ){
        _isLoadingRegistration.postValue(true)
        val client = ApiConfig.getApiService().registerAccount(name, email, password)
        client.enqueue(object : Callback<SimpleResponse> {
            override fun onResponse(p0: Call<SimpleResponse>, p1: Response<SimpleResponse>) {
                _isLoadingRegistration.postValue(false)
                val responseBody = p1.body()
                if(p1.isSuccessful && responseBody != null){
                    _isRegistrationSuccess.postValue(true)
                } else if(!p1.isSuccessful) {
                    val errorResponse = getErrorResponse(p1.errorBody()!!.string())
                    _handleResponseError.postValue(Pair(errorResponse.error, errorResponse.message!!))
                }
            }
            override fun onFailure(p0: Call<SimpleResponse>, p1: Throwable) {
                _isLoadingRegistration.postValue(false)
                _handleResponseError.postValue(Pair(false, application.applicationContext.getString(R.string.unknown_error)))
            }
        })
    }

    fun nameValidation(name: String){
        this.name = name
    }
    fun emailValidation(email: String){
        _isEmailValid.postValue(Patterns.EMAIL_ADDRESS.matcher(email).matches())
        this.email = email
    }
    fun passwordValidation(password : String){
        if (password.isNotEmpty() && password.length >= 8) _isPasswordValid.postValue(true)
        else _isPasswordValid.postValue(false)
        this.password = password
    }
}