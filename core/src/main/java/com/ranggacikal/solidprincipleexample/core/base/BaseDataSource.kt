package com.ranggacikal.solidprincipleexample.core.base

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.SocketTimeoutException
import com.ranggacikal.solidprincipleexample.core.entity.Result
import com.ranggacikal.solidprincipleexample.core.entity.error.ErrorResponse
import timber.log.Timber

abstract class BaseDataSource {


    /**
     * Use this method if the response returned from the service is in form of a collection but,
     * it's wrapped inside a single named object and you just want to get its collection inside. For example:
     * ```
     * {
     *     "aSingleObjectThatContainsTheDataCollection": [
     *          { A collection of data },
     *          {...},
     *          {...},
     *          .
     *          .
     *          .
     *      ]
     * }
     * ```
     * This function will automatically get the value contained in the current function
     *
     */
    suspend fun <T> getResult(call: suspend () -> Response<Map<String, T>>): Result<T> = try {
        val response = call()

        if (response.isSuccessful) response.body()?.run {
            Result.success(values.first())
        } ?: errorHandler(response.code(), null, " ${response.code()} ${response.message()}")
        else errorHandler(response.code(), response.errorBody(), response.message())
    } catch (e: Exception) {
        Timber.e(e)
        errorHandler(0, null, e.message ?: e.toString())
    }

    /**
     * Similar use with [getResult] but, the response will be the whole thing and delegate the returned value to the
     * caller of this function to be processed further.
     */
    suspend fun <T> getResultWithSingleObject(call: suspend () -> Response<T>): Result<T> = try {
        val response = call()
        if (response.isSuccessful) response.body()?.run {
            Result.success(this)
        } ?: errorHandler(
            response.code(),
            response.errorBody(),
            "returns NULL. ${response.message()}"
        )
        else errorHandler(response.code(), response.errorBody(), "Error. ${response.message()}")
    } catch (ex: SocketTimeoutException) {
        errorHandler("504".toInt(), null, ex.message ?: ex.toString())
    } catch (e: Exception) {
        Timber.e(e)
        errorHandler(0, null, e.message ?: e.toString())
    }

    suspend fun <T> getLocalResult(load: suspend () -> T?): Result<T> = with(load.invoke()) {
        return@with if (this != null) Result.success(this)
        else Result.error(NullPointerException().message.toString())
    }

    private fun <T> errorHandler(
        httpCode: Int,
        errorBody: ResponseBody?,
        extraMessage: String? = ""
    ): Result<T> {
        try {
            val errorDetail = errorBody?.string()?.run {
                Moshi.Builder().build().adapter(ErrorResponse::class.java).fromJson(this)
            }
            return Result.error(
                message = errorDetail?.message
                    ?: "Network call has failed for the following reason(s): $extraMessage",
                data = null,
                title = errorDetail?.title,
                code = errorDetail?.code,
                httpStatus = httpCode.toString()
            )
        } catch (e: Exception) {
            return Result.error(
                message = "",
                data = null,
                title = "",
                code = "",
                httpStatus = httpCode.toString()
            )
        }
    }
}