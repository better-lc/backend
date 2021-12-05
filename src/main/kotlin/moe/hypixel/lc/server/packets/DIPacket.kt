package moe.hypixel.lc.server.packets

import moe.hypixel.lc.server.packets.utils.Packet
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.LateInitDI

abstract class DIPacket: Packet, DIAware {
	override val di = LateInitDI()
	fun setDI(newDI: DI) {
		di.baseDI = newDI
	}
}