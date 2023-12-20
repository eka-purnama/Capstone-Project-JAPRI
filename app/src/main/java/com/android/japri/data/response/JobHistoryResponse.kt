package com.android.japri.data.response

import com.google.gson.annotations.SerializedName

data class JobDetailResponse(

	@field:SerializedName("pesan")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<JobHistoryResponseItem?>? = null
)

data class JobHistoryResponseItem(

	@field:SerializedName("end_day")
	val endDay: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("start_day")
	val startDay: String? = null,

	@field:SerializedName("end_time")
	val endTime: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("salary")
	val salary: Int? = null,

	@field:SerializedName("feedback")
	val feedback: Feedback? = null,

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("job_name")
	val jobName: String? = null,

	@field:SerializedName("penyedia_jasa")
	val penyediaJasa: String? = null,

	@field:SerializedName("pengguna_jasa")
	val penggunaJasa: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("photo_url_pengguna_jasa")
	val photoUrlPenggunaJasa: String? = null,

	@field:SerializedName("photo_url_penyedia_jasa")
	val photoUrlPenyediaJasa: String? = null,
)


data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)

data class Feedback(

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null
)