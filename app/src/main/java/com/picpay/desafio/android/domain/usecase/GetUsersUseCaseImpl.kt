package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.entity.mapper.toModel
import com.picpay.desafio.android.data.repository.UserDataRepository
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsersUseCaseImpl(private val repository: UserDataRepository) : GetUsersUseCase {
    override fun invoke(): Flow<ResultWrapper<List<User>>> = flow {
        emit(ResultWrapper.Loading)
        when (val result = repository.getUsers()) {
            is ResultWrapper.Success -> emit(ResultWrapper.Success(result.value.toModel()))
            is ResultWrapper.Error -> emit(ResultWrapper.Error(result.code, result.error))
        }
    }
}