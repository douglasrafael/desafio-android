package com.picpay.desafio.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.ui.UserViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _viewState = MutableStateFlow<UserViewState>(UserViewState.Init)
    val viewState: StateFlow<UserViewState> get() = _viewState

    fun getUsers() {
        viewModelScope.launch(dispatcher) {
            getUsersUseCase.invoke().collect { result ->
                _viewState.emit(
                    when (result) {
                        ResultWrapper.Loading -> UserViewState.Loading
                        is ResultWrapper.Error -> UserViewState.Error(result.error?.message)
                        is ResultWrapper.Success -> UserViewState.Success(result.value)
                    }
                )
            }
        }
    }
}