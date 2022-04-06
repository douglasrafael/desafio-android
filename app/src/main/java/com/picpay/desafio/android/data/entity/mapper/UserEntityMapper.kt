package com.picpay.desafio.android.data.entity.mapper

import com.picpay.desafio.android.data.entity.UserEntity
import com.picpay.desafio.android.domain.model.User

fun UserEntity.toModel() = User(id, img, name, username)