package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId

@PacketId(38)
class GiveEmotesPacket: Packet {
	override fun write(buf: ByteBuf) {
		buf.writeInt(201)
		for (i in 0..200)
			buf.writeInt(i)
	}

	override fun read(buf: ByteBuf) {

	}
}