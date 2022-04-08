package com.picpay.desafio.android.presentation.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.not
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

    private lateinit var scenario: FragmentScenario<UserListFragment>

    private val getUsersUseCase = mockk<GetUsersUseCase>(relaxed = true)

    private val modulesTest = module {
        viewModel {
            UserViewModel(getUsersUseCase)
        }
    }

    @Before
    fun setup() {
        loadKoinModules(modulesTest)

        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @After
    fun setupDown() {
        unloadKoinModules(modulesTest)
    }

    @Test
    fun shouldDisplayViewWithInitialState() {
        scenario.onFragment { fragment -> fragment.buildViewState(state = UserViewState.Init) }

        onView(withId(R.id.swipeRefreshLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.listProgressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldDisplayTheCorrectTitle() {
        scenario.onFragment { fragment ->
            fragment.buildViewState(state = UserViewState.Success(emptyList()))
        }

        onView(withId(R.id.title)).check(
            matches(allOf(isDisplayed(), withText("Contatos")))
        )
    }

    @Test
    fun shouldOther() {
        val users = listOf(
            User(
                id = 1,
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                name = "Sandrine Spinka",
                username = "Tod86"
            )
        )
        every { getUsersUseCase.invoke() } returns flow {
            emit(ResultWrapper.Loading)
            emit(ResultWrapper.Success(users))
        }
        scenario.onFragment { it._viewModel.getUsers() }
        val recyclerView = withParent(withParent(withId(R.id.recyclerView)))

        // Check the displayed picture
        onView(
            allOf(
                withId(R.id.picture),
                recyclerView,
                isDisplayed()
            )
        ).check(
            matches(
                allOf(
                    isDisplayed(),
                    withContentDescription("Imagem do usuário ${users.first().name}")
                )
            )
        )

        // Check the displayed username
        onView(
            allOf(
                withId(R.id.username),
                recyclerView,
                isDisplayed()
            )
        ).check(matches(withText("${users.first().username}")))

        // Check the displayed name
        onView(
            allOf(
                withId(R.id.name),
                recyclerView,
                isDisplayed()
            )
        ).check(matches(withText("${users.first().name}")))
    }
}