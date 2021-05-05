package com.justdance.passwordwaller.network

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("error") val error: String
    )