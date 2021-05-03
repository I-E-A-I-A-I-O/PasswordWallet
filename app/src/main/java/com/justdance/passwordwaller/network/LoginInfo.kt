package com.justdance.passwordwaller.network

import com.squareup.moshi.Json

data class LoginInfo(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)