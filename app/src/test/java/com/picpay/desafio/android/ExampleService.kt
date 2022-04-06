package com.picpay.desafio.android

import com.picpay.desafio.android.data.repository.datasource.network.PicPayService
import com.picpay.desafio.android.domain.model.User

class ExampleService(
    private val service: PicPayService
) {

    fun example(): List<User> {
        val users = service.getUsers().execute()

        return users.body() ?: emptyList()
    }
}