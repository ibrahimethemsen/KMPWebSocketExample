package remote

import BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import model.Offset


class BackendServiceImpl : BackendService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
    private var socket: DefaultClientWebSocketSession? = null

    override suspend fun openSession() {
        try {
            socket = client.webSocketSession(
                method = HttpMethod.Get,
                host = BASE_URL,
                port = BackendService.PORT,
                path = BackendService.PATH
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observePosition(): Flow<Offset> = flow {
        while (true) {
            emit(socket!!.receiveDeserialized())
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    override suspend fun sendPosition(position: Offset) {
        try {
            socket?.sendSerialized(position)
        } catch (e: Exception) {
            println("Hata : " + e.message)
        }
    }
}