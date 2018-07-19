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
package com.github.kvnxiao.spring.webflux.chatroom.handler.route

import com.github.kvnxiao.spring.webflux.chatroom.model.ChatLobby
import com.github.kvnxiao.spring.webflux.chatroom.model.Session
import com.github.kvnxiao.spring.webflux.chatroom.model.User
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

@Component
class RoomHandler @Autowired constructor(
    private val lobby: ChatLobby
) {

    fun enterRoom(request: ServerRequest, index: Resource): Mono<ServerResponse> =
        request.session()
            .filter { it.attributes.containsKey(Session.USER) }
            .map { request.pathVariable("id") }
            .filter(lobby::exists)
            .map { lobby.get(it)!! }
            .zipWith(request.session().map { it.attributes[Session.USER] as User })
            .map { lobby.addUserToRoom(it.t2, it.t1) }
            .flatMap { ServerResponse.ok().contentType(MediaType.TEXT_HTML).syncBody(index) }
            .switchIfEmpty(ServerResponse.temporaryRedirect(URI.create("/lobby")).build())
}
