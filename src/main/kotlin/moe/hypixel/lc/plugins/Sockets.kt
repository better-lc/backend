package moe.hypixel.lc.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.websocket.WebSockets
import io.netty.buffer.Unpooled
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.objects.Player
import moe.hypixel.lc.server.packets.PacketManager
import moe.hypixel.lc.server.packets.`in`.*
import moe.hypixel.lc.server.packets.out.*
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.sendPacket
import java.util.*
import kotlin.concurrent.thread

fun Application.configureSockets() {
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(15)
		timeout = Duration.ofSeconds(15)
		maxFrameSize = Long.MAX_VALUE
		masking = false
	}

	routing {
		val packetManager = PacketManager()
		packetManager.registerInPackets(
			FriendMessageInPacket::class,
			DoEmoteInPacket::class,
			CosmeticChangePacket::class,
			PlayerDataRequestPacket::class,
			ClientSettingsInPacket::class,
			ServerDataInPacket::class
		)

		webSocket("/") {
			val playerId = UUID.fromString(call.request.headers["playerid"])
			println("Connection from $playerId!")

			val proxy = WebsocketProxy(this)
			proxy.run()

			/*
			sendPacket(Packet57())
			sendPacket(GiveCosmeticsPacket(playerId))
			sendPacket(EmoteGivePacket())
			sendPacket(ChatMessagePacket("Hello There"))

			for (frame in incoming) {
				when (frame) {
					is Frame.Binary -> {
						val buf = Unpooled.copiedBuffer(frame.data)
						when(val packet = packetManager.getInPacket(buf)) {
							is FriendMessageInPacket -> {
								if (packet.message.startsWith("!uwu")) {
									sendPacket(BanMessagePacket(2, "unlimitedcoder2", listOf("Hello", "World")))
								}
							}

							is CosmeticChangePacket -> {
								for(change in packet.changes) {
									println("Cosmetic ${change.id} ${if(change.state) "enabled" else "disabled"}")
								}
							}

							is PlayerDataRequestPacket -> {
								for(requestedPlayerId in packet.requestedPlayers) {
//									println("Client requested data for $requestedPlayerId")
								}
							}

							null -> {
								val pk = Unpooled.copiedBuffer(frame.data)
								val packetId = pk.readVarInt()
								println("Unknown packet with id $packetId received")
							}

							else -> {
								println(packet.toString())
							}
						}
					}
				}
			}
			*/
		}
	}
}