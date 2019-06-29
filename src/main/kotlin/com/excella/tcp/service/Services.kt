package com.excella.tcp.service

import com.excella.tcp.common.NotFoundException
import com.excella.tcp.domain.DomainModel
import com.excella.tcp.domain.Employee
import com.excella.tcp.repository.EmployeeRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class CrudService<T : DomainModel> {
    fun all(): Flux<T> = repository.findAll()
    fun byId(id: String): Mono<T> =
            repository.findById(id)
                    .switchIfEmpty(Mono.error(NotFoundException("Unknown resource: $id")))

    fun save(t: T): Mono<T> = repository.save(t)
    fun update(id: String, t: T): Mono<T> =
            byId(id).map { t.apply { this.id = it.id } }
                    .flatMap { repository.save(it) }


    fun deleteById(id: String): Mono<T> =
            byId(id).flatMap { repository.delete(it).thenReturn(it) }

    abstract val repository: ReactiveMongoRepository<T, String>
}

@Service
class EmployeeService(override val repository: EmployeeRepository) : CrudService<Employee>()
