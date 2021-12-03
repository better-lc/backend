package moe.hypixel.lc.server.handlers

import io.ktor.websocket.*
import moe.hypixel.lc.cache.Cache
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket
import moe.hypixel.lc.utils.playerId
import org.kodein.di.instance

suspend fun WebsocketProxy.handleGiveCosmetics(connection: WebSocketServerSession, packet: GiveCosmeticsPacket) {
	val cache by instance<Cache>()
	val db by instance<Database>()

	val playerExists = cache.playerExists(packet.id)

	if(playerExists) {
		val user = db.userRepo.getUser(packet.id)
		packet.iconColour = user.rank.colour

		if(user.hasCosmetics()) {
			packet.enabledCosmetics = user.getCosmetics()
		}

		if (packet.id == connection.playerId)
			packet.all = true
	}
}