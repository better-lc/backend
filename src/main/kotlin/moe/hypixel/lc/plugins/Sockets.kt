package moe.hypixel.lc.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.netty.buffer.Unpooled
import moe.hypixel.lc.server.objects.Player
import moe.hypixel.lc.server.packets.PacketManager
import moe.hypixel.lc.server.packets.`in`.DoEmoteInPacket
import moe.hypixel.lc.server.packets.`in`.FriendMessageInPacket
import moe.hypixel.lc.server.packets.out.*
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
			DoEmoteInPacket::class
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
						val packet = packetManager.getInPacket(buf) ?: continue

						when(packet) {
							is FriendMessageInPacket -> {
								if (packet.message.startsWith("uwu")) {
									sendPacket(BanMessageOutPacket(2, "unlimitedcoder2", listOf("Hello", "World")))
								}
							}
							else -> {
								println(packet.toString())
							}
						}
					}
				}
			}
		}
	}
}