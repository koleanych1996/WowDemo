package com.example.wowdemo.repository

import android.content.Context
import androidx.constraintlayout.motion.utils.ViewState
import com.example.wowdemo.model.GetProductsResponse
import com.example.wowdemo.model.Product
import com.example.wowdemo.network.ApiResponseHandler
import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.persistance.WowDemoDao
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
    private val wowDemoApiService: WowDemoApiService,
    private val wowDemoDao: WowDemoDao,
    private val context: Context
) : ProductsRepository {


    override fun getProductsList(stateEvent: StateEvent): Flow<DataState<ProductsFragmentViewState>> {
        return object :
            NetworkBoundResource<List<Product>, List<Product>, ProductsFragmentViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    wowDemoApiService.getProducts().products
                },
                cacheCall = {
                    wowDemoDao.readAllProductsLocalStorage()
                },
                context = context,
                gson = gson
            ) {
            override fun shouldFetchFromNetwork(): Boolean = true

            override suspend fun handleCacheSuccess(resultObj: List<Product>): DataState<ProductsFragmentViewState> {
                return DataState.data(
                    data = ProductsFragmentViewState(
                        productsList = resultObj
                    ),
                    response = null,
                    stateEvent = stateEvent
                )
            }

            override suspend fun updateCache(networkObject: List<Product>) {
                wowDemoDao.insertOrUpdateProducts(products = networkObject)
            }

        }.result
    }


    override fun setProductFavourite(
        stateEvent: StateEvent,
        productId: Int
    ): Flow<DataState<ProductsFragmentViewState>> = flow {

        val product = wowDemoDao.readProduct(productId)
        product.isFavourite = !product.isFavourite
        wowDemoDao.insertOrUpdateProducts(listOf(product))

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            wowDemoDao.readAllProductsLocalStorage()
        }

        emit(
            object : CacheResponseHandler<ProductsFragmentViewState, List<Product>>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: List<Product>): DataState<ProductsFragmentViewState> {
                    return DataState.data(
                        data = ProductsFragmentViewState(
                            productsList = resultObj
                        ),
                        response = null,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )

    }

//    override fun getProductsList(stateEvent: StateEvent): Flow<DataState<ProductsFragmentViewState>> {
//        return flow {
//
//            val apiResult = safeApiCall(gson = gson, dispatcher = Dispatchers.IO) {
//                wowDemoApiService.getProducts()
//            }
//
//            emit(
//                object : ApiResponseHandler<ProductsFragmentViewState, GetProductsResponse>(
//                    response = apiResult,
//                    stateEvent = stateEvent
//                ) {
//                    override suspend fun handleSuccess(resultObj: GetProductsResponse): DataState<ProductsFragmentViewState> {
//                        resultObj.let {
//                            return DataState.data(
//                                stateEvent = stateEvent,
//                                response = null,
//                                data = ProductsFragmentViewState(
//                                    productsList = resultObj.products,
//                                )
//                            )
//                        }
//                    }
//                }.getResult()
//            )
//
//        }
//
//    }

}