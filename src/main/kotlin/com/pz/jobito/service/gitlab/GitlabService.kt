package com.pz.jobito.service.gitlab

import com.pz.jobito.model.gitlab.MergeRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GitlabService {
    @Serializable
    private data class GitlabMergeRequest(
        val title: String,
        val web_url: String
    )

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun waitingForMe(): List<MergeRequest> {
        val gitlabBaseUrl = System.getProperty("GITLAB_BASE_URL") 
            ?: throw IllegalStateException("GITLAB_BASE_URL not configured")
        val gitlabUserId = System.getProperty("GITLAB_USER_ID") 
            ?: throw IllegalStateException("GITLAB_USER_ID not configured")
        val gitlabToken = System.getProperty("GIT_LAB_ACCESS_TOKEN") 
            ?: throw IllegalStateException("GIT_LAB_ACCESS_TOKEN not configured")

        val url = "$gitlabBaseUrl/api/v4/merge_requests?state=opened&reviewer_id=$gitlabUserId"
        
        val httpResponse: HttpResponse = client.get(url) {
            header("Authorization", "Bearer $gitlabToken")
        }

        if (httpResponse.status == HttpStatusCode.OK) {
            val response: List<GitlabMergeRequest> = httpResponse.body()
            return response.map { 
                MergeRequest(
                    name = it.title,
                    url = it.web_url
                )
            }
        } else {
            val errorBody = httpResponse.bodyAsText()
            throw RuntimeException("GitLab API error: ${httpResponse.status.value} - ${httpResponse.status.description}. Response: $errorBody")
        }
    }
}