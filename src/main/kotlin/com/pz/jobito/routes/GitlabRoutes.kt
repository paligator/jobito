package com.pz.jobito.routes

import com.pz.jobito.model.api.ErrorResponse
import com.pz.jobito.model.api.gitlab.MergeRequestResponse
import com.pz.jobito.service.gitlab.GitlabService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.gitlabRoutes() {
    val gitlabService = GitlabService()
    
    route("/gitlab") {
        get("/waiting-for-me") {
            try {
                val mergeRequests = gitlabService.waitingForMe()
                val response = MergeRequestResponse(merge_request_waiting = mergeRequests)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.application.environment.log.error("Error fetching GitLab merge requests", e)
                val errorResponse = ErrorResponse(
                    status = "error",
                    message = "Failed to fetch merge requests from GitLab",
                    details = e.message
                )
                call.respond(HttpStatusCode.InternalServerError, errorResponse)
            }
        }
    }
}