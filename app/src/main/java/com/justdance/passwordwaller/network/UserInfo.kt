package com.justdance.passwordwaller.network

import com.google.gson.annotations.SerializedName


data class UserInfo(
    @SerializedName("token") val token: String?,
    @SerializedName("email") val email: String,
)