package com.android.japri.data.request

class RequestBody {

    data class RegisterRequest(
        val email: String,
        val password: String,
        val name: String,
        val username: String,
        val role: String,
    )

    data class LoginRequest(
        val email: String,
        val password: String
    )
}