package com.excella.tcp.router

import com.excella.tcp.domain.DomainModel
import com.excella.tcp.handler.CrudHandler
import com.excella.tcp.handler.EmployeeHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

fun <T : DomainModel> crudRoutes(handler: CrudHandler<T>):
        RouterFunction<ServerResponse> =
        router {
            GET("/", handler::getAll)
            GET("/{id}", handler::getById)
            POST("/", handler::create)
            PUT("/{id}", handler::update)
            DELETE("/{id}", handler::delete)
        }

@Configuration
class TopLevelRoutes() {
    @Autowired
    lateinit var employeeHandler: EmployeeHandler

    @Bean
    fun routes(): RouterFunction<*> =
            nest(RequestPredicates.path("/api"), api())


    fun api(): RouterFunction<*> =
            nest(RequestPredicates.path("/employees"), crudRoutes(employeeHandler))
}
