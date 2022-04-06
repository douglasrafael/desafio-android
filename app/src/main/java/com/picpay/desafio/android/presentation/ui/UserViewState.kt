package com.picpay.desafio.android.presentation.ui

import androidx.annotation.IdRes
import com.picpay.desafio.android.domain.model.User

sealed class UserViewState {
    class ShowData(val users: List<User>) : UserViewState()
    class Error(val message: Int) : UserViewState()
    object Loading : UserViewState()
}