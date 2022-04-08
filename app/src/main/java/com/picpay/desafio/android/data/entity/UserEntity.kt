package com.picpay.desafio.android.data.entity

import com.google.gson.annotations.SerializedName

data class UserEntity(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("img") val img: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("username") val username: String? = null
)