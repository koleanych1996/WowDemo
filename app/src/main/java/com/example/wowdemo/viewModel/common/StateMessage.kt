package com.example.wowdemo.viewModel

data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val messageType: MessageType,
    val validation: InternalValidation = InternalValidation.None
)

sealed class MessageType {

    object Success : MessageType()

    object Error : MessageType() {
        override fun toString(): String = "Error"
    }

    object AuthError : MessageType() {
        override fun toString(): String = "Authentication Error"
    }

    object Info : MessageType()

    data class None(val message: String?) : MessageType()
}

sealed class InternalValidation {
    data class EmptyFieldsPresent(
        val mapOfViewAndMessage: Map<String, String> = hashMapOf()
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
