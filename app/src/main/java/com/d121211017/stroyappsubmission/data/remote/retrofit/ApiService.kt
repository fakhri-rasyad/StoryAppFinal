package com.d121211017.stroyappsubmission.data.remote.retrofit

import com.d121211017.stroyappsubmission.data.remote.entity.GetStories
import com.d121211017.stroyappsubmission.data.remote.entity.LoginResponse
import com.d121211017.stroyappsubmission.data.remote.entity.SimpleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerAccount(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
        ) : Call<SimpleResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginAccount(
        @Field("email") email : String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part("description") description : RequestBody,
        @Part file : MultipartBody.Part
    ) : Call<SimpleResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location : Int = 1
    ) : Call<GetStories>

    @GET("stories")
    suspend fun getStoriesForPaging(
        @Query("page") page : Int = 1,
        @Query("size") size : Int = 20
    ) : GetStories
}