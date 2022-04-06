package com.picpay.desafio.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.ui.UserViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {
    private val _uiViewState = MutableStateFlow<UserViewState>(UserViewState.Loading)
    val uiViewState: StateFlow<UserViewState> = _uiViewState

    init {
        listUsers()
    }

    private fun listUsers() {
        viewModelScope.launch {
            getUsersUseCase.invoke().collect { result ->
                _uiViewState.value = when (result) {
                    ResultWrapper.Loading -> UserViewState.Loading
                    is ResultWrapper.Error -> UserViewState.Error(R.string.error)
                    is ResultWrapper.Success -> UserViewState.ShowData(result.value)
                }
            }
        }
    }
}