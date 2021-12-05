package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.cosmetics.CosmeticType
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.obj.CosmeticChange
import moe.hypixel.lc.utils.unique
import org.kodein.di.instance

@PacketId(20)
class CosmeticChangePacket(
	var cosmeticData: MutableList<CosmeticChange>,
	var clothCloak: Boolean
): DIPacket() {
	constructor(): this(mutableListOf(), false)
	override fun write(buf: ByteBuf) {
		buf.writeInt(cosmeticData.size)
		for(change in cosmeticData) {
			buf.writeLong(change.id.toLong())
			buf.writeBoolean(change.state)
		}

		buf.writeBoolean(clothCloak)
	}

	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readInt()) {
			cosmeticData.add(
				CosmeticChange(
					buf.readLong().toInt(),
					buf.readBoolean()
				)
			)
		}

		clothCloak = buf.readBoolean()
	}

	fun dbCosmetics(user: User): MutableMap<CosmeticType, Int> {
		val cosmeticManager by instance<CosmeticManager>()

		val cosmeticsMap = mutableMapOf<CosmeticType, Int>()
		for(cosmetic in cosmeticData
			.filter { it.state }
			.mapNotNull { cosmeticManager.getCosmetic(it.id) }
			.filter { user.rank.canUseCosmetic(it) }
		) {
			cosmeticsMap[cosmetic.type] = cosmetic.id
		}

		return cosmeticsMap
	}
}