package com.cms.tickets.web

import com.cms.tickets.domain.Project
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ProjectRequest(
        @field:NotBlank(message = "The name must be defined.")
        val name: String?,

        @field:NotBlank(message = "The description must be defined.")
        val description: String?) {

    fun toProject(): Project = Project(name = this.name!!, description = this.description!!)
}

data class TicketRequest(
        @field:NotNull(message = "The Ticket ID must be defined")
        @field:Min(value = 1, message = "Ticket ID must be positive")
        val projectId: Int,

        @field:NotBlank(message = "The title must be defined.")
        val title: String,

        @field:NotBlank(message = "The body must be defined.")
        val body: String, val tags: List<String> = emptyList()) {

}