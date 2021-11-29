package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.getId
import moe.hypixel.lc.server.packets.utils.readVarInt
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class PacketManager {
	private val inPackets = mutableSetOf<KClass<out InPacket>>()

	 fun getInPacket(buf: ByteBuf): InPacket? {
		 val id = buf.readVarInt()
		 val packet = getInPacket(id)
		 packet?.read(buf)
		 return packet
	 }

	fun getInPacket(id: Int): InPacket? {
		return inPackets.firstOrNull { (it as KClass<Packet>).getId() == id || it.findAnnotation<AdditionalPacketIds>()?.ids?.contains(id) ?: false }?.createInstance()
	}


	fun registerInPacket(packetClass: KClass<out InPacket>) {
		if(!packetClass.hasAnnotation<PacketId>())
			throw IllegalArgumentException("Packet ${packetClass.simpleName} does not have id")

		inPackets.add(packetClass)
	}

	fun registerInPackets(vararg packetClasses: KClass<out InPacket>) {
		for(packetClass in packetClasses) {
			registerInPacket(packetClass)
		}
	}
}