package com.example.wowdemo.viewModel.common

import androidx.annotation.StringRes

data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val messageType: MessageType,
    val validation: InternalValidation = InternalValidation.None,
    @StringRes val resId: Int? = null,
    val showError: Boolean = true,
)

sealed class MessageType {

    object Success : MessageType() {
        override fun toString(): String = "Success"
    }

    object Error : MessageType() {
        override fun toString(): String = "Error"
    }

    object ForbiddenError : MessageType() {
        override fun toString(): String = "Ooops, something went wrong"
    }

    object AuthError : MessageType() {
        override fun toString(): String = "Authentication Error"
    }

    object NotFoundError : MessageType() {
        override fun toString(): String = "Not Found Error"
    }

    object Info : MessageType()

    object None : MessageType()
}

sealed class InternalValidation {
    data class EmptyFieldsPresent(
        val mapOfViewAndMessage: Map<String, String> = hashMapOf()
    ) : InternalValidation()

    data class InvalidPassword(
        val message: String? = null
    ) : InternalValidation()

    data class InvalidEmail(
        val message: String? = null
    ) : InternalValidation()

    data class PasswordsDoNotMatch(
        val message: String? = null
    ) : InternalValidation()

    data class ExternalErrors(
        val message: String,
        val errors: Map<String, List<String>>?
    ) : InternalValidation()

    data class IncompleteReview(
        val message: String? = null
    ) : InternalValidation()

    object None : InternalValidation()
}


interface StateMessageCallback {
    fun removeMessageFromStack()
}