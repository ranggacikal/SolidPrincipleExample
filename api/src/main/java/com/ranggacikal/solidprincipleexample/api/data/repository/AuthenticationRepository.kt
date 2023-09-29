package com.ranggacikal.solidprincipleexample.api.data.repository

import com.ranggacikal.solidprincipleexample.api.data.remote.AuthenticationDataSource
import com.ranggacikal.solidprincipleexample.core.data.CoroutineDispatcherProvider
import com.ranggacikal.solidprincipleexample.core.data.resultFlow
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginRequest
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val remote: AuthenticationDataSource
) {

    fun postLogin(request: LoginRequest) = resultFlow(
        networkCall = {remote.postLogin(request)}
    )

}