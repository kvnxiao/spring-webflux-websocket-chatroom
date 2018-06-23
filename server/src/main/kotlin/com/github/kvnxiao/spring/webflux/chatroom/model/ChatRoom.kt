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

import org.hashids.Hashids
import java.util.concurrent.atomic.AtomicLong

data class ChatRoom(val name: String, val password: String = "") {

    companion object {
        private val incr = AtomicLong()
        private val hash = Hashids("", 4)

        fun generateId(): String =
            hash.encode(
                incr.incrementAndGet())

        fun decodeId(code: String): Long? =
            hash.decode(code).let {
                if (it.isEmpty()) null else it[0]
            }
    }

    val id: String = generateId()
    val users: MutableList<User> = mutableListOf()

    fun count(): Int = users.size

    fun hasPassword(): Boolean = password.isNotEmpty()
}
