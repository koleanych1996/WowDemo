package com.example.wowdemo.network

import com.google.gson.annotations.Expose

interface ApiException {
    fun displayErrors(): String
}

data class ApiErrorException(
    @Expose
    override val message: String,
    @Expose
    val errors: Map<String, List<String>>?
) : RuntimeException(), ApiException {

    override fun displayErrors(): String {
        return if (errors == null) {
            message
        } else {
            val builder = StringBuilder()
            errors.values.forEach { errorList ->
                errorList.forEachIndexed { index, s ->
                    builder.append(s)
                    if (index < errorList.size - 1) {
                        builder.append("\n")
                    }
                }
            }
            builder.toString()
        }
    }
}

data class AuthException(
    @Expose
    override val message: String
) : RuntimeException(), ApiException {

    override fun displayErrors(): String {
        return message
    }

}

data class ForbiddenException(
    @Expose
    override val message: String
) : RuntimeException(), ApiException {

    override fun displayErrors(): String {
        return message
    }

}