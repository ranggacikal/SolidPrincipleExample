package com.ranggacikal.solidprincipleexample.api.data.remote

import com.ranggacikal.solidprincipleexample.api.data.api.AuthenticationApi
import com.ranggacikal.solidprincipleexample.core.base.BaseDataSource
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginRequest
import javax.inject.Inject

class AuthenticationDataSource @Inject constructor(
    private var api: AuthenticationApi
) : BaseDataSource() {

    suspend fun postLogin(request: LoginRequest) = getResultWithSingleObject { api.postLogin(request) }

}