package moe.hypixel.lc.server.handlers

import io.ktor.websocket.*
import moe.hypixel.lc.cache.Cache
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket
import moe.hypixel.lc.utils.playerId
import org.kodein.di.instance

suspend fun WebsocketProxyHandler.handleGiveCosmetics(packet: GiveCosmeticsPacket) {
	val playerExists = cache.playerExists(packet.id)

	if(playerExists) {
		val user = db.userRepo.getUser(packet.id)
		packet.iconColour = user.rank.colour

		if(user.hasCosmetics()) {
			packet.cosmetics = cosmeticManager.getPlayerCosmetics(user)
		}
	}
}