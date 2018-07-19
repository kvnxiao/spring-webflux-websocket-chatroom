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
package com.github.kvnxiao.spring.webflux.chatroom.handler.websocket

import com.fasterxml.jackson.core.type.TypeReference
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.WebSocketEvent
import com.github.kvnxiao.spring.webflux.chatroom.model.User
import reactor.core.publisher.UnicastProcessor
import java.io.IOException

/**
 * Represents a WebSocket connection to a browser session, providing event methods to interact with
 * received messages, errors, and completion signals.
 */
open class WebSocketSubscriber<T : WebSocketEvent>(
    protected val publisher: UnicastProcessor<T>,
    protected val user: User
) {

    protected var lastReceivedEvent: T? = null
    private val typeRef: TypeReference<T> = object : TypeReference<T>() {}

    fun onReceive(event: String) {
        try {
            val convertedEvent: T = WebSocketEvent.MAPPER.readValue(event, typeRef)
            onNext(convertedEvent)
        } catch (e: IOException) {
            onError(e)
        }
    }

    protected open fun onNext(event: T) {
        lastReceivedEvent = event
        publisher.onNext(event)
    }

    open fun onError(error: Throwable) {
        println(error)
    }

    open fun onComplete() {
        // no-op
        lastReceivedEvent = null
        println("$user has disconnected")
    }
}
