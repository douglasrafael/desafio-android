package com.picpay.desafio.android.presentation.viewmodel

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.entity.mapper.toModel
import com.picpay.desafio.android.data.repository.helpers.ErrorResponse
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.ui.UserViewState
import com.picpay.desafio.android.utils.UserDataMock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val getUsersUseCase = mock<GetUsersUseCase>()
    private val usersMock by lazy { UserDataMock.generateUserEntityFake(2).toModel() }

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        viewModel = UserViewModel(getUsersUseCase, dispatcher)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun down() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit the initial state init`() = runTest {
        val initialState = viewModel.viewState.value
        assertTrue(initialState is UserViewState.Init)
    }

    @Test
    fun `should emit the states in the correct order in the happy case`() = runTest {
        whenever(getUsersUseCase.invoke()).thenReturn(flow {
            emit(ResultWrapper.Loading)
            emit(ResultWrapper.Success(usersMock))
        })
        viewModel.viewState.test {
            viewModel.getUsers()

            assertTrue(awaitItem() is UserViewState.Init)
            assertTrue(awaitItem() is UserViewState.Loading)
            assertTrue(awaitItem() is UserViewState.Success)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should emit the states in the correct order in the error case`() = runTest {
        whenever(getUsersUseCase.invoke()).thenReturn(flow {
            emit(ResultWrapper.Loading)
            emit(ResultWrapper.Error())
        })
        viewModel.viewState.test {
            viewModel.getUsers()

            assertTrue(awaitItem() is UserViewState.Init)
            assertTrue(awaitItem() is UserViewState.Loading)
            assertTrue(awaitItem() is UserViewState.Error)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should emit success state with correct values`() = runTest {
        whenever(getUsersUseCase.invoke()).thenReturn(flow {
            emit(ResultWrapper.Loading)
            emit(ResultWrapper.Success(usersMock))
        })

        viewModel.viewState.test {
            viewModel.getUsers()
            delay(100)

            expectMostRecentItem().also { result ->
                assertTrue(result is UserViewState.Success)
                assertEquals(result.users, usersMock)
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit error state with correct values`() = runTest {
        val errorMock = ErrorResponse(503, "Service Unavailable")
        whenever(getUsersUseCase.invoke()).thenReturn(flow {
            emit(ResultWrapper.Loading)
            emit(ResultWrapper.Error(errorMock.code, errorMock))
        })

        viewModel.viewState.test {
            viewModel.getUsers()
            delay(100)

            expectMostRecentItem().also { result ->
                assertTrue(result is UserViewState.Error)
                assertEquals(result.message, errorMock.message)
            }

            cancelAndIgnoreRemainingEvents()
        }
    }
}