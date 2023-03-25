package com.cms.tickets.web.api

import com.cms.tickets.domain.Project
import com.cms.tickets.domain.ProjectNotFoundException
import com.cms.tickets.services.ProjectService
import com.cms.tickets.web.ProjectRequest
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
@RequestMapping("/api/v1/projects")
@CrossOrigin
class ProjectApiController(private val service: ProjectService) {

    @PostMapping(produces = ["application/json"])
    @ApiResponses(value = [ApiResponse(description = "Create a project", responseCode = "201")])
    suspend fun create(@RequestBody @Valid request: ProjectRequest): Project = service.create(request.toProject())

    @GetMapping(produces = ["application/json"])
    @ApiResponses(value = [ApiResponse(description = "Get all projects", responseCode = "200")])
    fun all(): Flow<Project> = service.all()

    @GetMapping("/{id}")
    @ApiResponses(
            ApiResponse(description = "Get one project", responseCode = "200"),
            ApiResponse(description = "Not found", responseCode = "404")
    )
    suspend fun find(@PathVariable id: Int): Project = nullCheck(id) {
        service.findById(id)
    }

    @PutMapping("/{id}")
    @ApiResponses(
            ApiResponse(description = "Update a project", responseCode = "201"),
            ApiResponse(description = "Not found", responseCode = "404")
    )
    suspend fun update(@PathVariable id: Int, @RequestBody @Valid request: ProjectRequest): Project = nullCheck(id) {
        service.update(id, request.toProject())
    }


    @DeleteMapping("/{id}")
    @ApiResponses(
            ApiResponse(description = "Delete a project. Idempotent", responseCode = "204")
    )
    suspend fun delete(@PathVariable id: Int) = service.delete(id)

    private suspend fun nullCheck(id: Int, body: suspend () -> Project?): Project {
        val project = body()
        if (project != null) {
            return project
        }
        throw ProjectNotFoundException(id)
    }

    @ExceptionHandler(ProjectNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(ex: ProjectNotFoundException) = ex.message

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