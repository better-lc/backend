package moe.hypixel.lc.server.packets.utils

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.WebsocketProxy
import org.kodein.di.DI
import org.kodein.di.DIAware

interface Packet {
	fun write(buf: ByteBuf)
	fun read(buf: ByteBuf)
}