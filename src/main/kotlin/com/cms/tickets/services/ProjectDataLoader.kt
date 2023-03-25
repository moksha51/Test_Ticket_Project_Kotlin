package com.cms.tickets.services

import com.cms.tickets.domain.Project
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProjectDataLoader(private val service: ProjectService) {
    @EventListener(ApplicationReadyEvent::class)
    fun load() = runBlocking {
        logger.debug { "Inserting demo data" }
        (1..5).forEach { i ->
            service.create(Project(name = "Project $i", description = "Lorem ipsum dolor sit amet"))
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}