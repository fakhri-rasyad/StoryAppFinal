package com.d121211017.stroyappsubmission.data.remote.entity

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
	@field:SerializedName("error")
	val error: Boolean = true,

	@field:SerializedName("message")
	val message: String? = null
)
