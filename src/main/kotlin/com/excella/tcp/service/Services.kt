package com.excella.tcp.service

import com.excella.tcp.common.NotFoundException
import com.excella.tcp.domain.DomainModel
import com.excella.tcp.domain.Employee
import com.excella.tcp.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class CrudService<T : DomainModel> {
    fun all(sort: Sort): Flux<T> = repository.findAll(sort)
    fun byId(id: String): Mono<T> =
            repository.findById(id)
                    .switchIfEmpty(Mono.error(NotFoundException("Unknown resource: $id")))

    fun save(t: T) = repository.save(t)
    fun update(id: String, t: T): Mono<T> {
        t.id = id
        return byId(id).flatMap {
            repository.save(it).thenReturn(it)
        }
    }

    fun deleteById(id: String): Mono<T> =
            byId(id).flatMap { repository.delete(it).thenReturn(it) }

    abstract val repository: ReactiveMongoRepository<T, String>
}

class EmployeeService @Autowired constructor(override val repository: EmployeeRepository): CrudService<Employee>()