package com.android.japri.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("role")
	val role: String? = null,
)