package com.pz.jobito.plugins

import com.pz.jobito.model.Tasks
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.github.cdimascio.dotenv.dotenv

fun configureDatabase() {
    val dotenv = dotenv()
    val config = HikariConfig().apply {
        driverClassName = "com.mysql.cj.jdbc.Driver"
        jdbcUrl = dotenv["DATABASE_URL"]!!
        username = dotenv["DATABASE_USERNAME"]!!
        password = dotenv["DATABASE_PASSWORD"]!!
        maximumPoolSize = 10
        minimumIdle = 2
        connectionTimeout = 30000
        validationTimeout = 5000
    }
    
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    
    transaction {
        SchemaUtils.create(Tasks)
    }
}