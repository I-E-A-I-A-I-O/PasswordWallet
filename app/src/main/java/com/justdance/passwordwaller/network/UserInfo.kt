package com.justdance.passwordwaller.network

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "email") val email: String,
    @Json(name = "user_id") val userId: String
)