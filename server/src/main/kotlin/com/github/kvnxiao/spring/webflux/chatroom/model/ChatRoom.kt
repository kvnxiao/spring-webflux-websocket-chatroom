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
package com.github.kvnxiao.spring.webflux.chatroom.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.WebSocketEvent
import com.github.kvnxiao.spring.webflux.chatroom.serdes.ChatRoomSerializer
import org.hashids.Hashids
import reactor.core.publisher.UnicastProcessor
import java.util.concurrent.atomic.AtomicLong

@JsonSerialize(using = ChatRoomSerializer::class)
data class ChatRoom(val name: String, val password: String = "") {

    val id: String = generateId()
    val users: MutableList<User> = mutableListOf()
    val eventProcessor: UnicastProcessor<WebSocketEvent> = UnicastProcessor.create()
    val chatFlux = eventProcessor.publish()
        .autoConnect(1)

    fun count(): Int = users.size

    fun isEmpty(): Boolean = users.size == 0

    fun hasPassword(): Boolean = password.isNotEmpty()

    companion object {
        private val incr = AtomicLong()
        private val hash = Hashids("", 4)

        fun generateId(): String =
            hash.encode(
                incr.incrementAndGet())

        fun decodeId(code: String): Long =
            hash.decode(code).let {
                if (it.isEmpty()) -1 else it[0]
            }
    }
}
