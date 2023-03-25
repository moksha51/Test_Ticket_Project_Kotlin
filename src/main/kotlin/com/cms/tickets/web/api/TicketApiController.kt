package com.cms.tickets.web.api

import com.cms.tickets.domain.Project
import com.cms.tickets.domain.ProjectNotFoundException
import com.cms.tickets.domain.Ticket
import com.cms.tickets.domain.TicketNotFoundException
import com.cms.tickets.services.TicketService
import com.cms.tickets.web.ProjectRequest
import com.cms.tickets.web.TicketRequest
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.WebExchangeBindException

@RestController
@RequestMapping("/api/v1/tickets")
@CrossOrigin
class TicketApiController(private val service: TicketService) {

    @PostMapping(produces = ["application/json"])
    @ApiResponses(value = [ApiResponse(description = "Create a new ticket", responseCode = "201")])
    suspend fun create(@RequestBody @Valid request: TicketRequest): Ticket = service.create(request.toTicket())

    @GetMapping(produces = ["application/json"])
    @ApiResponses(value = [ApiResponse(description = "Get all tickets", responseCode = "200")])
    fun all(): Flow<Ticket> = service.allTickets()

    @GetMapping("/{id}")
    @ApiResponses(
            ApiResponse(description = "Get one project", responseCode = "200"),
            ApiResponse(description = "Not found", responseCode = "404")
    )
    suspend fun find(@PathVariable id: Int): Ticket? = nullCheck(id) {
        service.findByTicketId(id)
    }

    @PutMapping("/ticket/{id}")
    @ApiResponses(
            ApiResponse(description = "Update a project", responseCode = "201"),
            ApiResponse(description = "Not found", responseCode = "404")
    )
    suspend fun update(@PathVariable id: Int, @RequestBody @Valid request: ProjectRequest): Project = nullCheck(id) {
        service.updateTicket(id, request.toTicket())
    }


    @DeleteMapping("/{id}")
    @ApiResponses(
            ApiResponse(description = "Delete a project. Idempotent", responseCode = "204")
    )
    suspend fun delete(@PathVariable id: Int) = service.deleteTicket(id)

    private suspend fun nullCheck(id: Int, body: () -> Ticket?): Ticket {
        val ticket = body()
        if (ticket != null) {
            return ticket
        }
        throw ProjectNotFoundException(id)
    }

    @ExceptionHandler(ProjectNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(ex: TicketNotFoundException) = ex.message

    @ExceptionHandler(WebExchangeBindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: WebExchangeBindException): Map<String, String> {
        logger.debug { "Handle validation exception $ex" }
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            errors[fieldName] = error.defaultMessage!!
        }

        logger.debug { "Errors => $errors" }
        return errors
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }

}