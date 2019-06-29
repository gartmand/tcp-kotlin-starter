package com.excella.tcp.config

import com.excella.tcp.common.InvalidRequestException
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
@Primary
class DetailedErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(request: ServerRequest?, includeStackTrace: Boolean): MutableMap<String, Any> {
        val map = super.getErrorAttributes(request, false)
        val error = getError(request)
        if (error is InvalidRequestException) {
            map.putIfAbsent("details", error.details)
        }

        return map
    }
}