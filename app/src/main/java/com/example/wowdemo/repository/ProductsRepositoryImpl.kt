package com.example.wowdemo.repository

import com.example.wowdemo.model.GetProductsResponse
import com.example.wowdemo.network.ApiResponseHandler
import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.ProductsFragmentViewState
import com.example.wowdemo.viewModel.common.StateEvent
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl
@Inject constructor(
    private val gson: Gson,
    private val wowDemoApiService: WowDemoApiService
) : ProductsRepository {

    override fun getProductsList(stateEvent: StateEvent): Flow<DataState<ProductsFragmentViewState>> {
        return flow {

            val apiResult = safeApiCall(gson = gson, dispatcher = Dispatchers.IO) {
                wowDemoApiService.getProducts()
            }

            emit(
                object : ApiResponseHandler<ProductsFragmentViewState, GetProductsResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GetProductsResponse): DataState<ProductsFragmentViewState> {
                        resultObj.let {
                            return DataState.data(
                                stateEvent = stateEvent,
                                response = null,
                                data = ProductsFragmentViewState(
                                    productsList = resultObj.products,
                                )
                            )
                        }
                    }
                }.getResult()
            )

        }

    }
}