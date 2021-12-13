package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.EquipEmotesPacket

suspend fun WebsocketProxyHandler.handleEquipEmotes(packet: EquipEmotesPacket) {
	cancelPacket(packet)
	val user = db.userRepo.get(playerId) ?: return
	user.equippedEmotes = packet.equipped.toMutableList()

	db.userRepo.save(user)
}