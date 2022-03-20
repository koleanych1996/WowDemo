package com.example.wowdemo.repository

import com.example.wowdemo.viewModel.ProductDetailsFragmentViewState
import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.common.StateEvent
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {

    fun getProduct(
        stateEvent: StateEvent,
        productId: Int
    ): Flow<DataState<ProductDetailsFragmentViewState>>

}