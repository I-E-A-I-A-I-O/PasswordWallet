package com.justdance.passwordwaller.network

import com.squareup.moshi.Json

data class NewPasswordInfo (
    @Json(name = "description") val description: String,
    @Json(name = "password") val password: String,
    @Json(name = "password_confirm") val passwordConfirm: String
    )