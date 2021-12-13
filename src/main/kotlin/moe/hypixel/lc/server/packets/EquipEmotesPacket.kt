package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.writeVarInt

@PacketId(56)
class EquipEmotesPacket(
	var equipped: MutableList<Int>
): Packet {
	@Deprecated("", level = DeprecationLevel.ERROR)
	constructor(): this(mutableListOf())
	override fun write(buf: ByteBuf) {
		buf.writeVarInt(equipped.size)
		for(emote in equipped) {
			buf.writeVarInt(emote)
		}
	}

	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readVarInt()) {
			equipped.add(buf.readVarInt())
		}
	}
}