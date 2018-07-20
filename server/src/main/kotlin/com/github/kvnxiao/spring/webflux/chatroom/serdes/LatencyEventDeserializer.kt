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
package com.github.kvnxiao.spring.webflux.chatroom.serdes

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.github.kvnxiao.spring.webflux.chatroom.handler.websocket.event.LatencyEvent

/**
 * Deserializes [LatencyEvent] JSON representations into the same singleton object.
 * Prevents Jackson from instantiating a new instance of [LatencyEvent] each time, to abide by
 * the 'object' singleton contract in Kotlin.
 */
class LatencyEventDeserializer(t: Class<LatencyEvent>?) : StdDeserializer<LatencyEvent>(t) {

    constructor() : this(null)

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LatencyEvent = LatencyEvent
}
