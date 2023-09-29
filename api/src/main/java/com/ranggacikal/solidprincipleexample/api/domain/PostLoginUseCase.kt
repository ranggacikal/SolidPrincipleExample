package com.ranggacikal.solidprincipleexample.api.domain

import com.ranggacikal.solidprincipleexample.api.data.repository.AuthenticationRepository
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginRequest
import dagger.Binds
import javax.inject.Inject
class PostLoginUseCase @Inject constructor(
    private val repo: AuthenticationRepository
) {
    operator fun invoke(request: LoginRequest) = repo.postLogin(request)
}