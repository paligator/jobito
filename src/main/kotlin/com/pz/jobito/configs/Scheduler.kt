package com.pz.jobito.configs

import com.pz.jobito.service.gitlab.GitlabService
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import mu.KotlinLogging
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

fun Application.configureScheduler() {
    val scheduler = Scheduler()
    scheduler.startScheduler()

    environment.monitor.subscribe(ApplicationStopping) {
        scheduler.stopScheduler()
    }
}

class Scheduler {
    companion object {
        private val logger = KotlinLogging.logger {}
    }
    
    private val gitlabService = GitlabService()
    private var timer: Timer? = null
    val config = ConfigFactory.load()

    fun startScheduler() {
        scheduleCheckMergeRequest()
    }

    fun stopScheduler() {
        timer?.cancel()
        logger.info { "Scheduler stopped" }
    }

    fun scheduleCheckMergeRequest() {
        val mergeRequestInterval = config.getLong("scheduler.check_merge_request_seconds")
        timer = fixedRateTimer(name = "merge-request-scheduler", daemon = true, initialDelay = 10000L, period = mergeRequestInterval * 1000) {
            try {
                CoroutineScope(Dispatchers.Default).async {
                    gitlabService.checkMergeRequest()
                }
            } catch (e: Exception) {
                logger.error("Error in scheduled merge request check", e)
            }
        }
        logger.info { "Scheduler started with interval of $mergeRequestInterval seconds" }
    }

}