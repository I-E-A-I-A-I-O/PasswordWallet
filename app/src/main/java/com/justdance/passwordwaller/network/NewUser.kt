package com.justdance.passwordwaller.network

import com.google.gson.annotations.SerializedName

data class NewUser (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirm") val confirm: String
    )