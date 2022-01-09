package com.lucacorp.todolucas.network

import android.util.Log
import android.widget.Toast
import com.lucacorp.todolucas.login.LoginForm
import com.lucacorp.todolucas.login.LoginResponse
import com.lucacorp.todolucas.login.SignUpForm
import com.lucacorp.todolucas.tasklist.Task
import okhttp3.MultipartBody
import retrofit2.Response

class UserRepository {
    private val userWebService = Api.userWebService

    suspend fun refresh(): UserInfo? {
        var response = userWebService.getInfo()
        if(response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part): UserInfo? {
        var response = userWebService.updateAvatar(avatar)
        if(response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun update(user: UserInfo): UserInfo? {
        var response = userWebService.update(user)
        if(response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun login(loginDetails: LoginForm): LoginResponse? {
        var response = userWebService.login(loginDetails)
        Log.e("J'aime me battre", "Avant : ${response.message()}")
        if(response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun signUp(signupDetails: SignUpForm): LoginResponse? {
        var response = userWebService.signUp(signupDetails)
        if(response.isSuccessful) {
            return response.body()
        }
        return null
    }
}