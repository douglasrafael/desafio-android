package com.picpay.desafio.android.presentation.ui

import com.picpay.desafio.android.domain.model.User

sealed class UserViewState {
    object Init : UserViewState()
    object Loading : UserViewState()

    class Success(val users: List<User>) : UserViewState()
    class Error(val message: String?) : UserViewState()
}