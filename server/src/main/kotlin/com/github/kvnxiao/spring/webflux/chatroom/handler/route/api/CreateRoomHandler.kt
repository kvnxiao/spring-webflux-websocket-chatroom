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
package com.github.kvnxiao.spring.webflux.chatroom.handler.route.api

import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.WebSocketEvent
import com.github.kvnxiao.spring.webflux.chatroom.model.ChatLobby
import com.github.kvnxiao.spring.webflux.chatroom.model.Session
import com.github.kvnxiao.spring.webflux.chatroom.model.request.CreateRoomRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class CreateRoomHandler @Autowired constructor(
    private val lobby: ChatLobby
) {

    fun handle(request: ServerRequest): Mono<ServerResponse> =
        request.session()
            .filter { it.attributes.containsKey(Session.USER) }
            .flatMap { request.bodyToMono<CreateRoomRequest>() }
            .flatMap { lobby.create(it.name, it.password) }
            .flatMap {
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(WebSocketEvent.MAPPER.writeValueAsString(it))
            }
            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
}
