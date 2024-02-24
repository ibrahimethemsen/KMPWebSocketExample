package com.ibrahimethemsen.backend

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSockets(position: Position) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        webSocket("/position") {
            coroutineScope {
                launch {
                    position.eventFlow.collect{offset->
                        sendSerialized(offset)
                    }
                }
                launch {
                    while (true) {
                        val receiveEvent = receiveDeserialized<Offset>()
                        position.broadcastEvent(receiveEvent)
                    }
                }
            }
        }
    }
}