package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.RemoveTrackedPlayersPacket
import moe.hypixel.lc.utils.trackedPlayers

suspend fun WebsocketProxyHandler.handleTrackedPlayersRemove(packet: RemoveTrackedPlayersPacket) {
	socket.trackedPlayers -= packet.requestedPlayers.toSet()
}
