package com.example.wowdemo.viewModel

import com.example.wowdemo.Constants
import com.example.wowdemo.model.Product
import com.example.wowdemo.repository.ProductDetailsRepository
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
class ProductDetailsFragmentViewModel
@Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository
) : BaseViewModel<ProductDetailsFragmentViewState>() {

    override fun handleNewData(data: ProductDetailsFragmentViewState) {
        data.product?.let {
            setProduct(it)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ProductDetailsFragmentViewState>> =
            when (stateEvent) {
                is ProductDetailsStateEvent.GetProductStateEvent -> {
                    productDetailsRepository.getProduct(
                        stateEvent = stateEvent,
                        productId = stateEvent.productId
                    )
                }
                else -> {
                    flow {
                        emit(
                            DataState.error<ProductDetailsFragmentViewState>(
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

    override fun initNewViewState(): ProductDetailsFragmentViewState {
        return ProductDetailsFragmentViewState()
    }

    private fun setProduct(product: Product) {
        val update = getCurrentViewStateOrNew()
        update.product = product
        setViewState(update)
    }
}