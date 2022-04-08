package com.picpay.desafio.android.domain.usecase

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.entity.mapper.toModel
import com.picpay.desafio.android.data.repository.UserDataRepository
import com.picpay.desafio.android.data.repository.helpers.ErrorResponse
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.utils.UserDataMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetUsersUseCaseTest {
    private val repoMock = mock<UserDataRepository>()
    private val usersMock by lazy { UserDataMock.generateUserEntityFake(2) }

    private lateinit var useCase: GetUsersUseCase

    @Before
    fun setup() {
        useCase = GetUsersUseCaseImpl(repoMock)
    }

    @Test
    fun `should emit loading and success states`() = runTest {
        whenever(repoMock.getUsers()).thenReturn(ResultWrapper.Success(emptyList()))

        useCase.invoke().test {
            assertTrue(awaitItem() is ResultWrapper.Loading)
            assertTrue(awaitItem() is ResultWrapper.Success)
            awaitComplete()
        }
    }

    @Test
    fun `should emit success state with correct values`() = runTest {
        whenever(repoMock.getUsers()).thenReturn(ResultWrapper.Success(usersMock))

        // ResultWrapper.Success
        useCase.invoke().last().run {
            assertTrue(this is ResultWrapper.Success)
            assertThat(this.value[0], `is`(instanceOf(User::class.java)))
            assertEquals(this.value, usersMock.toModel())
        }
    }

    @Test
    fun `should emit loading and error states`() = runTest {
        whenever(repoMock.getUsers()).thenReturn(ResultWrapper.Error())

        useCase.invoke().test {
            assertTrue(awaitItem() is ResultWrapper.Loading)
            assertTrue(awaitItem() is ResultWrapper.Error)
            awaitComplete()
        }
    }

    @Test
    fun `should emit error state with correct values`() = runTest {
        val errorMock = ErrorResponse(404, "Not found")
        whenever(repoMock.getUsers()).thenReturn(ResultWrapper.Error(errorMock.code, errorMock))

        // ResultWrapper.Error
        when (val result = useCase.invoke().last()) {
            is ResultWrapper.Error -> {
                assertEquals(result.code, errorMock.code)
                assertEquals(result.error, errorMock)
            }
        }
    }
}