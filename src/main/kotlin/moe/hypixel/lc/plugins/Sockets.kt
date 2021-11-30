package moe.hypixel.lc.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.WebSockets
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.packets.*
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketManager
import java.util.*

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
			FriendMessagePacket::class,
			DoEmoteInPacket::class,
			CosmeticChangePacket::class,
			PlayerDataRequestPacket::class,
			ClientSettingsInPacket::class,
			ServerDataInPacket::class,
			GiveCosmeticsPacket::class
		)

		webSocket("/") {
			val playerId = UUID.fromString(call.request.headers["playerid"])
			println("Connection from $playerId!")

			val proxy = object : WebsocketProxy(packetManager, this) {
				override suspend fun onClientSend(packet: Packet) {
					when (packet) {
						is CosmeticChangePacket -> {
							cancelPacket(packet)
						}
					}
				}

				override suspend fun onServerSend(packet: Packet) {
					when(packet) {
						is GiveCosmeticsPacket -> {
							if(packet.id == UUID.fromString("34c3d4d9-e9af-457c-99d4-e80fd13ebc7e"))
								packet.iconColour = 11141120
						}
					}
				}
			}

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