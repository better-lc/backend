package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf

interface OutPacket : Packet {
	fun write(buf: ByteBuf)
}