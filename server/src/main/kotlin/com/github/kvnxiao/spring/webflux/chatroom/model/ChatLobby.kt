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

import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.LobbyListEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

typealias RoomId = String
typealias RoomName = String

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

    fun get(id: String): Mono<ChatRoom> = Mono.justOrEmpty(idToRoomsMap[id])

    fun addUserToRoom(user: User, room: ChatRoom): Mono<Void> {
        return Mono.justOrEmpty(userToRoomIdMap.put(user, room.id)).then()
    }

    fun removeUserFromRoom(user: User): Mono<Void> {
        return Mono.justOrEmpty(userToRoomIdMap.remove(user))
            .flatMap { roomId -> Mono.justOrEmpty(idToRoomsMap[roomId]) }
            .filter { it.count() == 0 }
            .map {
                roomNames.remove(it.name)
                idToRoomsMap.remove(it.name)
            }
            .then()
    }

    fun listRoomsJson(): String = LobbyListEvent(idToRoomsMap.values).toJson()
}
