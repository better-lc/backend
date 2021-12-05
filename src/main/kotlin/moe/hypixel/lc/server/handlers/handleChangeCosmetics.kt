package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.CosmeticChangePacket
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket

suspend fun WebsocketProxyHandler.handleChangeCosmetics(packet: CosmeticChangePacket) {
	cancelPacket(packet)

	val user = db.userRepo.get(playerId)!!

	db.userRepo.save(user.apply {
		settings["clothCloak"] = packet.clothCloak.toString()
		cosmetics = packet.dbCosmetics(this)
	})

	broadcastCosmeticChange(GiveCosmeticsPacket(user))
}