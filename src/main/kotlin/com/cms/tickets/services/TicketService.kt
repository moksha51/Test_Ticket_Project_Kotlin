package com.cms.tickets.services

import com.cms.tickets.domain.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketService {
    suspend fun create(ticket: Ticket): Ticket

    fun allTickets(): Flow<Ticket>

    suspend fun findByTicketId(id: Int): Ticket?

    suspend fun updateTicket(id: Int, ticket: Ticket): Ticket?

    suspend fun deleteTicket(id: Int)
}