package com.picpay.desafio.android.data.entity.mapper

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.utils.UserDataMock
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.test.assertEquals

class UserEntityMapperTest {

    @Test
    fun testUserEntityToUserModel() {
        val userEntityMock = UserDataMock.generateUserEntityFake(1)[0]
        val user = userEntityMock.toModel()

        assertThat(user, `is`(instanceOf(User::class.java)))
        assertUserAndUserEntity(user, userEntityMock)
    }

    @Test
    fun testListUserEntityToListUserModel() {
        val userEntityMock = UserDataMock.generateUserEntityFake(2)
        val users = userEntityMock.toModel()

        assertEquals(users.size, userEntityMock.size)
        for (index in 0..1) {
            assertThat(users[index], `is`(instanceOf(User::class.java)))
            assertUserAndUserEntity(users[index], userEntityMock[index])
        }
    }

    private fun assertUserAndUserEntity(expected: User, actual: UserEntity) {
        assertEquals(expected = expected.id, actual = actual.id)
        assertEquals(expected = expected.img, actual = actual.img)
        assertEquals(expected = expected.name, actual = actual.name)
        assertEquals(expected = expected.username, actual = actual.username)
    }
}