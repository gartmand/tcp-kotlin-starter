package com.excella.tcp.common

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

fun Mono<out Any>.toOkResponse(): Mono<ServerResponse> = this.flatMap { ok().syncBody(it) }

fun ServerRequest.respondWith(block: ServerRequest.() -> Mono<*>): Mono<ServerResponse> =
        block().toOkResponse()

fun <T> ServerRequest.withBody(clazz: Class<T>, block: (Mono<T>) -> Mono<*>): Mono<*> =
        this.bodyToMono(clazz).let(block)

fun <T> ServerRequest.respondWithBody(clazz: Class<T>, block: (Mono<T>) -> Mono<*>): Mono<ServerResponse> =
        this.respondWith {
            this.withBody(clazz, block)
        }

inline fun <reified T : Any> ServerRequest.respondWithBody(noinline block: (Mono<T>) -> Mono<*>): Mono<ServerResponse> =
        this.respondWithBody(T::class.java, block)

fun ServerRequest.doByRequestId(block: (String) -> Mono<*>): Mono<ServerResponse> =
        this.pathVariable("id").let(block).flatMap { ok().syncBody(it) }
