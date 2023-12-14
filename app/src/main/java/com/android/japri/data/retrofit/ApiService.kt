package com.android.japri.data.retrofit

import com.android.japri.data.request.RequestBody
import com.android.japri.data.response.EditAccountResponse
import com.android.japri.data.response.EditPhotoResponse
import com.android.japri.data.response.LoginResponse
import com.android.japri.data.response.RegisterResponse
import com.android.japri.data.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body requestBody: RequestBody.RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body requestBody: RequestBody.LoginRequest
    ): LoginResponse

    @Multipart
    @PUT("users/edit-photo/{id}")
    suspend fun editPhotoProfile(
        @Path("id") id: String,
        @Part file: MultipartBody.Part,
    ): EditPhotoResponse

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): UserResponse

    @GET("users/{id}")
    suspend fun getUserById2(
        @Path("id") id: String
    ): UserResponse

    @PUT("users/edit-profile/{id}")
    suspend fun editAccount(
        @Path("id") id: String,
        @Body requestBody: RequestBody.AccountRequest
    ): EditAccountResponse

}