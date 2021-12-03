package moe.hypixel.lc.plugins

import com.eatthepath.uuid.FastUUID
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.WebSockets
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.handlers.handleChangeCosmetics
import moe.hypixel.lc.server.handlers.handleGiveCosmetics
import moe.hypixel.lc.server.handlers.handleTrackedPlayersAdd
import moe.hypixel.lc.server.packets.*
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.sendPacket
import moe.hypixel.lc.utils.playerId
import moe.hypixel.lc.utils.players
import moe.hypixel.lc.utils.sockets
import org.kodein.di.ktor.closestDI

fun Application.configureSockets() {
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(15)
		timeout = Duration.ofSeconds(15)
		maxFrameSize = Long.MAX_VALUE
		masking = false
	}

	routing {
		webSocket("/") {
			sockets += this

			players = mutableSetOf()
			playerId = FastUUID.parseUUID(call.request.headers["playerid"])

			println("Connection from $playerId!")

			val proxy = WebsocketProxy(this, object : WebsocketProxyHandler(closestDI()) {
				override suspend fun onClientSend(packet: Packet) {
					when (packet) {
						is CosmeticChangePacket -> handleChangeCosmetics(packet)
						is ServerDataInPacket -> sendPacket(ChatMessagePacket("Thank you for using BetterLC!\nhttps://discord.gg/JtM5FFhArY"))
						is DoEmoteInPacket -> cancelPacket(packet)
						is AddTrackedPlayersPacket -> handleTrackedPlayersAdd(packet)
					}
				}

				override suspend fun onServerSend(packet: Packet) {
					when(packet) {
						is GiveCosmeticsPacket -> handleGiveCosmetics(packet)

						is EmoteGivePacket -> {
							packet.emotes.clear()
							for(i in 1 until 200) {
								packet.emotes.add(i)
							}
						}
					}
				}
			})

			proxy.run()
		}
	}
}