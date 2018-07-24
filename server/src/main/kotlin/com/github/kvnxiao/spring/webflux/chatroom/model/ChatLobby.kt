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
import mu.KLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

@Component
class ChatLobby {

    companion object : KLogging()

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

    /**
     * Returns a Mono<Boolean> that signifies whether the user was added to the room.
     * This should always return true since it checks if the user is not already in a room before adding the user,
     * otherwise it will yield a [UserStillConnectedError] error.
     *
     * A back-off retry on this method is used to prevent multiple connections for a single user, and to prevent
     * out-of-order websocket close frames for when a user refreshes the WebSocket connection (the page).
     */
    fun addUserToRoom(user: User, room: ChatRoom): Mono<Boolean> {
        return Mono.just(user).filter { !userInRoom(it) }.map {
            userToRooms[user] = room
            room.addUser(user)
        }.switchIfEmpty(Mono.error(UserStillConnectedError(user)))
    }

    /**
     * Removes a user from the specified room.
     * Done on cleanup when a WebSocket connection is closed.
     */
    fun removeUserFromRoom(user: User, room: ChatRoom) {
        val userRoom = userToRooms[user]!!
        if (userRoom == room) {
            userToRooms.remove(user)
            userRoom.removeUser(user, Consumer {
                logger.debug { "$room has been empty for 60 seconds... deleting room and purging from the lobby." }
                idToRoomsMap.remove(room.id)
            })
        } else {
            throw IllegalStateException(
                "Attempted to remove $user from a room they were never in! Requested room was $room, but user was in room $userRoom")
        }
    }

    fun listRoomsJson(): String = LobbyListEvent(idToRoomsMap.values).toJson()
}
