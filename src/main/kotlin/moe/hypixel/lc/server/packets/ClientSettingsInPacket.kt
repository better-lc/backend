package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.*

@PacketId(64)
class ClientSettingsInPacket(
	var data: MutableMap<String, Boolean>,
	var msg: String
): Packet {
	constructor(): this(mutableMapOf(), "")

	override fun write(buf: ByteBuf) {
		buf.writeVarInt(data.size+1)
		for((k,v) in data) {
			buf.writeString(k)
			buf.writeBoolean(v)
		}

		buf.writeString("renderClothCloaks")
		buf.writeBoolean(true)

		buf.writeString(msg)
	}

	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readVarInt()) {
			val str = buf.readString(500)
			val bool = buf.readBoolean()
			data[str] = bool
		}

		msg = buf.readString(500)
	}
}