package moe.hypixel.lc.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.WebSockets
import moe.hypixel.lc.cache.Cache
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.packets.*
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.utils.instance
import java.util.*

fun Application.configureSockets() {
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(15)
		timeout = Duration.ofSeconds(15)
		maxFrameSize = Long.MAX_VALUE
		masking = false
	}

	val db by instance<Database>()
	val cache by instance<Cache>()

	routing {
		webSocket("/") {
			val playerId = UUID.fromString(call.request.headers["playerid"])
			println("Connection from $playerId!")

			val proxy = object : WebsocketProxy(this) {
				override suspend fun onClientSend(packet: Packet) {
					when (packet) {
						is CosmeticChangePacket -> {
							cancelPacket(packet)
						}

						is DoEmoteInPacket -> {
							cancelPacket(packet)
						}
					}
				}

				override suspend fun onServerSend(packet: Packet) {
					when(packet) {
						is GiveCosmeticsPacket -> {
							val playerExists = cache.playerExists(packet.id)

							if(playerExists) {
								val user = db.userRepo.getUser(packet.id)
								packet.iconColour = user.rank.colour
							}

							if (packet.id == playerId)
								packet.all = true
						}

						is EmoteGivePacket -> {
							packet.emotes.clear()
							for(i in 1 until 200) {
								packet.emotes.add(i)
							}
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