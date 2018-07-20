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
package com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.kvnxiao.spring.webflux.chatroom.model.ChatRoom
import com.github.kvnxiao.spring.webflux.chatroom.model.User
import com.github.kvnxiao.spring.webflux.chatroom.serdes.LatencyEventDeserializer

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
sealed class WebSocketEvent {

    companion object {
        val MAPPER: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .enable(SerializationFeature.WRITE_ENUMS_USING_INDEX)
            .apply {
                registerSubtypes(
                    NamedType(LatencyEvent::class.java, "l"),
                    NamedType(LobbyListEvent::class.java, "lb"),
                    NamedType(MessageReceivedEvent::class.java, "m"),
                    NamedType(MessageSendEvent::class.java, "s"),
                    NamedType(UserConnectedEvent::class.java, "uc"),
                    NamedType(UserDisconnectedEvent::class.java, "ud")
                )
            }
    }

    fun toJson(): String = MAPPER.writeValueAsString(this)
}

@JsonDeserialize(using = LatencyEventDeserializer::class)
object LatencyEvent : WebSocketEvent()
data class LobbyListEvent(val data: Collection<ChatRoom>) : WebSocketEvent()
data class MessageReceivedEvent(val msg: String) : WebSocketEvent()
data class MessageSendEvent(val msg: String, val user: User) : WebSocketEvent()
data class UserConnectedEvent(val user: User) : WebSocketEvent()
data class UserDisconnectedEvent(val user: User) : WebSocketEvent()
