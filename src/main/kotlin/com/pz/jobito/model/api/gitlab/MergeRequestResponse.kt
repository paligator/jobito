package com.pz.jobito.model.api.gitlab

import com.pz.jobito.model.gitlab.MergeRequest
import kotlinx.serialization.Serializable

@Serializable
data class MergeRequestResponse(
    val merge_request_waiting: List<MergeRequest>
)