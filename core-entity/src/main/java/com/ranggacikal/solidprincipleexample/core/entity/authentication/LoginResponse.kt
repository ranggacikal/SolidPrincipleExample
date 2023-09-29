package com.ranggacikal.solidprincipleexample.core.entity.authentication

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
class LoginResponse(
    @Json(name = "pesan") var pesan: String? = null,
    @Json(name = "sukses") var sukses: Boolean? = null,
    @Json(name = "status") var status: Int? = null
) : Parcelable
