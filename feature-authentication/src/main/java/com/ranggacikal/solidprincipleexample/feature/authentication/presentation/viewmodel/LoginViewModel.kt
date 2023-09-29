package com.ranggacikal.solidprincipleexample.feature.authentication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ranggacikal.solidprincipleexample.api.domain.PostLoginUseCase
import com.ranggacikal.solidprincipleexample.core.base.BaseViewModel
import com.ranggacikal.solidprincipleexample.core.entity.Result
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginRequest
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase
) : BaseViewModel() {

    var email: String = ""
    var password: String = ""

    private val _postLogin = MutableLiveData<Result<LoginResponse>>()
    val postLogin: LiveData<Result<LoginResponse>> = _postLogin

    suspend fun login(request: LoginRequest) = postLoginUseCase(request).collect { _postLogin.postValue(it) }
}