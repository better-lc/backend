package moe.hypixel.lc.server.handlers

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.CosmeticChangePacket
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket
import moe.hypixel.lc.server.packets.obj.PlayerCosmetic
import moe.hypixel.lc.utils.playerId
import moe.hypixel.lc.utils.unique
import org.kodein.di.instance

suspend fun WebsocketProxyHandler.handleChangeCosmetics(packet: CosmeticChangePacket) {
	val user = db.userRepo.getUser(playerId)

	val cosmetics = packet.changes
		.filter { it.state }
		.mapNotNull { cosmeticManager.getCosmetic(it.id) }
		.filter { user.rank.canUseCosmetic(it) }
		.unique { it.type }

	db.userRepo.setUserCosmetics(playerId, cosmetics.toSet())

	broadcastCosmeticChange(GiveCosmeticsPacket(
		playerId,
		cosmetics.map { PlayerCosmetic(it.id, true) }.toMutableSet(),
		user.rank.colour,
		firstBoolean = true,
		secondBoolean = true,
		thirdBoolean = true
	))
}