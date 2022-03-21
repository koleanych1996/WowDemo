package com.example.wowdemo.repository

import android.content.Context
import android.util.Log
import com.example.wowdemo.Constants.CACHE_ERROR_TIMEOUT
import com.example.wowdemo.Constants.CACHE_TIMEOUT
import com.example.wowdemo.Constants.NETWORK_ERROR_TIMEOUT
import com.example.wowdemo.Constants.NETWORK_TIMEOUT
import com.example.wowdemo.Constants.TAG
import com.example.wowdemo.Constants.UNKNOWN_ERROR
import com.example.wowdemo.network.ApiErrorException
import com.example.wowdemo.network.ApiResult
import com.example.wowdemo.viewModel.common.MessageType
import com.example.wowdemo.viewModel.common.Response
import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.common.StateEvent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    gson: Gson,
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            Log.e(TAG, "safeApiCall: error", throwable)
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    when (code) {
                        403 -> {
                            val errorObject = getErrorObject(gson, errorResponse)
                            ApiResult.ForbiddenError(
                                errorResponse,
                                errorObject
                            )
                        }
                        404 -> {
                            ApiResult.NotFoundError(
                                errorResponse
                            )
                        }
                        408 -> {
                            ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                        }
                        else -> {
                            val errorObject = getErrorObject(gson, errorResponse)
                            ApiResult.GenericError(
                                code,
                                errorResponse,
                                errorObject = errorObject
                            )
                        }
                    }
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                else -> {
                    ApiResult.GenericError(
                        null,
                        UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            Log.e(TAG, "safeCacheCall: error", throwable)
            when (throwable) {
                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(errorMessage = throwable.localizedMessage)
                }
            }
        }
    }
}


fun <ViewState> buildError(
    context: Context,
    messageRes: Int,
    stateEvent: StateEvent?
): DataState<ViewState> {
    return DataState.error(
        response = Response(
            message = "${stateEvent?.errorInfo()}\nReason: ${context.getString(messageRes)}",
            messageType = MessageType.Error
        ),
        stateEvent = stateEvent
    )
}

fun <ViewState> buildError(
    message: String?,
    stateEvent: StateEvent?
): DataState<ViewState> {
    return DataState.error(
        response = Response(
            message = "${stateEvent?.errorInfo()}\nReason: $message",
            messageType = MessageType.Error
        ),
        stateEvent = stateEvent
    )
}

fun <ViewState> buildForbiddenError(
    message: String?,
    stateEvent: StateEvent?
): DataState<ViewState> {
    return DataState.error(
        response = Response(
            message = "${stateEvent?.errorInfo()}\nReason: $message",
            messageType = MessageType.ForbiddenError
        ),
        stateEvent = stateEvent
    )
}

fun <ViewState> buildNotFoundError(
    message: String?,
    stateEvent: StateEvent?
): DataState<ViewState> {
    return DataState.error(
        response = Response(
            message = "${stateEvent?.errorInfo()}\nReason: $message",
            messageType = MessageType.NotFoundError
        ),
        stateEvent = stateEvent
    )
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        UNKNOWN_ERROR
    }
}

private fun getErrorObject(gson: Gson, json: String?): Any? {
    return try {
        gson.fromJson(json, ApiErrorException::class.java)
    } catch (exception: Exception) {
        null
    }
}