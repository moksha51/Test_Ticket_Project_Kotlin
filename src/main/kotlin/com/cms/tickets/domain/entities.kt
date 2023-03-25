package com.cms.tickets.domain

data class Project(val id: Int? = null, val name: String, val description: String)

data class Ticket(val id: Int? = null, val project: Int, val title: String, val body: String, val completed: Boolean = false, val tags: List<String> = emptyList())

class ProjectNotFoundException(id: Int) : RuntimeException("Project with id:$id doesn't exists in our system.")

class TicketNotFoundException(id: Int) : RuntimeException("Ticket$id doesn't exists in our system.")
