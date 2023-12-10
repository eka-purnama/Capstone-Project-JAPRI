package com.android.japri.data.retrofit

import com.android.japri.data.request.RequestBody
import com.android.japri.data.response.LoginResponse
import com.android.japri.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body requestBody: RequestBody.RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body requestBody: RequestBody.LoginRequest
    ): LoginResponse

}