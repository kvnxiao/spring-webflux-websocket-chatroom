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

import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.error.UserStillConnectedError
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.LobbyListEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatLobby {

    private val idToRoomsMap: MutableMap<String, ChatRoom> = ConcurrentHashMap()
    private val userToRooms: MutableMap<User, ChatRoom> = ConcurrentHashMap()

    fun count(): Mono<Int> = idToRoomsMap.size.toMono()

    fun create(name: String, password: String = ""): Mono<ChatRoom> {
        return Mono.just(ChatRoom(name, password))
            .map {
                idToRoomsMap[it.id] = it
                it
            }
    }

    fun exists(id: String): Boolean = idToRoomsMap.containsKey(id)

    fun get(id: String): ChatRoom? = idToRoomsMap[id]

    fun userInRoom(user: User): Boolean = userToRooms.containsKey(user)

    fun addUserToRoom(user: User, room: ChatRoom): Mono<Boolean> {
        return Mono.just(user).filter { !userInRoom(it) }.map {
            println("USER NOT IN ROOM")
            userToRooms[user] = room
            room.users.add(user)
        }.switchIfEmpty(Mono.error(UserStillConnectedError(user)))
    }

    fun removeUserFromRoom(user: User, room: ChatRoom) {
        if (userInRoom(user)) {
            userToRooms.remove(user)
            room.users.remove(user)
        }
    }

    fun listRoomsJson(): String = LobbyListEvent(idToRoomsMap.values).toJson()
}
