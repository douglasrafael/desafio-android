package com.picpay.desafio.android.di

import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetUsersUseCaseImpl
import org.koin.dsl.module

val userCaseModule = module {
    single<GetUsersUseCase> { GetUsersUseCaseImpl(get()) }
}