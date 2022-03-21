package com.example.wowdemo.viewModel

import com.example.wowdemo.viewModel.common.StateEvent


sealed class ProductsStateEvent : StateEvent {

    data class GetProductsStateEvent(
        val page: Int
    ) : ProductsStateEvent() {
        override fun errorInfo(): String = "Error getting products"
        override fun toString(): String = "PingProductsStateEvent"
    }

    data class SetProductFavouriteStateEvent(val productId: Int) : ProductsStateEvent() {
        override fun errorInfo(): String = "Error marking as favourite"
        override fun toString(): String = "SetProductFavouriteStateEvent"
    }

}