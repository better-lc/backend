package moe.hypixel.lc.server.handlers

import moe.hypixel.lc.server.WebsocketProxyHandler
import moe.hypixel.lc.server.packets.DoEmoteInPacket

suspend fun WebsocketProxyHandler.handleEmoteInPacket(packet: DoEmoteInPacket) {
	cancelPacket(packet)
}