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

import com.github.kvnxiao.spring.webflux.chatroom.handler.route.LobbyHandler
import com.github.kvnxiao.spring.webflux.chatroom.handler.route.RoomHandler
import com.github.kvnxiao.spring.webflux.chatroom.handler.route.StatusHandler
import com.github.kvnxiao.spring.webflux.chatroom.handler.route.api.CreateRoomHandler
import com.github.kvnxiao.spring.webflux.chatroom.handler.route.api.LoginHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java)
}

@SpringBootApplication
class Application {

    @Bean
    fun resources(): RouterFunction<ServerResponse> = router {
        resources("/**", ClassPathResource("static/"))
    }

    @Bean
    fun status(handler: StatusHandler): RouterFunction<ServerResponse> = router {
        GET("/status", handler::handle)
    }

    @Bean
    fun login(handler: LoginHandler): RouterFunction<ServerResponse> = router {
        POST("/api/login", handler::handle)
    }

    @Bean
    fun lobby(handler: LobbyHandler, @Value("classpath:/static/index.html") index: Resource): RouterFunction<ServerResponse> = router {
        GET("/lobby") {
            handler.handle(it, index)
        }
    }

    @Bean
    fun room(handler: RoomHandler, @Value("classpath:/static/index.html") index: Resource): RouterFunction<ServerResponse> = router {
        GET("/room/{id}") {
            handler.enterRoom(it, index)
        }
    }

    @Bean
    fun createRoom(handler: CreateRoomHandler): RouterFunction<ServerResponse> = router {
        POST("/api/createroom", handler::handle)
    }
}
