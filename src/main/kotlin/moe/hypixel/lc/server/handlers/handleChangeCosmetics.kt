package moe.hypixel.lc.server.handlers

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.server.WebsocketProxy
import moe.hypixel.lc.server.packets.CosmeticChangePacket
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket
import org.kodein.di.instance

suspend fun WebsocketProxy.handleChangeCosmetics(connection: WebSocketServerSession, packet: CosmeticChangePacket) {
	val cosmeticManager by instance<CosmeticManager>()

	for (id in packet.changes.filter { it.state }.map { cosmeticManager.getCosmetic(it.id) }.) {

//		connection.call.application.log.info("$?.name} was enabled!")
	}
}
