package moe.hypixel.lc.server.handlers

import com.eatthepath.uuid.FastUUID
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.FriendMessagePacket

//TODO: Do this better
suspend fun WebsocketProxyHandler.handleSystemMessage(packet: FriendMessagePacket) {
	cancelPacket(packet)
	val sender = db.userRepo.get(playerId) ?: return
	if(sender.rank != UserRank.OWNER) return

	val msg = packet.message

	if(msg.startsWith("!crash ")) {
		val id = msg.split(" ")[1]
		//sockets.firstOrNull { it.playerId == FastUUID.parseUUID(id) }?.sendPacket(closestDI { socket.call.application }, CrashPacket())
	}

	if(msg.startsWith("!announce")) {
		broadcastMessage(FastUUID.parseUUID(System.getenv("SYSTEM_USER_ID")), msg.replace("!announce", ""))
	}
}