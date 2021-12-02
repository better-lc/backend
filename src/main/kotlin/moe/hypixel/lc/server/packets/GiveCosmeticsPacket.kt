package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.writeVarInt
import java.util.*

@PacketId(8)
class GiveCosmeticsPacket(
	var id: UUID,
	var enabledCosmetics: MutableSet<Int>,
	var iconColour: Int,
	var firstBoolean: Boolean,
	var secondBoolean: Boolean,
	var thirdBoolean: Boolean,
	var all: Boolean
): Packet {
	@Deprecated("", level = DeprecationLevel.ERROR)
	constructor(): this(UUID.randomUUID(), mutableSetOf(), -1, true, true, true, false)

	override fun write(buf: ByteBuf) {
		buf.writeLong(id.mostSignificantBits)
		buf.writeLong(id.leastSignificantBits)

		if(all) {
			buf.writeVarInt(1973)
			for(i in 1..1973) {
				buf.writeVarInt(i)
				buf.writeBoolean(enabledCosmetics.contains(i))
			}
		} else {
			buf.writeVarInt(enabledCosmetics.size)
			for(cosmetic in enabledCosmetics) {
				buf.writeVarInt(cosmetic)
				buf.writeBoolean(true)
			}
		}

		buf.writeInt(iconColour)
		buf.writeBoolean(firstBoolean)
		buf.writeBoolean(secondBoolean)
		buf.writeBoolean(thirdBoolean)
	}

	override fun read(buf: ByteBuf) {
		id = UUID(buf.readLong(), buf.readLong())

		for(i in 0 until buf.readVarInt()) {
			val id = buf.readVarInt()
			val enabled = buf.readBoolean()
			if(enabled)
				enabledCosmetics.add(id)
		}

		iconColour = buf.readInt()

		// No clue what these do yet
		firstBoolean = buf.readBoolean()
		secondBoolean = buf.readBoolean()
		thirdBoolean = buf.readBoolean()
	}
}