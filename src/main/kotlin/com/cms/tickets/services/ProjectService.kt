package com.cms.tickets.services

import com.cms.tickets.domain.Project
import kotlinx.coroutines.flow.Flow

interface ProjectService {
    suspend fun create(project: Project): Project

    fun all(): Flow<Project>

    suspend fun findById(id: Int): Project?

    suspend fun update(id: Int, project: Project): Project?

    suspend fun delete(id: Int)
}