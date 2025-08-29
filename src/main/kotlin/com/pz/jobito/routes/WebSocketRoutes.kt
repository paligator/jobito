package com.pz.jobito.routes

import com.pz.jobito.service.WebSocketManager
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

fun Route.webSocketRoutes() {
    webSocket("/merge-requests") {
        val connectionId = UUID.randomUUID().toString()
        logger.info { "New WebSocket connection established: $connectionId" }
        
        try {
            WebSocketManager.addConnection(connectionId, this)
            
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        logger.debug { "Received message from $connectionId: $text" }
                    }
                    is Frame.Close -> {
                        logger.info { "WebSocket connection closed: $connectionId" }
                        break
                    }
                    else -> {
                        logger.debug { "Received frame type: ${frame.frameType}" }
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error in WebSocket connection $connectionId", e)
        } finally {
            WebSocketManager.removeConnection(connectionId)
            logger.info { "WebSocket connection cleanup completed: $connectionId" }
        }
    }
}