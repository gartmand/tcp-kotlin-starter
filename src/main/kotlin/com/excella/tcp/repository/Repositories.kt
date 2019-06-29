package com.excella.tcp.repository

import com.excella.tcp.domain.Employee
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface EmployeeRepository : ReactiveMongoRepository<Employee, String>