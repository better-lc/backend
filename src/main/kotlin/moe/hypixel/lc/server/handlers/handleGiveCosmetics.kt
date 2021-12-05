package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket

suspend fun WebsocketProxyHandler.handleGiveCosmetics(packet: GiveCosmeticsPacket) {
	val playerExists = cache.playerExists(packet.id)
	if(playerExists) {
		val user = db.userRepo.get(packet.id) ?: return
		packet.applyUser(user)
	}
}