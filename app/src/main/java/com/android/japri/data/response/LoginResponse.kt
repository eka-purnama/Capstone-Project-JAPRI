package com.android.japri.data.response

data class LoginResponse(
	val message: String? = null,
	val token: String? = null
)

//data class LoginResponse(
//	val data: List<DataLogin?>? = null,
//	val message: String? = null,
//	val error: Boolean? = null
//)
//
//data class DataLogin(
//	val id: Long? = null,
//	val email: String? = null,
//	val role: String? = null,
//	val token: String? = null
//)