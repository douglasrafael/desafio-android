package com.picpay.desafio.android.domain.model

import org.junit.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun `should accept empty constructor`() {
        assertEquals(User(), User())
    }

    @Test
    fun `should have all expected attributes and correct values`() {
        val user = User(1, "img", "name", "username")
        assertEquals(user.id, 1)
        assertEquals(user.img, "img")
        assertEquals(user.name, "name")
        assertEquals(user.username, "username")
    }
}