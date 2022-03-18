package com.example.wowdemo.viewModel

import com.example.wowdemo.Constants
import com.example.wowdemo.model.Product
import com.example.wowdemo.repository.ProductsRepository
import com.example.wowdemo.viewModel.common.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class ProductsFragmentViewModel
@Inject constructor(
    private val productsRepository: ProductsRepository
) : BaseViewModel<ProductsFragmentViewState>() {

    override fun handleNewData(data: ProductsFragmentViewState) {
        data.productsList?.let {
            setProductsList(it)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ProductsFragmentViewState>> =
            when (stateEvent) {
                is ProductsStateEvent.PingProductsStateEvent -> {
                    productsRepository.getProductsList(
                        stateEvent = stateEvent
                    )
                }
                else -> {
                    flow {
                        emit(
                            DataState.error<ProductsFragmentViewState>(
                                response = Response(
                                    message = Constants.INVALID_STATE_EVENT,
                                    messageType = MessageType.Error
                                ),
                                stateEvent = stateEvent
                            )
                        )
                    }
                }
            }
        launchJob(stateEvent = stateEvent, jobFunction = job)
    }

    override fun initNewViewState(): ProductsFragmentViewState {
        return ProductsFragmentViewState()
    }

    private fun setProductsList(productsList: List<Product>) {
        val update = getCurrentViewStateOrNew()
        update.productsList = productsList
        setViewState(update)
    }
}