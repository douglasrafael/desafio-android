package com.picpay.desafio.android.data.repository.datasource.network

import com.picpay.desafio.android.data.entity.UserEntity
import retrofit2.http.GET

interface PicPayService {
    @GET("users")
    suspend fun getUsers(): List<UserEntity>
}