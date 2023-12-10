package com.android.japri.data.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.response.RegisterResponse
import com.android.japri.data.retrofit.ApiService
import com.android.japri.data.request.RequestBody
import com.android.japri.data.response.LoginResponse
import com.android.japri.preferences.UserPreference
import com.android.japri.preferences.UserSessionData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AppRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    context: Context
) {

    val error = context.getString(R.string.connection_failed)

    fun registerAccount(registerRequestBody: RequestBody.RegisterRequest) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(registerRequestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(error))
        }
    }

    fun loginAccount(loginRequestBody: RequestBody.LoginRequest) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(loginRequestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(error))
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
