package com.justdance.passwordwaller.network

import com.squareup.moshi.Json

data class PasswordInfo(
    @Json(name = "password_id") val passwordId: String,
    @Json(name = "description") val description: String,
    @Json(name = "password") val password: String
)