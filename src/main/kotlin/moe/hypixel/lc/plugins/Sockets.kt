package moe.hypixel.lc.plugins

import com.eatthepath.uuid.FastUUID
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.WebSockets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.database.models.user.fromBits
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.handlers.*
import moe.hypixel.lc.server.objects.Player
import moe.hypixel.lc.server.packets.*
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.utils.playerId
import moe.hypixel.lc.utils.trackedPlayers
import moe.hypixel.lc.utils.removeSystem
import moe.hypixel.lc.utils.sockets
import org.kodein.di.ktor.closestDI
import java.awt.Color
import java.util.*

fun Application.configureSockets() {
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(15)
		timeout = Duration.ofSeconds(15)
		maxFrameSize = Long.MAX_VALUE
		masking = false
	}

	routing {
		val systemId = FastUUID.parseUUID(System.getenv("SYSTEM_USER_ID"))

		webSocket("/") {
			sockets += this

			trackedPlayers = mutableListOf()
			playerId = FastUUID.parseUUID(call.request.headers["playerid"])

			val proxy = WebsocketProxy(this, object : WebsocketProxyHandler(closestDI()) {
				override suspend fun onOpen() {
					application.log.info("Connection from $playerId!")

					val user = db.userRepo.get(playerId)

					if (user == null) {
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

				override suspend fun onClose(closeReason: CloseReason?) {
					application.log.info("$playerId disconnected ${closeReason?.knownReason}${closeReason?.message}!")
				}

				override suspend fun onClientSend(packet: Packet) {
					when (packet) {
						is CosmeticChangePacket -> handleChangeCosmetics(packet)
						is DoEmoteInPacket -> handleDoEmote(packet)
						is AddTrackedPlayersPacket -> handleTrackedPlayersAdd(packet)
						is RemoveTrackedPlayersPacket -> handleTrackedPlayersRemove(packet)
						is EquipEmotesPacket -> handleEquipEmotes(packet)
						is FriendMessagePacket -> {
							if(FastUUID.parseUUID(packet.playerId) == systemId)
								handleSystemMessage(packet)
						}
					}
				}

				override suspend fun onServerSend(packet: Packet) {
					when(packet) {
						is GiveCosmeticsPacket -> handleGiveCosmetics(packet)
						is GiveEmotesPacket -> handleGiveEmotes(packet)
						is FriendsListPacket -> {
							packet.offlineFriends.removeSystem(systemId)
							packet.onlineFriends.removeSystem(systemId)

							packet.onlineFriends.add(Player(
								FastUUID.toString(systemId),
								"BETTER LC",
								0,
								"SYSTEM"
							))
						}
					}
				}
			})

			try {
				proxy.run()
			} catch (e: Exception) {
				application.log.error(e.toString())
			}

			sockets -= this
		}
	}
}