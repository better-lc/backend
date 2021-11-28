package moe.hypixel.lc.server.packets

import moe.hypixel.lc.server.packets.utils.getId
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation

class PacketManager {
	private val inPackets = mutableSetOf<KClass<InPacket>>()

	fun getInPacket(id: Int): InPacket? {
		return inPackets.firstOrNull { (it as KClass<Packet>).getId() == id }?.createInstance()
	}

	fun registerInPacket(packetClass: KClass<InPacket>) {
		if(!packetClass.hasAnnotation<PacketId>())
			throw IllegalArgumentException("Packet ${packetClass.simpleName} does not have id")

		inPackets.add(packetClass)
	}
}