package com.pz.jobito.routes

import com.pz.jobito.model.api.ErrorResponse
import com.pz.jobito.model.api.gitlab.MergeRequestResponse
import com.pz.jobito.service.gitlab.GitlabService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun Route.healthRoutes() {
    val gitlabService = GitlabService()
    
    route("/health") {
        get("/") {
            call.respond(HttpStatusCode.OK, "I'm OK")
        }
    }
}