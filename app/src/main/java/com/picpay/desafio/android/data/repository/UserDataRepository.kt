package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper

interface UserDataRepository {
    suspend fun getUsers(): ResultWrapper<List<UserEntity>>
}