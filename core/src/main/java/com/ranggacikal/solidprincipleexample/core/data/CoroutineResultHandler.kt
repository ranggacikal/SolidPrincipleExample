package com.ranggacikal.solidprincipleexample.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.ranggacikal.solidprincipleexample.core.entity.Result
import com.ranggacikal.solidprincipleexample.core.util.tripleOf

fun <T> resultFlow(
    localLoad: suspend () -> Result<T>,
    networkCall: suspend () -> Result<T>,
    saveCallResult: suspend (T) -> Unit,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
) = flow<Result<T>> {
    emit(Result.loading<T>())
    val data = localLoad().data
    when {
        data != null -> checkDataType(data, networkCall, saveCallResult)
        else -> emit(callRemote(networkCall, saveCallResult))
    }
}.flowOn(dispatcher.io())

fun <T> resultLocalFlow(
    localLoad: suspend () -> Result<T>,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
) = flow {
    emit(Result.loading<T>())
    emit(localLoad())
}.flowOn(dispatcher.io())

fun <T> resultLocalFlow(
    localUpdate: suspend () -> Unit,
    localLoad: suspend () -> Result<T>,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
) = flow {
    localUpdate()
    emit(Result.loading<T>())
    emit(localLoad())
}.flowOn(dispatcher.io())

fun <T> resultSaveLocalFlow(
    data: suspend () -> Result<T>,
    localSave: suspend (T) -> Unit,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
) = flow {
    data().data?.let { emit(localSave(it)) }
}.flowOn(dispatcher.io())

fun resultSaveLocalFlow(
    localUpdate: suspend () -> Unit,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
) = flow {
    localUpdate()
    emit(Result.success(true))
}.flowOn(dispatcher.io())

private suspend fun <T> FlowCollector<Result<T>>.checkDataType(
    data: T,
    networkCall: suspend () -> Result<T>,
    saveCallResult: suspend (T) -> Unit
) = when (data) {
    is List<*> -> {
        if (data.isNotEmpty()) emit(Result.success(data))
        else emit(callRemote(networkCall, saveCallResult))
    }

    else -> emit(Result.success(data))
}

inline fun <A> resultFlow(
    crossinline networkCall: suspend () -> Result<A>,
    crossinline saveCallResult: suspend (A) -> Unit,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
): Flow<Result<A>> = flow {
    emit(Result.loading())
    val responseStatus = networkCall()
    when (responseStatus.status) {
        Result.Status.SUCCESS -> {
            saveCallResult(responseStatus.data!!)
            emit(Result.success(responseStatus.data))
        }

        Result.Status.ERROR -> {
            emit(
                Result.error(
                    data = null,
                    message = responseStatus.message.toString(),
                    title = responseStatus.title,
                    code = responseStatus.code,
                    httpStatus = responseStatus.httpStatus
                )
            )
        }

        Result.Status.LOADING -> {
            // Do nothing
        }
    }
}.flowOn(dispatcher.io())

fun <A> resultFlow(
    networkCall: suspend () -> Result<A>,
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider()
): Flow<Result<A>> = flow {
    emit(Result.loading())
    val responseStatus = networkCall.invoke()
    when (responseStatus.status) {
        Result.Status.SUCCESS -> responseStatus.data?.let { emit(Result.success(it)) }
        Result.Status.ERROR -> {
            emit(
                Result.error(
                    data = null,
                    message = responseStatus.message.toString(),
                    title = responseStatus.title,
                    code = responseStatus.code,
                    httpStatus = responseStatus.httpStatus
                )
            )
        }

        Result.Status.LOADING -> {
            // Do nothing
        }
    }
}.flowOn(dispatcher.io())

suspend fun <T> callRemote(
    networkCall: suspend () -> Result<T>,
    saveCallResult: suspend (T) -> Unit,
    saveCallResultError: suspend (Triple<String?, String?, String?>) -> Unit
): Result<T> {
    val response = networkCall()
    return when (response.status) {
        Result.Status.SUCCESS -> {
            saveCallResult(response.data!!)
            Result.success(response.data)
        }

        Result.Status.ERROR -> {
            saveCallResultError(tripleOf(response.code, response.title, response.message))
            Result.error(
                code = response.code,
                title = response.title,
                message = response.message.toString(),
            )
        }

        Result.Status.LOADING -> Result.loading()
    }
}

suspend fun <T> callRemote(
    networkCall: suspend () -> Result<T>,
    saveCallResult: suspend (T) -> Unit
): Result<T> {
    val response = networkCall()
    return when (response.status) {
        Result.Status.SUCCESS -> {
            saveCallResult(response.data!!)
            Result.success(response.data)
        }

        Result.Status.ERROR -> Result.error(
            message = response.message.toString(),
            title = response.title,
            code = response.code,
            httpStatus = response.httpStatus
        )

        Result.Status.LOADING -> Result.loading()
    }
}

suspend fun <T> callRemote(
    networkCall: suspend () -> Result<T>
): Result<T?> {
    val response = networkCall()
    return when (response.status) {
        Result.Status.SUCCESS -> {
            Result.success(response.data)
        }

        Result.Status.ERROR -> Result.error(
            message = response.message.toString(),
            title = response.title,
            code = response.code,
            httpStatus = response.httpStatus
        )

        Result.Status.LOADING -> Result.loading()
    }
}