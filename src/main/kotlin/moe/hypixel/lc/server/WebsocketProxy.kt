package moe.hypixel.lc.server

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import io.netty.buffer.Unpooled
import kotlinx.coroutines.selects.select
import moe.hypixel.lc.server.packets.CosmeticChangePacket
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketManager
import moe.hypixel.lc.server.packets.utils.sendPacket

abstract class WebsocketProxy(
	val packetManger: PacketManager,
	val clientSocket: DefaultWebSocketServerSession
) {
	// lol racism
	private val blacklistedHeaders = listOf("connection", "sec-websocket-key", "sec-websocket-version", "upgrade", "host")
	private val cancelledPackets = mutableSetOf<Packet>()

	abstract suspend fun onClientSend(packet: Packet)
	abstract suspend fun onServerSend(packet: Packet)

	fun cancelPacket(packet: CosmeticChangePacket) {
		cancelledPackets.add(packet)
	}

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
			//TODO: This could do with a cleanup
			while(!outgoing.isClosedForSend && !clientSocket.outgoing.isClosedForSend) {
				select<Unit> {
					incoming.onReceiveCatching {
						val frame = it.getOrNull() ?: return@onReceiveCatching
						val packet = packetManger.getPacket(frame)

						if(packet != null) {
							onServerSend(packet)
							if(!cancelledPackets.contains(packet))
								clientSocket.sendPacket(packet)
							else
								cancelledPackets.remove(packet)
						} else {
							clientSocket.send(Frame.Binary(true, frame.data))
						}
					}

					clientSocket.incoming.onReceiveCatching {
						val frame = it.getOrNull() ?: return@onReceiveCatching
						val packet = packetManger.getPacket(frame)

						if(packet != null) {
							onClientSend(packet)
							if(!cancelledPackets.contains(packet))
								sendPacket(packet)
							else
								cancelledPackets.remove(packet)
						} else {
							send(Frame.Binary(true, frame.data))
						}
					}
				}
			}
		}
	}
}