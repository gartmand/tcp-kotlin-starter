package com.excella.tcp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
