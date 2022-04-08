package com.picpay.desafio.android.utils

import com.picpay.desafio.android.data.entity.UserEntity

object UserDataMock {
    fun generateUserEntityFake(count: Int = 1): List<UserEntity> =
        (1..count).map { generateUserEntity(it) }

    private fun generateUserEntity(id: Int) = UserEntity(
        id = id,
        img = "https://randomuser.me/api/portraits/men/$id.jpg",
        name = "Name $id",
        username = "Username $id"
    )
}