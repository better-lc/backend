package moe.hypixel.lc.server

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import io.ktor.server.engine.*
import io.ktor.util.*
import io.ktor.websocket.*
import io.netty.buffer.Unpooled
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.SelectClause1
import kotlinx.coroutines.selects.SelectInstance
import kotlinx.coroutines.selects.select

class WebsocketProxy(
	val clientSocket: DefaultWebSocketServerSession
) {
	// lol racism
	private val blacklistedHeaders = listOf("connection", "sec-websocket-key", "sec-websocket-version", "upgrade", "host")

	suspend fun run() {
		val wsClient = HttpClient(CIO) {
			install(io.ktor.client.features.websocket.WebSockets)
		}

		wsClient.webSocket("wss://assetserver.lunarclientprod.com/connect", {
			headers {
				for((key, value) in clientSocket.call.request.headers.toMap().filterKeys { !blacklistedHeaders.contains(it.lowercase()) }) {
					set(key, value.joinToString())
				}
			}
		}) {
			while(!outgoing.isClosedForSend && !clientSocket.outgoing.isClosedForSend) {
				select<Unit> {
					incoming.onReceive {
						clientSocket.send(Frame.Binary(true, Unpooled.copiedBuffer(it.data).array()))
					}

					clientSocket.incoming.onReceive {
						send(Frame.Binary(true, Unpooled.copiedBuffer(it.data).array()))
					}
				}
			}
		}
	}
}
