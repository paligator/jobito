package com.pz.jobito.plugins

import com.pz.jobito.routes.gitlabRoutes
import com.pz.jobito.routes.taskRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        taskRoutes()
        gitlabRoutes()
    }
}