package com.pz.jobito.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String,
    val message: String,
    val details: String? = null
)