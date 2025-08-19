package com.pz.jobito.routes

import com.pz.jobito.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun Route.taskRoutes() {
    route("/tasks") {
        get {
            val tasks = transaction {
                TaskEntity.all().map { taskEntity ->
                    Task(
                        id = taskEntity.id.value,
                        title = taskEntity.title,
                        description = taskEntity.description,
                        completed = taskEntity.completed,
                        createdAt = taskEntity.createdAt.toString(),
                        updatedAt = taskEntity.updatedAt.toString()
                    )
                }
            }
            call.respond(tasks)
        }
        
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
                return@get
            }
            
            val task = transaction {
                TaskEntity.findById(id)?.let { taskEntity ->
                    Task(
                        id = taskEntity.id.value,
                        title = taskEntity.title,
                        description = taskEntity.description,
                        completed = taskEntity.completed,
                        createdAt = taskEntity.createdAt.toString(),
                        updatedAt = taskEntity.updatedAt.toString()
                    )
                }
            }
            
            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
        
        post {
            val request = call.receive<CreateTaskRequest>()
            val task = transaction {
                val taskEntity = TaskEntity.new {
                    title = request.title
                    description = request.description
                    completed = false
                    createdAt = LocalDateTime.now()
                    updatedAt = LocalDateTime.now()
                }
                Task(
                    id = taskEntity.id.value,
                    title = taskEntity.title,
                    description = taskEntity.description,
                    completed = taskEntity.completed,
                    createdAt = taskEntity.createdAt.toString(),
                    updatedAt = taskEntity.updatedAt.toString()
                )
            }
            call.respond(HttpStatusCode.Created, task)
        }
        
        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
                return@put
            }
            
            val request = call.receive<UpdateTaskRequest>()
            val task = transaction {
                val taskEntity = TaskEntity.findById(id)
                if (taskEntity != null) {
                    request.title?.let { taskEntity.title = it }
                    request.description?.let { taskEntity.description = it }
                    request.completed?.let { taskEntity.completed = it }
                    taskEntity.updatedAt = LocalDateTime.now()
                    
                    Task(
                        id = taskEntity.id.value,
                        title = taskEntity.title,
                        description = taskEntity.description,
                        completed = taskEntity.completed,
                        createdAt = taskEntity.createdAt.toString(),
                        updatedAt = taskEntity.updatedAt.toString()
                    )
                } else null
            }
            
            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
        
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
                return@delete
            }
            
            val deleted = transaction {
                val taskEntity = TaskEntity.findById(id)
                if (taskEntity != null) {
                    taskEntity.delete()
                    true
                } else false
            }
            
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
    }
}