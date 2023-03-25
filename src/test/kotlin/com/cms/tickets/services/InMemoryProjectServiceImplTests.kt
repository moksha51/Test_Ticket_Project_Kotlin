package com.cms.tickets.services

import com.cms.tickets.domain.Project
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class InMemoryProjectServiceImplTests {
    @Test
    fun `zero projects`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val all = service.all()
        assertThat(all.toList()).hasSize(0)
    }

    @Test
    fun `create one project`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val created = service.create(Project(name = "Test", description = "Test"))
        val all = service.all()
        assertThat(all.first()).isEqualTo(created)
    }

    @Test
    fun `create several projects`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        (1..5).forEach { i ->
            val project = Project(name = "Test $i", description = "Test")
            service.create(project)
        }
        val all = service.all()
        assertThat(all.toList()).hasSize(5)
    }

    @Test
    fun `find by id`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val created = service.create(Project(name = "Test", description = "Test"))
        val stored = service.findById(created.id!!)
        assertThat(stored).isNotNull
        assertThat(stored).isEqualTo(created)
    }

    @Test
    fun `not existing`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val nonExisting = service.findById(1)
        assertThat(nonExisting).isNull()
    }

    @Test
    fun `update existing`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val created = service.create(Project(name = "Test", description = "Test"))
        val updated = service.update(created.id!!, created.copy(name = "Updated"))
        assertThat(updated).isNotNull
        assertThat(updated!!.name).isEqualTo("Updated")
        assertThat(updated).isEqualTo(service.findById(updated.id!!))

    }

    @Test
    fun `update non existing`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val updated = service.update(1, Project(name = "Test", description = "Test"))
        assertThat(updated).isNull()
    }

    @Test
    fun `delete existing`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        val created = service.create(Project(name = "Test", description = "Test"))
        service.delete(created.id!!)
        val nonExisting = service.findById(created.id!!)
        assertThat(nonExisting).isNull()
    }

    @Test
    fun `delete non existing`(): Unit = runBlocking {
        val service = InMemoryProjectServiceImpl()
        service.delete(1)
        val nonExisting = service.findById(1)
        assertThat(nonExisting).isNull()
    }
}