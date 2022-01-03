package com.lucacorp.todolucas.network

import com.lucacorp.todolucas.tasklist.Task
import okhttp3.MultipartBody

class UserRepository {
    private val userWebService = Api.userWebService

    suspend fun getUserInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateUserAvatar(avatar: MultipartBody.Part) {
        userWebService.updateAvatar(avatar)
    }

    suspend fun updateUserInfo(user: UserInfo) {
        userWebService.update(user)
    }
}