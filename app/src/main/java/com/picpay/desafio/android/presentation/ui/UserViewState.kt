package com.picpay.desafio.android.presentation.ui

import com.picpay.desafio.android.domain.model.User

sealed class UserViewState {
    object Loading : UserViewState()

    class ShowData(val users: List<User>) : UserViewState()
    class Error(val message: Int) : UserViewState()
}