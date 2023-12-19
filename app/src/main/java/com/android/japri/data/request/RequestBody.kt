package com.android.japri.data.request

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("role")
    val role: String,
)

data class LoginRequestBody(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class AccountRequestBody(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("personal_data")
    val personalData: PersonalDataRequest? = null,
)

data class PersonalDataRequest (
    @field:SerializedName("skill")
    val skill: Array<String>? = null,

    @field:SerializedName("deskripsi")
    val description: String? = null
)

data class JobHistoryRequestBody (
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("username")
    val username: String
)

data class AddJobRequestBody(
    @field:SerializedName("job_name")
    val jobName: String,

    @field:SerializedName("start_day")
    val startDay: String,

    @field:SerializedName("end_day")
    val endDay: String,

    @field:SerializedName("start_time")
    val startTime: String,

    @field:SerializedName("end_time")
    val endTime: String,

    @field:SerializedName("salary")
    val salary: Int,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("pengguna_jasa")
    val client: String,

    @field:SerializedName("penyedia_jasa")
    val serviceProvider: String,

    @field:SerializedName("feedback")
    val feedback: Feedback? = null
)

data class FeedbackRequestBody (
    @field:SerializedName("feedback")
    val feedback: Feedback? = null
)

data class Feedback (
    @field:SerializedName("comment")
    val comment: String? = null,

    @field:SerializedName("rating")
    val rating: Int? = null
)