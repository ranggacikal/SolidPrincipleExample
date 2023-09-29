package com.ranggacikal.solidprincipleexample.api.data.api

import com.ranggacikal.solidprincipleexample.api.util.ApiConstants.URL_POST_LOGIN_USER
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginRequest
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST(URL_POST_LOGIN_USER)
    suspend fun postLogin(
        @Body request: LoginRequest
    ) : Response<LoginResponse>

}