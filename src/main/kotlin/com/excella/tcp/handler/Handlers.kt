package com.excella.tcp.handler

import com.excella.tcp.common.InvalidRequestException
import com.excella.tcp.common.doByRequestId
import com.excella.tcp.common.respondWithBody
import com.excella.tcp.domain.DomainModel
import com.excella.tcp.domain.Employee
import com.excella.tcp.service.CrudService
import com.excella.tcp.service.EmployeeService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import javax.validation.Validator

abstract class CrudHandler<T : DomainModel>(private val domainClass: Class<T>) {
    @Suppress("UNUSED_PARAMETER")
    fun getAll(req: ServerRequest): Mono<ServerResponse> =
            ok().body(service.all(), domainClass)

    fun getById(req: ServerRequest): Mono<ServerResponse> = req.doByRequestId(service::byId)

    fun create(req: ServerRequest): Mono<ServerResponse> =
            req.respondWithBody(domainClass) { ensureDomainValid(it).flatMap(service::save) }

    fun update(req: ServerRequest): Mono<ServerResponse> =
            req.respondWithBody(domainClass) {
                ensureDomainValid(it).flatMap { domain ->
                    service.update(req.pathVariable("id"), domain)
                }
            }

    fun delete(req: ServerRequest) = req.doByRequestId(service::deleteById)

    abstract val validator: Validator
    abstract val service: CrudService<T>

    protected open fun ensureDomainValid(domainMono: Mono<T>) =
            domainMono.flatMap {
                val errors = validator.validate(it)
                val invalid = errors.isNotEmpty()
                if (invalid) {
                    Mono.error(InvalidRequestException.of(errors))
                } else Mono.just(it)
            }
}

@Component
final class EmployeeHandler(override val service: EmployeeService,
                            override val validator: Validator) : CrudHandler<Employee>(Employee::class.java)
