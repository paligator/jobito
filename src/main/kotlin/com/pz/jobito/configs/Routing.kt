package com.pz.jobito.configs

import com.pz.jobito.routes.gitlabRoutes
import com.pz.jobito.routes.healthRoutes
import com.pz.jobito.routes.taskRoutes
import com.pz.jobito.routes.webSocketRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            taskRoutes()
            gitlabRoutes()
            webSocketRoutes()
            healthRoutes()
        }
    }
}