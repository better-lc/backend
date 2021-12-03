package moe.hypixel.lc.plugins

import com.eatthepath.uuid.FastUUID
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.WebSockets
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.handlers.handleChangeCosmetics
import moe.hypixel.lc.server.handlers.handleGiveCosmetics
import moe.hypixel.lc.server.packets.*
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.sendPacket
import moe.hypixel.lc.utils.playerId

fun Application.configureSockets() {
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(15)
		timeout = Duration.ofSeconds(15)
		maxFrameSize = Long.MAX_VALUE
		masking = false
	}

	routing {
		webSocket("/") {
			playerId = FastUUID.parseUUID(call.request.headers["playerid"])
			println("Connection from $playerId!")

			val proxy = object : WebsocketProxy(this) {
				override suspend fun onClientSend(packet: Packet) {
					when (packet) {
						is CosmeticChangePacket -> handleChangeCosmetics(this@webSocket, packet)

						is ServerDataInPacket -> {
							sendPacket(ChatMessagePacket("Thank you for using BetterLC!\nhttps://discord.gg/JtM5FFhArY"))
						}

						is DoEmoteInPacket -> {
							cancelPacket(packet)
						}
					}
				}

				override suspend fun onServerSend(packet: Packet) {
					when(packet) {
						is GiveCosmeticsPacket -> handleGiveCosmetics(this@webSocket, packet)

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