package com.ranggacikal.solidprincipleexample.core.entity.authentication

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
class LoginRequest(
    @Json(name = "email") var email : String? = null,
    @Json(name = "password") var password: String? = null
)