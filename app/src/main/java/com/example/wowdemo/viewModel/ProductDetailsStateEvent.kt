package com.example.wowdemo.viewModel

import com.example.wowdemo.viewModel.common.StateEvent

sealed class ProductDetailsStateEvent : StateEvent {

    data class GetProductStateEvent(
        val productId: Int
    ) : ProductDetailsStateEvent() {
        override fun errorInfo(): String = "Error getting product #${productId}"
        override fun toString(): String = "GetProductStateEvent"
    }
}