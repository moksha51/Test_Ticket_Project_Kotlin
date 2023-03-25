package com.cms.tickets.services

import com.cms.tickets.domain.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Service
class InMemoryProjectServiceImpl : ProjectService {

    private val counter = AtomicInteger(0)

    private val projects = ConcurrentHashMap<Int, Project>()
    override suspend fun create(project: Project): Project {
        val id = counter.incrementAndGet()
        val created = project.copy(id = id)
        logger.debug { "Creating project $created" }
        projects[id] = created
        return created
    }

    override fun all(): Flow<Project> = projects
            .values
            .also { projects -> logger.debug { "Return all ${projects.size} projects(s)" } }
            .asFlow()

    override suspend fun findById(id: Int): Project? = projects[id].also { project -> logger.debug { "Returning $project by id:$id" } }

    override suspend fun update(id: Int, project: Project): Project? {
        val updated = project.copy(id = id)
        if (!exists(id)) {
            return null
        }
        projects[id] = updated
        logger.debug { "Project $updated updated" }
        return updated
    }

    private suspend fun exists(id: Int): Boolean = findById(id) != null

    override suspend fun delete(id: Int) {
        projects.remove(id).also { project -> logger.debug { "$project deleted" } }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}