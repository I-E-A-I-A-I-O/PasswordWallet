package com.justdance.passwordwaller.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.0.101:8000"
private val moshi = Moshi.Builder().
        add(KotlinJsonAdapterFactory()).
        build()
private val retrofit = Retrofit.Builder().
        addConverterFactory(MoshiConverterFactory.create(moshi)).
        baseUrl(BASE_URL).
        build()

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("password")
    suspend fun addPassword(@Body newPassword: NewPasswordInfo): Call<PasswordInfo>
    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body loginInfo: LoginInfo): Call<UserInfo>?
    @GET("passwords")
    suspend fun getPasswords(): List<PasswordInfo>
}

object ApiManager {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}