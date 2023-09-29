package com.ranggacikal.solidprincipleexample.core.base

import androidx.lifecycle.LiveData
import com.ranggacikal.solidprincipleexample.core.entity.Result

fun <A> BaseFragment<*>.observeDataFlow(
    liveData: LiveData<out Result<A>>,
    specificGeneralError: List<String>? = null,
    isParallelCall: Boolean = false,
    onLoad: (() -> Unit)? = null,
    onError: ((Result<A>) -> Unit)? = null,
    onSuccess: ((A) -> Unit)? = null
) {
    liveData.observe(viewLifecycleOwner) { result ->
        result?.let {
            when (result.status) {
                Result.Status.LOADING -> onLoadForDataFlow(result, onLoad)
                Result.Status.SUCCESS -> onSuccessForDataFlow(result, onSuccess)
                Result.Status.ERROR -> onErrorForDataFlow(specificGeneralError, result, isParallelCall, onError)
            }
        }
    }
}

fun <A> BaseFragment<*>.onLoadForDataFlow(
    result: Result<A>,
    onLoad: (() -> Unit)? = null
) {
    result.getLoadingStateIfNotHandled()?.let {
        initCustomOnLoadState?.let { it() }

        onLoad?.let { it() }
    }
}

fun <A> BaseFragment<*>.onSuccessForDataFlow(
    result: Result<A>,
    onSuccess: ((A) -> Unit)? = null
) {
    retryCount = 0
    result.getSuccessStateIfNotHandled()?.let { data ->
        onSuccess?.let { it(data) }

        initCustomOnSuccessState?.let { it() }
    }
}

fun <A> BaseFragment<*>.onErrorForDataFlow(
    specificGeneralError: List<String>? = null,
    result: Result<A>,
    isParallelCall: Boolean,
    onError: ((Result<A>) -> Unit)? = null
) {
    result.getErrorStateIfNotHandled()?.let {
        initCustomOnErrorState?.let { it() }
    }
}