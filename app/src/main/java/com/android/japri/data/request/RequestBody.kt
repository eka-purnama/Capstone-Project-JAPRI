package com.android.japri.data.request

class RequestBody {

    data class RegisterRequest(
        val email: String,
        val password: String,
        val username: String,
        val role: String,
    )

    data class LoginRequest(
        val email: String,
        val password: String
    )

    data class AccountRequest(
        val email: String? = null,
        val name: String? = null,
        val password: String? = null,
        val phone_number: String? = null,
        val gender: String? = null,
        val address: String? = null,
        val personal_data: PersonalDataRequest? = null,
    )

    data class PersonalDataRequest(
        val skill: Array<String>? = null,
        val description: String? = null
    )
}