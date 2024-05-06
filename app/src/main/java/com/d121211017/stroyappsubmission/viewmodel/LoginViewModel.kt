package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.remote.entity.LoginResponse
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import com.d121211017.stroyappsubmission.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application : Application, private val pref: UserPreferences) : ViewModel() {

    private val _isEmailValid = MutableLiveData<Boolean>()
    private val _isPasswordValid  = MutableLiveData<Boolean>()

    private val _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin : LiveData<Boolean> = _isLoadingLogin

    private val _errorResponse = MutableLiveData<Pair<Boolean, String>>()
    val errorResponse : LiveData<Pair<Boolean, String>> = _errorResponse

    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_isEmailValid) { value = it && (_isPasswordValid.value ?: false) }
        addSource(_isPasswordValid) { value = it && (_isEmailValid.value ?: false) }
    }

    val isButtonEnabled : LiveData<Boolean> = _isButtonEnabled

    private val _isLoginSuccess = MutableLiveData<Boolean>()
    val isLoginSuccess : LiveData<Boolean> = _isLoginSuccess

    private val applicationContext : Context = application.applicationContext

    private var email = ""
    private var password = ""
    fun postLogin(){
        _isLoadingLogin.postValue(true)

        val client = ApiConfig.getApiService().loginAccount(email, password)
        client.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(p0: Call<LoginResponse>, p1: Response<LoginResponse>) {
                _isLoadingLogin.postValue(false)
                val responseBody = p1.body()
                if(p1.isSuccessful && responseBody != null){
                    val loginResult = responseBody.loginResult!!
                    viewModelScope.launch {
                        pref.saveUserData(loginResult.token!!, loginResult.name!!, loginResult.userId!!)
                    }

                    _isLoginSuccess.postValue(true)
                } else if (!p1.isSuccessful) {
                    val errorResponse = getErrorResponse(p1.errorBody()!!.string())
                    _errorResponse.postValue(Pair(errorResponse.error, errorResponse.message!!))
                }
            }
            override fun onFailure(p0: Call<LoginResponse>, p1: Throwable) {
                _isLoadingLogin.postValue(false)
                _errorResponse.postValue(Pair(true, applicationContext.getString(R.string.unknown_error)))
            }
        })
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