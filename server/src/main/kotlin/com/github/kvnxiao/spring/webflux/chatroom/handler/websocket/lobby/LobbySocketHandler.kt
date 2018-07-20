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
package com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.lobby

import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.WebSocketSubscriber
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.HeartBeatEvent
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.WebSocketEvent
import com.github.kvnxiao.spring.webflux.chatroom.model.ChatLobby
import com.github.kvnxiao.spring.webflux.chatroom.model.Session
import com.github.kvnxiao.spring.webflux.chatroom.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.UnicastProcessor
import java.time.Duration

@Component
class LobbySocketHandler @Autowired constructor(
    private val lobby: ChatLobby
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val user = session.attributes[Session.USER] as User

        // create a unicast processor for handling websocket events for each user connected to the lobby
        val eventProcessor = UnicastProcessor.create<WebSocketEvent>()

        // create handler for received payloads to be processed by the event processor
        val receiveSubscriber = WebSocketSubscriber<WebSocketEvent>(eventProcessor, user)

        // send heartbeat every 30 seconds
        val heartbeatFlux = Flux.interval(Duration.ZERO, Duration.ofSeconds(30))
            .map { session.pingMessage { it.wrap(HeartBeatEvent.byteArray()) } }

        // send list of rooms available every 15 seconds
        val sendFlux = Flux.interval(Duration.ZERO, Duration.ofSeconds(15))
            .map { session.textMessage(lobby.listRoomsJson()) }

        // echo payloads received from the user, back to the user
        val receivedFlux = eventProcessor.publish()
            .autoConnect()
            .map(WebSocketEvent::toJson)
            .map(session::textMessage)

        // merge all fluxes that are to be sent
        val finalSendFlux = Flux.merge(receivedFlux, sendFlux, heartbeatFlux)

        // handle received payloads
        session.receive()
            .filter { it.type == WebSocketMessage.Type.TEXT }
            .map(WebSocketMessage::getPayloadAsText)
            .subscribe(receiveSubscriber::onReceive, receiveSubscriber::onError, receiveSubscriber::onComplete)
        // handle send payloads
        return session.send(finalSendFlux)
    }
}
