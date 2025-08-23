package com.pz.jobito.model.gitlab

import kotlinx.serialization.Serializable

@Serializable
data class MergeRequest(
    val name: String,
    val url: String
)