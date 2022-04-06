package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.repository.datasource.UserDataSource
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper

class UserDataRepositoryImpl(private val remoteDataSource: UserDataSource) : UserDataRepository {
    override suspend fun getUsers(): ResultWrapper<List<UserEntity>> = remoteDataSource.users()
}