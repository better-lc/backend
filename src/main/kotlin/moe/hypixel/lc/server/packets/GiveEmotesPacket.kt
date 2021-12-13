package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.writeVarIntList

@PacketId(57)
class GiveEmotesPacket(
	var emotes: MutableList<Int>,
	var equippedEmotes: MutableList<Int>
): Packet {
	constructor(): this(mutableListOf(), mutableListOf())

	override fun write(buf: ByteBuf) {
		buf.writeVarIntList(emotes.toList())
		buf.writeVarIntList(equippedEmotes.toList())
	}

	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readVarInt()) {
			emotes.add(buf.readVarInt())
		}

		for(i in 0 until buf.readVarInt()) {
			equippedEmotes.add(buf.readVarInt())
		}
	}
}