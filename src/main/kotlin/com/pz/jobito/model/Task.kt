package com.pz.jobito.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Tasks : LongIdTable() {
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val completed = bool("completed").default(false)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class TaskEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskEntity>(Tasks)
    
    var title by Tasks.title
    var description by Tasks.description
    var completed by Tasks.completed
    var createdAt by Tasks.createdAt
    var updatedAt by Tasks.updatedAt
}

@Serializable
data class Task(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val completed: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class CreateTaskRequest(
    val taskName: String,
    val description: String? = null
)

@Serializable
data class UpdateTaskRequest(
    val title: String? = null,
    val description: String? = null,
    val completed: Boolean? = null
)