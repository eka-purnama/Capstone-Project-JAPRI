package com.android.japri.data.response

import com.google.gson.annotations.SerializedName

data class JobHistoryResponse(

	@field:SerializedName("JobHistoryResponse")
	val jobHistoryResponse: List<JobHistoryResponseItem?>? = null
)

data class JobHistoryDetailResponse(

	@field:SerializedName("formJasaData")
	val formJasaData: JobHistoryResponseItem? = null
)

data class JobHistoryResponseItem(

	@field:SerializedName("end_day")
	val end_day: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("start_day")
	val start_day: String? = null,

	@field:SerializedName("end_time")
	val end_time: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("feedbacks")
	val feedbacks: Feedbacks? = null,

	@field:SerializedName("salary")
	val salary: Int? = null,

	@field:SerializedName("start_time")
	val start_time: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("pemberi_jasa")
	val pemberi_jasa: String? = null,

	@field:SerializedName("job_name")
	val job_name: String? = null,

	@field:SerializedName("penyedia_jasa")
	val penyedia_jasa: String? = null,

	@field:SerializedName("pengguna_jasa")
	val pengguna_jasa: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val _nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val _seconds: Long? = null
)

data class Feedbacks(
	val any: Any? = null
)
