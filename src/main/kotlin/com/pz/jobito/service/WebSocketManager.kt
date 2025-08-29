package com.pz.jobito.service

import com.pz.jobito.model.gitlab.MergeRequest
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

@Serializable
data class WebSocketMessage(
    val type: String,
    val data: List<MergeRequest>
)

object WebSocketManager {
    private val logger = KotlinLogging.logger {}
    private val connections = ConcurrentHashMap<String, WebSocketSession>()
    
    fun addConnection(id: String, session: WebSocketSession) {
        connections[id] = session
        logger.info { "WebSocket connection added: $id. Total connections: ${connections.size}" }
    }
    
    fun removeConnection(id: String) {
        connections.remove(id)
        logger.info { "WebSocket connection removed: $id. Total connections: ${connections.size}" }
    }
    
    suspend fun broadcastMergeRequests(mergeRequests: List<MergeRequest>) {
        if (mergeRequests.isEmpty()) return
        
        val message = WebSocketMessage(
            type = "merge_requests_update",
            data = mergeRequests
        )
        
        val jsonMessage = Json.encodeToString(message)
        logger.info { "Broadcasting merge requests to ${connections.size} connections: ${mergeRequests.size} merge requests found" }
        
        val disconnectedConnections = mutableListOf<String>()
        
        connections.forEach { (id, session) ->
            try {
                session.send(Frame.Text(jsonMessage))
            } catch (e: ClosedReceiveChannelException) {
                logger.warn { "Connection $id is closed, removing from active connections" }
                disconnectedConnections.add(id)
            } catch (e: Exception) {
                logger.error("Error sending message to connection $id", e)
                disconnectedConnections.add(id)
            }
        }
        
        disconnectedConnections.forEach { removeConnection(it) }
    }
}