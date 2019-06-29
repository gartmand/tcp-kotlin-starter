package com.excella.tcp.handler

import com.excella.tcp.common.InvalidRequestException
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
    fun getAll(req: ServerRequest): Mono<ServerResponse> =
            ok().body(service.all(), domainClass)

    fun getById(req: ServerRequest): Mono<ServerResponse> =
            req.pathVariable("id")
                    .let(service::byId)
                    .compose { ok().body(it, domainClass) }

    fun create(req: ServerRequest): Mono<ServerResponse> =
            bodyToValidDomain(req)
                    .flatMap { service.save(it) }
                    .flatMap { ok().syncBody(it) }


    fun update(req: ServerRequest): Mono<ServerResponse> =
            bodyToValidDomain(req)
                    .flatMap { service.update(req.pathVariable("id"), it) }
                    .flatMap { ok().syncBody(it) }

    fun delete(req: ServerRequest) =
            req.pathVariable("id")
                    .let(service::deleteById)
                    .compose { ok().body(it, domainClass) }

    abstract val validator: Validator
    abstract val service: CrudService<T>

    protected open fun bodyToValidDomain(req: ServerRequest): Mono<T> =
            req.bodyToMono(domainClass).let(::ensureDomainValid)

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