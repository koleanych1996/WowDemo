package com.example.wowdemo.viewModel.common

import androidx.annotation.StringRes

data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val messageType: MessageType,
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

    object NotFoundError : MessageType() {
        override fun toString(): String = "Not Found Error"
    }

    object Info : MessageType()

    object None : MessageType()
}

interface StateMessageCallback {
    fun removeMessageFromStack()
}