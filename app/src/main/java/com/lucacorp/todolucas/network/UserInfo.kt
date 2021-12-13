package com.lucacorp.todolucas.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String,
    @SerialName("avatar")
    val avatar: String = "https://cdn.discordapp.com/avatars/188385753686999040/164e4788ec23ffc3e58fe39cb3451694.png"
)
