package com.pz.jobito.configs

import com.pz.jobito.model.api.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureAuthentication() {
    intercept(ApplicationCallPipeline.Call) {
        val apiKey = call.request.headers["API_KEY"]
        val expectedApiKey = System.getProperty("API_KEY")

        if (expectedApiKey == null) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    status = "error",
                    message = "API key not configured on server",
                    details = "Contact administrator to configure API_KEY"
                )
            )
            return@intercept finish()
        }

        if (apiKey == null || apiKey != expectedApiKey) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(
                    status = "error",
                    message = "Invalid or missing API key",
                    details = "Include valid API key in Authorization header"
                )
            )
            return@intercept finish()
        }
    }
}