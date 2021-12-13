package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId

@PacketId(33)
class CrashPacket : Packet {
	override fun write(buf: ByteBuf) {

	}

	override fun read(buf: ByteBuf) {
	}
}
