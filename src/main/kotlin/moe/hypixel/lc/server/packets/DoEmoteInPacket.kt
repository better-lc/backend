package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId

@PacketId(39)
class DoEmoteInPacket(
	var emoteId: Int
): Packet {
	constructor(): this(-1)

	override fun write(buf: ByteBuf) {
		buf.writeInt(emoteId)
	}

	override fun read(buf: ByteBuf) {
		emoteId = buf.readInt()
	}
}