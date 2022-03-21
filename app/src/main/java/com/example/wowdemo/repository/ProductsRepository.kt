package com.example.wowdemo.repository

import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.ProductsFragmentViewState
import com.example.wowdemo.viewModel.common.StateEvent
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getProductsList(stateEvent: StateEvent, page: Int): Flow<DataState<ProductsFragmentViewState>>

    fun setProductFavourite(stateEvent: StateEvent, productId: Int): Flow<DataState<ProductsFragmentViewState>>

}