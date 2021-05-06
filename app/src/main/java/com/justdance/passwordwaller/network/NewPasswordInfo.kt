package com.justdance.passwordwaller.network

import com.google.gson.annotations.SerializedName

data class NewPasswordInfo (
    @SerializedName("description") val description: String,
    @SerializedName("password") val password: String,
    )