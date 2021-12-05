package moe.hypixel.lc.plugins

import com.eatthepath.uuid.FastUUID
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.WebSockets
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.database.models.user.UserFlag
import moe.hypixel.lc.database.models.user.UserFlags
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.database.models.user.fromBits
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.handlers.*
import moe.hypixel.lc.server.packets.*
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.sendPacket
import moe.hypixel.lc.utils.playerId
import moe.hypixel.lc.utils.players
import moe.hypixel.lc.utils.sockets
import org.bson.types.ObjectId
import org.kodein.di.ktor.closestDI
import org.litote.kmongo.newId

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
				override suspend fun onOpen() {
					println("onOpen called!")

					val user = db.userRepo.get(playerId)

					if(user == null) {
						db.userRepo.create(
							User(
								null,
								playerId,
								UserRank.DEFAULT,
								fromBits(0),
								null,
								mutableMapOf(),
								mutableListOf(),
								mutableMapOf()
							)
						)
					}
				}

				override suspend fun onClientSend(packet: Packet) {
					when (packet) {
						is CosmeticChangePacket -> handleChangeCosmetics(packet)
						is ServerDataInPacket -> sendPacket(ChatMessagePacket("Thank you for using BetterLC!\nhttps://discord.gg/JtM5FFhArY"))
						is DoEmoteInPacket -> handleEmoteInPacket(packet)
						is AddTrackedPlayersPacket -> handleTrackedPlayersAdd(packet)
						is RemoveTrackedPlayersPacket -> handleTrackedPlayersRemove(packet)
					}
				}

				override suspend fun onServerSend(packet: Packet) {
					when(packet) {
						is GiveCosmeticsPacket -> handleGiveCosmetics(packet)
						is EmoteGivePacket -> cancelPacket(packet)
					}
				}
			})

			proxy.run()
		}
	}
}