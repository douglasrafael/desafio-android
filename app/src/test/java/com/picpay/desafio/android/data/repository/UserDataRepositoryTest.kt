package com.picpay.desafio.android.data.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.repository.datasource.UserDataSource
import com.picpay.desafio.android.data.repository.helpers.ErrorResponse
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.utils.UserDataMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@ExperimentalCoroutinesApi
class UserDataRepositoryTest {

    private var dataSourceMock = mock<UserDataSource>()
    private lateinit var repository: UserDataRepository

    @Before
    fun setup() {
        repository = UserDataRepositoryImpl(dataSourceMock)
    }

    @Test
    fun `should return empty user list`() = runTest {
        val usersMock = emptyList<UserEntity>()
        whenever(dataSourceMock.users()).thenReturn(ResultWrapper.Success(emptyList()))

        val result = repository.getUsers()

        verify(dataSourceMock).users()
        when (result) {
            is ResultWrapper.Success -> assertEquals(result.value, usersMock)
            else -> fail()
        }
    }

    @Test
    fun `should return user list successfully`() = runTest {
        val usersMock = UserDataMock.generateUserEntityFake(2)
        whenever(dataSourceMock.users()).thenReturn(ResultWrapper.Success(usersMock))

        val result = repository.getUsers()

        verify(dataSourceMock).users()
        when (result) {
            is ResultWrapper.Success -> assertEquals(result.value, usersMock)
            else -> fail()
        }
    }

    @Test
    fun `should return an error in the users listing`() = runTest {
        val errorMock = ErrorResponse(404, "Not found")
        whenever(dataSourceMock.users()).thenReturn(ResultWrapper.Error(errorMock.code, errorMock))

        val result = repository.getUsers()

        verify(dataSourceMock).users()
        when (result) {
            is ResultWrapper.Error -> {
                assertEquals(result.code, errorMock.code)
                assertEquals(result.error, errorMock)
            }
            else -> fail()
        }
    }
}