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
package com.github.kvnxiao.spring.webflux.chatroom.config

import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.lobby.LobbySocketHandler
import com.github.kvnxiao.spring.webflux.chatroom.model.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
class WebSocketConfiguration @Autowired constructor(
    private val lobbySocketHandler: LobbySocketHandler
) {

    @Bean
    fun webSocketMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping().apply {
            order = 1
            urlMap = mapOf<String, WebSocketHandler>(
                "/lobby/ws" to lobbySocketHandler
            )
        }
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter = object : WebSocketHandlerAdapter() {
        override fun handle(exchange: ServerWebExchange, handler: Any): Mono<HandlerResult> {
            (this.webSocketService as HandshakeWebSocketService)
                .setSessionAttributePredicate { it == Session.USER }
            return super.handle(exchange, handler)
        }
    }
}
