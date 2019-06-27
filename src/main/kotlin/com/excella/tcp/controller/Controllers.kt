package com.excella.tcp.controller

import com.excella.tcp.domain.DomainModel
import com.excella.tcp.domain.Employee
import com.excella.tcp.service.CrudService
import com.excella.tcp.service.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

abstract class CrudController<T : DomainModel> {
    fun getAll(sort: Sort) = service.all(sort)
    fun getById(@PathVariable id: String) = service.byId(id)
    fun create(@Valid @RequestBody t: T) = service.save(t)
    fun update(@PathVariable id: String,
               @Valid @RequestBody t: T) = service.update(id, t)

    fun delete(id: String) = service.deleteById(id)
    abstract val service: CrudService<T>
}

class EmployeeController
@Autowired constructor(override val service: EmployeeService) : CrudController<Employee>()