package com.pz.jobito.configs

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway

private val logger = KotlinLogging.logger {}

fun configureDatabase() {
     logger.info { "ConfigureDatabase - start"}
     val config = ConfigFactory.load()

    val dbConfig = HikariConfig().apply {
        driverClassName = "com.mysql.cj.jdbc.Driver"
        jdbcUrl = config.getString("database.url")
        username = config.getString("database.user")
        password = config.getString("database.password")
        maximumPoolSize = 10
        minimumIdle = 2
        connectionTimeout = 30000
        validationTimeout = 5000
    }
    
    val dataSource = HikariDataSource(dbConfig)

    logger.info { "ConfigureDatabase - run Flyway"}

    // Run Flyway migrations
    val flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .load()
    
    flyway.migrate()
    
    Database.connect(dataSource)

    logger.info { "ConfigureDatabase - end"}
}