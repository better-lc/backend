package moe.hypixel.lc.server.packets.`in`

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.InPacket
import moe.hypixel.lc.server.packets.PacketId
import moe.hypixel.lc.server.packets.`in`.obj.CosmeticChange

@PacketId(20)
class CosmeticChangePacket: InPacket {
	var changes = mutableListOf<CosmeticChange>()

	override fun read(buf: ByteBuf) {
		val changeCount = buf.readInt()
		for(i in 0 until changeCount) {
			changes.add(
				CosmeticChange(
					buf.readLong(),
					buf.readBoolean()
				)
			)
		}
	}
}