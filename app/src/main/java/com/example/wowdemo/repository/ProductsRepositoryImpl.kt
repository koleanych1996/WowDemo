package com.example.wowdemo.repository

import android.content.Context
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
        return object : NetworkBoundResource<List<Product>, List<Product>, ProductsFragmentViewState>(
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

}