package com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.room

import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.WebSocketSubscriber
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.WebSocketEvent
import com.github.kvnxiao.spring.webflux.chatroom.model.ChatLobby
import com.github.kvnxiao.spring.webflux.chatroom.model.User
import reactor.core.publisher.UnicastProcessor

class RoomSocketSubscriber(val lobby: ChatLobby, eventProcessor: UnicastProcessor<WebSocketEvent>, user: User)
    : WebSocketSubscriber<WebSocketEvent>(eventProcessor, user) {

    override fun onComplete() {
        lastReceivedEvent = null
        lobby.removeUserFromRoom(user)
        println("$user has disconnected from the room")
    }

    override fun onConnect() {
        println("$user has connected to the room")
    }
}
