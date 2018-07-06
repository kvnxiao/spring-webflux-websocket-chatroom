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

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.github.kvnxiao.spring.webflux.chatroom.model.ChatRoom

/**
 * JSON Serializer for [ChatRoom] object type.
 *
 * Sends the necessary room information to the client for displaying rooms in the lobby:
 * - room name
 * - number of players in the room
 * - if room has a password
 * - hash-id of room for accessing in url
 * @see ChatRoom
 */
class ChatRoomSerializer(t: Class<ChatRoom>?) : StdSerializer<ChatRoom>(t) {

    constructor() : this(null)

    override fun serialize(value: ChatRoom, gen: JsonGenerator, provider: SerializerProvider) {
        gen.apply {
            writeStartObject()
            writeStringField("name", value.name)
            writeNumberField("count", value.count())
            writeBooleanField("hasPwd", value.hasPassword())
            writeStringField("id", value.id)
            writeEndObject()
        }
    }
}
