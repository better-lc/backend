package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.DoEmoteInPacket
import moe.hypixel.lc.server.packets.DoEmoteOutPacket
import moe.hypixel.lc.server.packets.utils.sendPacket
import org.kodein.di.ktor.closestDI

suspend fun WebsocketProxyHandler.handleDoEmote(packet: DoEmoteInPacket) {
	cancelPacket(packet)
	sendPacket(DoEmoteOutPacket(playerId, packet.emoteId))
	broadcastEmote(DoEmoteOutPacket(playerId, packet.emoteId))
}