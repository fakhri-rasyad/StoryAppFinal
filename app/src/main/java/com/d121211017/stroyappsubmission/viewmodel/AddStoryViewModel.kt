package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.remote.entity.SimpleResponse
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import com.d121211017.stroyappsubmission.getErrorResponse
import com.d121211017.stroyappsubmission.reduceFileImage
import com.d121211017.stroyappsubmission.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(application: Application, pref: UserPreferences) : ViewModel(){
    private val applicationContext = application.applicationContext
    private val userPreferences = pref

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri : LiveData<Uri> = _imageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<String>()
    val response : LiveData<String> = _response

    private val _isUploadSuccess = MutableLiveData<Boolean>()
    val isUploadSuccess : LiveData<Boolean> = _isUploadSuccess

    fun setPhotoUri(photoUri : Uri){
        _imageUri.postValue(photoUri)
    }

    fun postStory(description: String){
        _isLoading.postValue(true)
        val uri = _imageUri.value!!
        val imageFile = uriToFile(uri, applicationContext).reduceFileImage()
        Log.d("Image File", "showImage: ${imageFile.path}")
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val token = runBlocking { userPreferences.getUserToken().first() }
        val client = ApiConfig.getApiService(token).addStory(file = multipartBody, description = requestBody)

        client.enqueue(object : Callback<SimpleResponse>{
            override fun onResponse(p0: Call<SimpleResponse>, p1: Response<SimpleResponse>) {
                _isLoading.postValue(false)
                val responseBody = p1.body()
                if(p1.isSuccessful && responseBody != null){
                    _response.postValue(responseBody.message.toString())
                    _isUploadSuccess.postValue(true)
                } else if(!p1.isSuccessful){
                    val failureResponse = getErrorResponse(p1.errorBody()!!.string())
                    _response.postValue(failureResponse.message.toString())
                    _isUploadSuccess.postValue(false)
                }
            }

            override fun onFailure(p0: Call<SimpleResponse>, p1: Throwable) {
                _isLoading.postValue(false)
                _response.postValue(applicationContext.getString(R.string.unknown_error))
                _isUploadSuccess.postValue(false)
            }
        })
    }


}