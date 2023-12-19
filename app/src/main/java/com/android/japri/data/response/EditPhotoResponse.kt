package com.android.japri.data.response

import com.google.gson.annotations.SerializedName

data class EditPhotoResponse(
	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)

