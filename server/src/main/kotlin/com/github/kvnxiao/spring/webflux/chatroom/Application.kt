/*
 *   Copyright (C) 2017-2018 Ze Hao Xiao
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.kvnxiao.spring.webflux.chatroom

import com.github.kvnxiao.spring.webflux.chatroom.handlers.StatusHandler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@SpringBootApplication
class Application {

    @Bean
    fun router(statusHandler: StatusHandler): RouterFunction<ServerResponse> = router {
        GET("/status", statusHandler::status)
        resources("/**", ClassPathResource("static/"))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java)
}

@Component
class CustomWebFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        exchange.request.let {
            if (it.uri.path == "/") {
                chain.filter(exchange.mutate()
                    .request(it.mutate().path("/index.html").build())
                    .build())
            } else {
                chain.filter(exchange)
            }
        }
}
