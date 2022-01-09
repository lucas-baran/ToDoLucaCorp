package com.lucacorp.todolucas.network

import com.lucacorp.todolucas.login.LoginForm
import com.lucacorp.todolucas.login.LoginResponse
import com.lucacorp.todolucas.login.SignUpForm
import com.lucacorp.todolucas.tasklist.Task
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>

    @POST("users/sign_up")
    suspend fun signUp(@Body user: SignUpForm): Response<LoginResponse>
}