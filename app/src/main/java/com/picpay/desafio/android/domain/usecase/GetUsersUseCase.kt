package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    fun invoke(): Flow<ResultWrapper<List<User>>>
}