package com.picpay.desafio.android.presentation.ui

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.ui.robot.userListRobot
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class UserListFragmentTest {

    private val getUsersUseCase = mockk<GetUsersUseCase>(relaxed = true)

    private val usersMock = listOf(
        User(
            id = 1,
            img = "https://randomuser.me/api/portraits/men/1.jpg",
            name = "Sandrine Spinka",
            username = "Tod86"
        )
    )

    private val modulesTest = module {
        viewModel {
            UserViewModel(getUsersUseCase)
        }
    }

    @Before
    fun setup() {
        loadKoinModules(modulesTest)
    }

    @After
    fun setupDown() {
        unloadKoinModules(modulesTest)
    }

    @Test
    fun shouldDisplayViewWithInitialState() {
        userListRobot {
            setUiState(UserViewState.Init)
            checkInitialState()
        }
    }

    @Test
    fun shouldDisplayViewWithLoadingState() {
        userListRobot {
            setUiState(UserViewState.Loading)
            checkLoadingState()
        }
    }

    @Test
    fun shouldDisplayViewWithSuccessState() {
        userListRobot {
            setUiState(UserViewState.Success(usersMock))
            checkSuccessState()
        }
    }

    @Test
    fun shouldDisplayViewWithErrorState() {
        val errorMessage = "Error Test"
        userListRobot {
            setUiState(UserViewState.Error(errorMessage))
            checkErrorState()
        }
    }

    @Test
    fun shouldDisplayTheCorrectTitle() {
        userListRobot {
            setUiState(UserViewState.Success(usersMock))
            checkTitle("Contatos")
        }
    }

    @Test
    fun shouldDisplayItemsInRecyclerView() {
        every { getUsersUseCase.invoke() } returns flow {
            emit(ResultWrapper.Loading)
            emit(ResultWrapper.Success(usersMock))
        }

        Thread.sleep(100)
        userListRobot {
            checkPictureInRv("Imagem do usuário ${usersMock.first().name}")
            checkUsernameInRv("${usersMock.first().username}")
            checkNameInRv("${usersMock.first().name}")
        }
    }
}