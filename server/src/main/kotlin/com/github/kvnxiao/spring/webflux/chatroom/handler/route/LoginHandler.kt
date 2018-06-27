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

import com.github.kvnxiao.spring.webflux.chatroom.model.Session
import com.github.kvnxiao.spring.webflux.chatroom.model.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class LoginHandler {

    fun handle(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono<User>()
            .filter { it.name.isNotBlank() }
            .map { user -> request.session().subscribe { it.attributes[Session.USER] = user } }
            .flatMap { ServerResponse.ok().build() }
            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
}
