package com.excella.tcp.common

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import javax.validation.ConstraintViolation

data class NotFoundException(override val message: String = "Not Found")
    : ResponseStatusException(HttpStatus.NOT_FOUND, message)

data class InvalidRequestException(override val message: String = "Invalid Request",
                                   val details: List<String> = emptyList())
    : ResponseStatusException(HttpStatus.BAD_REQUEST, message) {
    companion object {
        fun <T> of(errors: Set<ConstraintViolation<T>>): InvalidRequestException = InvalidRequestException(details = formatErrors(errors))
    }
}

fun <T> formatError(error: ConstraintViolation<T>): String =
        error.let { "${it.propertyPath}: ${it.message} (Rejected value: ${it.invalidValue})" }

fun <T> formatErrors(errors: Collection<ConstraintViolation<T>>): List<String> =
        errors.map(::formatError)

