package com.example.wowdemo.viewModel

import com.example.wowdemo.viewModel.common.StateEvent


sealed class ProductsStateEvent : StateEvent {
    // TODO: Remove ping samples
    object PingProductsStateEvent: ProductsStateEvent() {
        override fun errorInfo(): String = "Error PingProductsStateEvent"
        override fun toString(): String = "PingProductsStateEvent"
    }

}