package com.cms.tickets.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class TicketErrorHandler{
    @ExceptionHandler(TicketNotFoundException:: class)
    fun handleTicketNotFoundException(
        servletRequest: HttpServletRequest,
        exception: Exception):
            ResponseEntity<String>{
        return ResponseEntity("Ticket not found", HttpStatus.NOT_FOUND)
    }
}