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

import com.github.kvnxiao.spring.webflux.chatroom.util.RoomId
import com.github.kvnxiao.spring.webflux.chatroom.util.RoomName
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.LobbyListEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Component
class ChatLobby {

    private val idToRoomsMap: MutableMap<RoomId, ChatRoom> = mutableMapOf()
    private val userToRoomIdMap: MutableMap<User, RoomId> = mutableMapOf()
    private val roomNames: MutableSet<RoomName> = mutableSetOf()

    fun count(): Mono<Int> = idToRoomsMap.size.toMono()

    fun create(name: String, password: String = ""): Mono<ChatRoom> {
        return if (roomNames.contains(name)) {
            Mono.empty()
        } else {
            return Mono.just(ChatRoom(name, password))
                .map {
                    roomNames.add(it.name)
                    idToRoomsMap[it.id] = it
                    return@map it
                }
        }
    }

    fun exists(id: String): Boolean = idToRoomsMap.containsKey(id)

    fun get(id: String): ChatRoom? = idToRoomsMap[id]

    fun getRoom(user: User): ChatRoom? = userToRoomIdMap[user]?.let { idToRoomsMap[it] }

    fun addUserToRoom(user: User, room: ChatRoom) {
        userToRoomIdMap[user] = room.id
        room.users.add(user)
    }

    fun removeUserFromRoom(user: User) {
        val roomId = userToRoomIdMap.remove(user) ?: return
        val room = idToRoomsMap[roomId] ?: return
        room.users.remove(user)

        if (room.isEmpty()) {
            room.name.let {
                roomNames.remove(it)
                idToRoomsMap.remove(it)
            }
        }
    }

    fun listRoomsJson(): String = LobbyListEvent(idToRoomsMap.values).toJson()
}
