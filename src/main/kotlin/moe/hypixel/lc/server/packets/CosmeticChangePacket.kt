package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.obj.CosmeticChange

@PacketId(20)
class CosmeticChangePacket(
	var changes: MutableList<CosmeticChange>
): Packet {
	constructor(): this(mutableListOf())

	override fun write(buf: ByteBuf) {
		buf.writeInt(changes.size)
		for(change in changes) {
			buf.writeLong(change.id)
			buf.writeBoolean(change.state)
		}
	}

	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readInt()) {
			changes.add(
				CosmeticChange(
					buf.readLong(),
					buf.readBoolean()
				)
			)
		}
	}
}