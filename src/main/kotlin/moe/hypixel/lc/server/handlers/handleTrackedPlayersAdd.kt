package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.AddTrackedPlayersPacket
import moe.hypixel.lc.utils.players

suspend fun WebsocketProxyHandler.handleTrackedPlayersAdd(packet: AddTrackedPlayersPacket) {
	socket.players += packet.requestedPlayers
}
