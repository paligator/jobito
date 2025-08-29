package com.pz.jobito.service.gitlab

import com.pz.jobito.model.gitlab.MergeRequest
import com.pz.jobito.configs.HttpClientApp
import com.pz.jobito.service.WebSocketManager
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GitlabService {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Serializable
    private data class GitlabMergeRequest(
        val title: String,
        val web_url: String,
        val author: GitlabAuthor,
        val created_at: String
    )

    @Serializable
    private data class GitlabAuthor(
        val name: String
    )

    suspend fun waitingForMe(): List<MergeRequest> {
        val gitlabBaseUrl = System.getProperty("GITLAB_BASE_URL") 
        val gitlabUserId = System.getProperty("GITLAB_USER_ID")
        val gitlabToken = System.getProperty("GIT_LAB_ACCESS_TOKEN")
        val url = "$gitlabBaseUrl/api/v4/merge_requests?state=opened&reviewer_id=$gitlabUserId"
        
        val httpResponse: HttpResponse = HttpClientApp.client.get(url) {
            header("Authorization", "Bearer $gitlabToken")
        }

        if (httpResponse.status == HttpStatusCode.OK) {
            val response: List<GitlabMergeRequest> = httpResponse.body()
            logger.info { "Successfully retrieved ${response.size} merge requests from GitLab" }
            return response.map { 
                MergeRequest(
                    name = it.title,
                    url = it.web_url,
                    authorName = it.author.name,
                    createdAt = LocalDateTime.parse(it.created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                )
            }
        } else {
            val errorBody = httpResponse.bodyAsText()
            logger.error { "GitLab API error: ${httpResponse.status.value} - ${httpResponse.status.description}. Response: $errorBody" }
            throw RuntimeException("GitLab API error: ${httpResponse.status.value} - ${httpResponse.status.description}. Response: $errorBody")
        }
    }

    suspend fun checkMergeRequest() {
        logger.info { "Checking merge requests..." }
        try {
            val mergeRequests = this.waitingForMe()
            if (mergeRequests.isNotEmpty()) {
                logger.info { "Found ${mergeRequests.size} merge requests waiting for review" }
                WebSocketManager.broadcastMergeRequests(mergeRequests)
            } else {
                logger.debug { "No merge requests waiting for review" }
            }
        } catch (e: Exception) {
            logger.error("Error checking merge requests", e)
        }
    }
}