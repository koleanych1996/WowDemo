package com.example.wowdemo.repository

import android.content.Context
import android.util.Log
import com.example.wowdemo.Constants.FORBIDDEN_ERROR
import com.example.wowdemo.Constants.NETWORK_ERROR
import com.example.wowdemo.Constants.NOT_FOUND_ERROR
import com.example.wowdemo.Constants.TAG
import com.example.wowdemo.Constants.UNKNOWN_ERROR
import com.example.wowdemo.isConnectedToInternet
import com.example.wowdemo.network.ApiResult
import com.example.wowdemo.viewModel.common.DataState
import com.example.wowdemo.viewModel.common.StateEvent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.system.measureTimeMillis

abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val stateEvent: StateEvent,
    private val apiCall: suspend () -> NetworkObj?,
    private val cacheCall: suspend () -> CacheObj?,
    private val context: Context,
    private val gson: Gson,
    private var handleNotFoundError: (suspend () -> DataState<ViewState>)? = null
) {
    var executionTime = 0L
    val result: Flow<DataState<ViewState>> = flow {
        if (isConnectedToInternet(context)) {
            if (shouldFetchFromNetwork()) {
                val apiResult = safeApiCall(gson, dispatcher) { apiCall.invoke() }

                when (apiResult) {
                    is ApiResult.GenericError -> {
                        emit(
                            buildError(
                                apiResult.errorMessage ?: UNKNOWN_ERROR,
                                stateEvent
                            )
                        )
                    }

                    is ApiResult.NetworkError -> {
                        emit(
                            buildError(
                                NETWORK_ERROR,
                                stateEvent
                            )
                        )
                    }

                    is ApiResult.ForbiddenError -> {
                        emit(
                            buildForbiddenError(
                                FORBIDDEN_ERROR,
                                stateEvent
                            )
                        )
                    }

                    is ApiResult.NotFoundError -> {
                        val newState = if (handleNotFoundError == null) {
                            buildNotFoundError(
                                NOT_FOUND_ERROR,
                                stateEvent
                            )
                        } else {
                            handleNotFoundError!!.invoke()
                        }
                        emit(newState)
                    }
                    is ApiResult.Success -> {
                        if (apiResult.value == null) {
                            emit(
                                buildError(
                                    UNKNOWN_ERROR,
                                    stateEvent
                                )
                            )
                        } else {
                            updateCache(apiResult.value)
                            emit(
                                returnCache()
                            )
                        }
                    }
                }
            } else {
                emit(
                    returnCache()
                )
            }
        } else {
            emit(
                returnCache()
            )
        }
    }

    abstract fun shouldFetchFromNetwork(): Boolean

    abstract suspend fun handleCacheSuccess(resultObj: CacheObj): DataState<ViewState> // make sure to return null for stateEvent

    abstract suspend fun updateCache(networkObject: NetworkObj)

    private suspend fun returnCache(): DataState<ViewState> {
        Log.d(TAG, "reading cache start time = ${Calendar.getInstance().time}")
        val cacheResult = safeCacheCall(dispatcher) {
            cacheCall.invoke()
        }


        Log.d(TAG, "reading cache end time = ${Calendar.getInstance().time}")

        if (cacheResult is CacheResult.GenericError) {
            return buildError(
                message = "Cache Error, reason=${cacheResult.errorMessage}",
                stateEvent = stateEvent
            )
        }

        return object : CacheResponseHandler<ViewState, CacheObj>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: CacheObj): DataState<ViewState> {
                return handleCacheSuccess(resultObj)
            }
        }.getResult()
    }
}