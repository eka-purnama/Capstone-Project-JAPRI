package com.android.japri.data.retrofit

import com.android.japri.data.request.AccountRequestBody
import com.android.japri.data.request.AddJobRequestBody
import com.android.japri.data.request.EditPasswordRequestBody
import com.android.japri.data.request.FeedbackRequestBody
import com.android.japri.data.request.JobHistoryRequestBody
import com.android.japri.data.request.LoginRequestBody
import com.android.japri.data.request.RegisterRequestBody
import com.android.japri.data.response.CommonResponse
import com.android.japri.data.response.JobHistoryDetailResponse
import com.android.japri.data.response.JobHistoryResponseItem
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
        @Body requestBody: RegisterRequestBody
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody
    ): LoginResponse

    @Multipart
    @PUT("users/edit-photo/{id}")
    suspend fun editPhotoProfile(
        @Path("id") id: String,
        @Part file: MultipartBody.Part,
    ): CommonResponse

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): UserResponse

    @PUT("users/edit-profile/{id}")
    suspend fun editAccount(
        @Path("id") id: String,
        @Body requestBody: AccountRequestBody
    ): CommonResponse

    @POST("users/get-status-data")
    suspend fun getJobHistory(
        @Body requestBody: JobHistoryRequestBody
    ): List<JobHistoryResponseItem>

    @GET("jasa/{id}")
    suspend fun getJobHistoryDetail(
        @Path("id") id: String
    ): JobHistoryDetailResponse

    @POST("jasa")
    suspend fun addJob(
        @Body requestBody: AddJobRequestBody
    ): CommonResponse

    @PUT("jasa/{id}")
    suspend fun finishTheJob(
        @Path("id") id: String,
        @Body requestBody: FeedbackRequestBody
    ): CommonResponse

    @PUT("users/edit-password/{id}")
    suspend fun editPassword(
        @Path("id") id: String,
        @Body requestBody: EditPasswordRequestBody
    ): CommonResponse
}