package moe.hypixel.lc.server.handlers

import io.ktor.websocket.*
import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.AddTrackedPlayersPacket
import moe.hypixel.lc.server.packets.RemoveTrackedPlayersPacket
import moe.hypixel.lc.utils.players

suspend fun WebsocketProxyHandler.handleTrackedPlayersRemove(packet: RemoveTrackedPlayersPacket) {
	socket.players -= packet.requestedPlayers.toSet()
}
