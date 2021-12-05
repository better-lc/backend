package moe.hypixel.lc.database.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import moe.hypixel.lc.database.models.user.UserFlag
import moe.hypixel.lc.database.models.user.UserFlags
import moe.hypixel.lc.database.models.user.fromBits
import moe.hypixel.lc.database.models.user.toBits
import java.util.*

object UserFlagsSerializer: KSerializer<UserFlags> {
	override val descriptor = PrimitiveSerialDescriptor("USERFLAGS", PrimitiveKind.INT)

	override fun deserialize(decoder: Decoder): UserFlags {
		return fromBits(decoder.decodeInt())
	}


	override fun serialize(encoder: Encoder, value: UserFlags) {
		encoder.encodeInt(value.toBits())
	}
}
