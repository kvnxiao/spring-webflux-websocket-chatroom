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

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
data class ChatLobby @Autowired constructor(private val mapper: ObjectMapper) {

    private val rooms: MutableMap<String, ChatRoom> = mutableMapOf()

    init {
        // TODO: remove test room and add createRoom functions
        rooms["test"] = ChatRoom("test-room")
    }

    fun count(): Int = rooms.size

    fun create(room: ChatRoom) = rooms.put(room.name, room)

    fun get(name: String): ChatRoom? = rooms[name]

    fun listRoomsJson(): String = mapper.writeValueAsString(rooms.values)
}
