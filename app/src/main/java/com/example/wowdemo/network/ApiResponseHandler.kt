package com.example.wowdemo.network

import com.example.wowdemo.Constants.GENERIC_AUTH_ERROR
import com.example.wowdemo.Constants.NETWORK_ERROR
import com.example.wowdemo.Constants.NOT_FOUND_ERROR
import com.example.wowdemo.Constants.UNKNOWN_ERROR
import com.example.wowdemo.viewModel.common.*

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val showError: Boolean = true,
    private var handleNotFoundError: (suspend () -> DataState<ViewState>)? = null,
    private val stateEvent: StateEvent
) {
    suspend fun getResult(): DataState<ViewState> {

        return when (response) {
            is ApiResult.AuthError ->
                DataState.error(
                    stateEvent = stateEvent,
                    response = Response(
                        messageType = MessageType.Error,
                        message = GENERIC_AUTH_ERROR,
                        showError = showError

                    )
                )

            is ApiResult.ForbiddenError -> {
                val message = (response.errorObject as? ApiErrorException)?.displayErrors()
                DataState.error(
                    stateEvent = stateEvent,
                    response = Response(
                        messageType = MessageType.ForbiddenError,
                        message = message,
                        showError = showError
                    )
                )
            }

            is ApiResult.GenericError -> {
                val (message, messageType) = getMessageAndMessageTypeFromResponse(response)
                DataState.error(
                    response = Response(
                        message = message,
                        messageType = messageType,
                        showError = showError
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NotFoundError -> {

                val newState = if (handleNotFoundError == null) {
                    DataState.error(
                        response = Response(
                            message = "${stateEvent.errorInfo()}\nReason: $NOT_FOUND_ERROR",
                            messageType = MessageType.NotFoundError,
                            showError = showError
                        ),
                        stateEvent = stateEvent
                    )
                } else {
                    handleNotFoundError!!.invoke()
                }
                newState
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\nReason: $NETWORK_ERROR",
                        messageType = MessageType.Error,
                        showError = showError
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> if (response.value == null) {
                DataState.error(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\nReason: Data is NULL.",
                        messageType = MessageType.Error,
                        showError = showError
                    ),
                    stateEvent = stateEvent
                )
            } else {
                handleSuccess(resultObj = response.value)
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

    private fun getMessageAndMessageTypeFromResponse(response: ApiResult.GenericError): Pair<String, MessageType> {
        var message = response.errorMessage ?: UNKNOWN_ERROR
        var messageType: MessageType = MessageType.Error

        response.errorObject?.let {
            when (it) {
                is ApiErrorException -> {
                    message = it.displayErrors()
                    messageType = MessageType.Error
                }
                else -> {
                    message = UNKNOWN_ERROR
                    messageType = MessageType.Error
                }
            }
        }

        return Pair(message, messageType)
    }

}
