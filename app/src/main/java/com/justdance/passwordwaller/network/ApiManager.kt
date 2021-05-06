package com.justdance.passwordwaller.network

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://192.168.0.101:8000"
private val client = OkHttpClient.Builder().build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("password")
    fun addPassword(@Header("Authorization") authToken: String?, @Body newPassword: NewPasswordInfo): Call<PasswordInfo>

    @Headers("Content-Type: application/json")
    @POST("session/login")
    fun login(@Body loginInfo: LoginInfo): Call<UserInfo>

    @GET("passwords")
    suspend fun getPasswords(): List<PasswordInfo>

    @Headers("Content-Type: application/json")
    @POST("users/user")
    fun registerUser(@Body newUser: NewUser): Call<UserInfo>
}

object ApiManager {
    val retrofitService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}

class ApiService {
    fun addPassword(authToken: String?, newPassword: NewPasswordInfo, onResult: (PasswordInfo?, ErrorResponse?) -> Unit) {
        ApiManager.retrofitService.addPassword(authToken, newPassword).enqueue(
            object : Callback<PasswordInfo> {
                override fun onResponse(call: Call<PasswordInfo>, response: Response<PasswordInfo>) {
                    if (response.isSuccessful) {
                        onResult(response.body(), null)
                    } else {
                        try {
                            val converter: Converter<ResponseBody, ErrorResponse> =
                                retrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
                            val errorResponse = converter.convert(response.errorBody()!!)
                            onResult(null, errorResponse)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            onResult(null, null)
                        }
                    }
                }

                override fun onFailure(call: Call<PasswordInfo>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null, null)
                }

            }
        )
    }

    fun login(loginInfo: LoginInfo, onResult: (UserInfo?, ErrorResponse?) -> Unit) {
        ApiManager.retrofitService.login(loginInfo).enqueue(
            object : Callback<UserInfo> {
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        onResult(body, null)
                    } else {
                        try {
                            val converter: Converter<ResponseBody, ErrorResponse> =
                                retrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
                            val error = converter.convert(response.errorBody()!!)
                            onResult(null, error)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            onResult(null, null)
                        }
                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null, null)
                }

            }
        )
    }

    suspend fun getPasswords(): List<PasswordInfo> {
        TODO("Not yet implemented")
    }

    fun registerUser(newUser: NewUser, onResult: (UserInfo?, ErrorResponse?) -> Unit) {
        ApiManager.retrofitService.registerUser(newUser).enqueue(
            object : Callback<UserInfo> {
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    if (response.isSuccessful) {
                        val addedUser = response.body()
                        onResult(addedUser, null)
                    } else {
                        try {
                            val converter: Converter<ResponseBody, ErrorResponse> =
                                retrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
                            val errorResponse = converter.convert(response.errorBody()!!)
                            onResult(null, errorResponse)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            onResult(null, null)
                        }
                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null, null)
                }
            }
        )
    }

}