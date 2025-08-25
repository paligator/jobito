package com.pz.jobito.configs

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway
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
    
    // Run Flyway migrations
    val flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .load()
    
    flyway.migrate()
    
    Database.connect(dataSource)
}