package com.android.japri.data.response

data class UserResponse(
	val password: String? = null,
	val role: String? = null,
	val address: String? = null,
	val gender: String? = null,
	val personal_data: PersonalData? = null,
	val name: String? = null,
	val phone_number: String? = null,
	val id: String? = null,
	val photo_url: String? = null,
	val email: String? = null,
	val username: String? = null
)

data class PersonalData(
	val skill: List<Any?>? = null,
	val deskripsi: String? = null
)

