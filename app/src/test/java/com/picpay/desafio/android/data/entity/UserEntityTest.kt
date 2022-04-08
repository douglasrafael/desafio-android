package com.picpay.desafio.android.data.entity

import org.junit.Test
import org.junit.Assert.assertEquals

class UserEntityTest {
    @Test
    fun `should accept empty constructor`() {
        assertEquals(UserEntity(), UserEntity())
    }

    @Test
    fun `should have all expected attributes and correct values`() {
        val userEntity = UserEntity(1, "img", "name", "username")
        assertEquals(userEntity.id, 1)
        assertEquals(userEntity.img, "img")
        assertEquals(userEntity.name, "name")
        assertEquals(userEntity.username, "username")
    }
}