package moe.hypixel.lc.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.netty.buffer.Unpooled
import moe.hypixel.lc.server.objects.Player
import moe.hypixel.lc.server.packets.PacketManager
import moe.hypixel.lc.server.packets.`in`.*
import moe.hypixel.lc.server.packets.out.*
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.sendPacket
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
			FriendMessageInPacket::class,
			DoEmoteInPacket::class,
			CosmeticChangePacket::class,
			PlayerDataRequestPacket::class,
			ClientSettingsInPacket::class
		)

		webSocket("/") {
			//TODO: Move to FastUUID
			val playerId = UUID.fromString(call.request.headers["playerid"])

			println("Connection from $playerId!")

			if(playerId == null) {
				close()
				return@webSocket
			}

			sendPacket(Packet57())
			sendPacket(GiveCosmeticsPacket(playerId))

			val friends = FriendsListPacket()

			friends.onlineFriends.add(Player(
				"f7c77d99-9f15-4a66-a87d-c4a51ef30d19",
				"hypixel",
				0, // online: 0, afk: 1, busy: 2 (probably)
				"Hypixel"
			))

			sendPacket(friends)

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
									println("Client requested data for $requestedPlayerId")
								}
							}

							null -> {
								val pk = Unpooled.copiedBuffer(frame.data)
								val packetId = pk.readVarInt()
								if(packetId == 6) {
									val str = pk.readString(52)
									val otherStr = pk.readString(100)
									println("6: $str - $otherStr")
								} else {
									println("Unknown packet with id $packetId received")
								}
							}

							else -> {
								//println(packet.toString())
							}
						}
					}
				}
			}
		}
	}
}