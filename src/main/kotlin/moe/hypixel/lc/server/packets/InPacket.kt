package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf

interface InPacket: Packet {
	fun read(buf: ByteBuf)
}