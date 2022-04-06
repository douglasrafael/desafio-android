package com.picpay.desafio.android.data.repository.datasource.network

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.repository.datasource.UserDataSource
import com.picpay.desafio.android.data.repository.helpers.NetworkHelper
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class NetworkUserDataSourceImpl(
    private val api: PicPayService,
    private val networkHelper: NetworkHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {
    override suspend fun users(): ResultWrapper<List<UserEntity>> = networkHelper
        .safeApiCall(dispatcher) { api.getUsers() }
}