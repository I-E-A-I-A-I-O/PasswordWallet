package com.justdance.passwordwaller.network

import com.google.gson.annotations.SerializedName

data class PasswordInfo(
    @SerializedName("password_id") val passwordId: String,
    @SerializedName("description") val description: String,
    @SerializedName("password") val password: String
)