package com.picpay.desafio.android.data.repository.datasource.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.data.repository.datasource.UserDataSource
import com.picpay.desafio.android.data.repository.helpers.ErrorResponse
import com.picpay.desafio.android.data.repository.helpers.NetworkHelper
import com.picpay.desafio.android.data.repository.helpers.ResultWrapper
import com.picpay.desafio.android.utils.UserDataMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import kotlin.test.assertEquals
import kotlin.test.fail

@ExperimentalCoroutinesApi
class NetworkUserDataSourceTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val apiMock = mock<PicPayService>()
    private val httpExceptionMock: HttpException = mock()

    private lateinit var dataSource: UserDataSource
    private lateinit var networkHelperMock: NetworkHelper

    @Before
    fun setup() {
        networkHelperMock = NetworkHelper(mock())
        dataSource = NetworkUserDataSourceImpl(apiMock, networkHelperMock, dispatcher)
    }

    @Test
    fun `should return user list successfully`() = runTest {
        val usersMock = UserDataMock.generateUserEntityFake(2)
        whenever(apiMock.getUsers()).thenReturn(usersMock)

        val result = dataSource.users()

        verify(apiMock).getUsers()
        when (result) {
            is ResultWrapper.Success -> {
                usersMock[1].id = 123
                assertEquals(result.value, usersMock)
            }
            else -> fail()
        }
    }

    @Test
    fun `should return empty user list`() = runTest {
        val usersMock = emptyList<UserEntity>()
        whenever(apiMock.getUsers()).thenReturn(emptyList())

        val result = dataSource.users()

        verify(apiMock).getUsers()
        when (result) {
            is ResultWrapper.Success -> assertEquals(result.value, usersMock)
            else -> fail()
        }
    }

    @Test
    fun `should return an error in the users listing`() = runTest {
        val errorMock = ErrorResponse(404, "Not found")
        whenever(httpExceptionMock.code()).thenReturn(errorMock.code)
        whenever(httpExceptionMock.message()).thenReturn(errorMock.message!!)
        whenever(apiMock.getUsers()).thenThrow(httpExceptionMock)

        val result = dataSource.users()

        verify(apiMock).getUsers()
        when (result) {
            is ResultWrapper.Error -> {
                assertEquals(result.code, errorMock.code)
                assertEquals(result.error, errorMock)
            }
            else -> fail()
        }
    }
}