package com.example.wowdemo.viewModel.common

data class DataState<T>(
    var data: T? = null,
    var stateMessage: StateMessage? = null,
    var stateEvent: StateEvent? = null
) {
    companion object {
        fun <T> data(
            data: T? = null,
            response: Response?,
            stateEvent: StateEvent?
        ): DataState<T> {
            return DataState(
                data = data,
                stateMessage = response?.let {
                    StateMessage(it)
                },
                stateEvent = stateEvent
            )
        }

        fun <T> error(
            response: Response,
            stateEvent: StateEvent?
        ): DataState<T> {
            return DataState(
                data = null,
                stateMessage = StateMessage(response),
                stateEvent = stateEvent
            )
        }
    }
}