package com.example.wowdemo.repository

import com.example.wowdemo.model.GetProductsResponse
import com.example.wowdemo.model.Product
import com.example.wowdemo.network.ApiResponseHandler
import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.viewModel.ProductDetailsFragmentViewState
import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.common.StateEvent
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductDetailsRepositoryImpl
@Inject constructor(
    private val gson: Gson,
    private val wowDemoApiService: WowDemoApiService
) : ProductDetailsRepository {

    override fun getProduct(
        stateEvent: StateEvent,
        productId: Int
    ): Flow<DataState<ProductDetailsFragmentViewState>> {
        return flow {

            val apiResult = safeApiCall(gson = gson, dispatcher = Dispatchers.IO) {
                wowDemoApiService.getProduct(productId = productId)
            }

            emit(
                object : ApiResponseHandler<ProductDetailsFragmentViewState, Product>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: Product): DataState<ProductDetailsFragmentViewState> {
                        resultObj.let {
                            return DataState.data(
                                stateEvent = stateEvent,
                                response = null,
                                data = ProductDetailsFragmentViewState(
                                    product = resultObj,
                                )
                            )
                        }
                    }
                }.getResult()
            )

        }

    }
}