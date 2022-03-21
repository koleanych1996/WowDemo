package com.example.wowdemo.repository

import android.content.Context
import com.example.wowdemo.model.GetProductsResponse
import com.example.wowdemo.model.Product
import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.persistance.WowDemoDao
import com.example.wowdemo.viewModel.ProductsFragmentViewState
import com.example.wowdemo.viewModel.common.DataState
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


    override fun getProductsList(
        stateEvent: StateEvent,
        page: Int
    ): Flow<DataState<ProductsFragmentViewState>> {
        return object :
            NetworkBoundResource<GetProductsResponse, List<Product>, ProductsFragmentViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    wowDemoApiService.getProducts(page)
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

            override suspend fun updateCache(networkObject: GetProductsResponse) {

                networkObject.products.forEach { product ->
                    product.page = networkObject.currentPage
                    wowDemoDao.readProduct(product.id)?.let {
                        product.isFavourite = it.isFavourite
                    }
                }
                wowDemoDao.insertOrUpdateProducts(products = networkObject.products)
            }

        }.result
    }


    override fun setProductFavourite(
        stateEvent: StateEvent,
        productId: Int
    ): Flow<DataState<ProductsFragmentViewState>> = flow {

        val product = wowDemoDao.readProduct(productId)
        product?.let {
            product.isFavourite = !product.isFavourite
            wowDemoDao.insertOrUpdateProducts(listOf(product))
        }


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

}


