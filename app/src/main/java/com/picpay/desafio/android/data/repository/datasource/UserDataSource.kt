package com.picpay.desafio.android.data.repository.datasource

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper

interface UserDataSource {
    suspend fun users(): ResultWrapper<List<UserEntity>>
}