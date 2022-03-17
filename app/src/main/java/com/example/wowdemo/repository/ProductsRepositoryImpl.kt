package com.example.wowdemo.repository

import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.ProductsFragmentViewState
import com.example.wowdemo.viewModel.common.StateEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductsRepositoryImpl : ProductsRepository {

    override fun pingProducts(stateEvent: StateEvent): Flow<DataState<ProductsFragmentViewState>> {
        return flow {
            emit(
                DataState.data(
                    ProductsFragmentViewState(ping = true),
                    response = null,
                    stateEvent
                )
            )
        }

    }
}