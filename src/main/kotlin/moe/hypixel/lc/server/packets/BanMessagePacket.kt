package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.writeString

@PacketId(1056)
class BanMessagePacket(
	var banType: Int, // Doesn't seem to change anything
	var name: String,
	var lines: MutableList<String>
): Packet {
	constructor(): this(-1, "", mutableListOf())
	override fun write(buf: ByteBuf) {
		buf.writeInt(banType)
		buf.writeString(name)
		buf.writeInt(lines.size)
		for(line in lines) {
			buf.writeString(line)
		}
	}

	override fun read(buf: ByteBuf) {
		banType = buf.readInt()
		name = buf.readString(128)

		for(i in 0 until buf.readInt()) {
			lines.add(buf.readString(128))
		}
	}
}