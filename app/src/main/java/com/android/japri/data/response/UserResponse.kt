package com.android.japri.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("personal_data")
	val personalData: PersonalData? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class PersonalData(

	@field:SerializedName("skill")
	val skill: List<String?>? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null
)

data class Rating(

	@field:SerializedName("totalFeedback")
	val totalFeedback: Int? = null,

	@field:SerializedName("averageRating")
	val averageRating: Any? = null
)
