package com.example.wowdemo.network

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()

    data class GenericError(
        val code: Int? = null,
        val errorMessage: String? = null,
        val errorObject: Any? = null
    ) : ApiResult<Nothing>()

    data class ForbiddenError(
        val errorMessage: String? = null,
        val errorObject: Any? = null
    ) : ApiResult<Nothing>()

    data class AuthError(
        val errorMessage: String? = null,
    ) : ApiResult<Nothing>()

    data class NotFoundError(
        val errorMessage: String? = null,
    ) : ApiResult<Nothing>()

    object NetworkError : ApiResult<Nothing>()
}
