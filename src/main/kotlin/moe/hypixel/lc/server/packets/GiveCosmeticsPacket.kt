package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.cosmetics.CosmeticType
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readVarInt
import moe.hypixel.lc.server.packets.utils.writeVarInt
import org.kodein.di.instance
import java.util.*

@PacketId(8)
class GiveCosmeticsPacket(
	var id: UUID,
	var enabledCosmetics: MutableMap<CosmeticType, Int>,
	var iconColour: Int,
	var clothCloak: Boolean,
	var lunarPlus: Boolean,
	var whoFuckingKnows: Boolean
): DIPacket() {
	@Deprecated("", level = DeprecationLevel.ERROR)
	constructor(): this(UUID.randomUUID(), mutableMapOf(), -1, true, true, true)
	constructor(user: User) : this(UUID.randomUUID(), mutableMapOf(), -1, true, true, true) {
		applyUser(user)
	}

	fun applyUser(user: User) {
		iconColour = user.rank.colour
		enabledCosmetics = user.cosmetics
		lunarPlus = user.rank.plus
		clothCloak = user.settings["clothCloak"]?.toBooleanStrict() ?: false
	}

	override fun write(buf: ByteBuf) {
		val cosmeticManager by instance<CosmeticManager>()

		buf.writeLong(id.mostSignificantBits)
		buf.writeLong(id.leastSignificantBits)

		val cosmetics = cosmeticManager.getCosmetics()
		buf.writeVarInt(cosmetics.size)
		for(cosmetic in cosmetics) {
			buf.writeVarInt(cosmetic.id)
			buf.writeBoolean(enabledCosmetics.containsValue(cosmetic.id))
		}

		buf.writeInt(iconColour)
		buf.writeBoolean(clothCloak)
		buf.writeBoolean(lunarPlus)
		buf.writeBoolean(whoFuckingKnows)
	}

	override fun read(buf: ByteBuf) {
		val cosmeticManager by instance<CosmeticManager>()

		id = UUID(buf.readLong(), buf.readLong())

		for(i in 0 until buf.readVarInt()) {
			val cosmeticInfo = cosmeticManager.getCosmetic(buf.readVarInt())
			if(!buf.readBoolean()) continue
			cosmeticInfo ?: continue
			enabledCosmetics[cosmeticInfo.type] = cosmeticInfo.id
		}

		iconColour = buf.readInt()

		// No clue what these do yet
		clothCloak = buf.readBoolean()
		lunarPlus = buf.readBoolean()
		whoFuckingKnows = buf.readBoolean()
	}
}