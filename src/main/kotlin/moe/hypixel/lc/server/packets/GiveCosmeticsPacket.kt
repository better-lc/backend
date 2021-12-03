package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.obj.PlayerCosmetic
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.writeVarInt
import java.util.*

@PacketId(8)
class GiveCosmeticsPacket(
	var id: UUID,
	var cosmetics: MutableSet<PlayerCosmetic>,
	var iconColour: Int,
	var firstBoolean: Boolean,
	var secondBoolean: Boolean,
	var thirdBoolean: Boolean,
): Packet {
	@Deprecated("", level = DeprecationLevel.ERROR)
	constructor(): this(UUID.randomUUID(), mutableSetOf(), -1, true, true, true)

	override fun write(buf: ByteBuf) {
		buf.writeLong(id.mostSignificantBits)
		buf.writeLong(id.leastSignificantBits)

		buf.writeVarInt(cosmetics.size)
		for(cosmetic in cosmetics) {
			buf.writeVarInt(cosmetic.id)
			buf.writeBoolean(cosmetic.enabled)
		}


		buf.writeInt(iconColour)
		buf.writeBoolean(firstBoolean)
		buf.writeBoolean(secondBoolean)
		buf.writeBoolean(thirdBoolean)
	}

	override fun read(buf: ByteBuf) {
		id = UUID(buf.readLong(), buf.readLong())

		for(i in 0 until buf.readVarInt()) {
			cosmetics.add(PlayerCosmetic(
				buf.readVarInt(),
				buf.readBoolean()
			))
		}

		iconColour = buf.readInt()

		// No clue what these do yet
		firstBoolean = buf.readBoolean()
		secondBoolean = buf.readBoolean()
		thirdBoolean = buf.readBoolean()
	}
}