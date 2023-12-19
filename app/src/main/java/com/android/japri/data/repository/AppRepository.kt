package com.android.japri.data.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.AccountRequestBody
import com.android.japri.data.request.AddJobRequestBody
import com.android.japri.data.request.FeedbackRequestBody
import com.android.japri.data.request.JobHistoryRequestBody
import com.android.japri.data.request.LoginRequestBody
import com.android.japri.data.request.RegisterRequestBody
import com.android.japri.data.response.RegisterResponse
import com.android.japri.data.retrofit.ApiService
import com.android.japri.data.response.EditAccountResponse
import com.android.japri.data.response.EditPhotoResponse
import com.android.japri.data.response.LoginResponse
import com.android.japri.preferences.UserPreference
import com.android.japri.preferences.UserSessionData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class AppRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    context: Context
) {
    private val errorMessage = context.getString(R.string.error_message)
    private val connectionError = context.getString(R.string.connection_failed)

    fun registerAccount(requestBody: RegisterRequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun loginAccount(requestBody: LoginRequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    suspend fun saveSession(user: UserSessionData) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserSessionData> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun editPhotoProfile(id: String, imageFile: File) = liveData {
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.editPhotoProfile(id, multipartBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditPhotoResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun getUserById(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getUserById(id)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            emit(ResultState.Error(errorMessage))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun editAccount(id: String, requestBody: AccountRequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.editAccount(id, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditAccountResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun getJobHistory(requestBody: JobHistoryRequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getJobHistory(requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            emit(ResultState.Error(errorMessage))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun getJobHistoryDetail(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getJobHistoryDetail(id)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            emit(ResultState.Error(errorMessage))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun addJob(requestBody: AddJobRequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.addJob(requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditAccountResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }

    fun finishTheJob(id: String, requestBody: FeedbackRequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.finishTheJob(id, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditAccountResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(connectionError))
        }
    }


    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            context: Context
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, userPreference, context)
            }.also { instance = it }
    }
}


