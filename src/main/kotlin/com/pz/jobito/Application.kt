package com.pz.jobito

import com.pz.jobito.configs.configureDatabase
import com.pz.jobito.configs.configureHTTP
import com.pz.jobito.configs.configureRouting
import com.pz.jobito.configs.configureSerialization
import com.pz.jobito.configs.configureScheduler
import com.pz.jobito.configs.configureWebSocket
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*

fun main(args: Array<String>) {
    loadEnv()
    io.ktor.server.netty.EngineMain.main(args)
}

fun loadEnv() {
    val dotenv = dotenv {
        ignoreIfMissing = true
    }
    dotenv.entries().forEach {
        System.setProperty(it.key, it.value)
    }
}

fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureHTTP()
    configureWebSocket()
    configureRouting()
    configureScheduler()
}
