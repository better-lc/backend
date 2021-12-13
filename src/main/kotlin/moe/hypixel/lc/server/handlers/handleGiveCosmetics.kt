package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket

suspend fun WebsocketProxyHandler.handleGiveCosmetics(packet: GiveCosmeticsPacket) {
	val user = db.userRepo.get(packet.id) ?: return

	if(user.uuid == playerId) {
		packet.all = true
	}

	packet.applyUser(user)
}