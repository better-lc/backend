package moe.hypixel.lc.server.packets.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

// https://github.com/PringlePot/LCWebsocket/blob/master/src/main/java/me/pringles/lcwebsocketclient/util/ByteBufWrapper.java
public class ByteBufHelper {
	public static int readVarInt(ByteBuf buf)
	{
		int i = 0;
		int j = 0;
		while (true)
		{
			byte b0 = buf.readByte();
			i |= (b0 & 127) << j++ * 7;
			if (j > 5)
			{
				throw new RuntimeException("VarInt too big");
			}
			if ((b0 & 128) != 128)
			{
				break;
			}
		}

		return i;
	}

	public static void writeVarInt(ByteBuf buf, int input) {
		while ((input & -128) != 0) {
			buf.writeByte(input & 127 | 128);
			input >>>= 7;
		}
		buf.writeByte(input);
	}

	public static String readString(ByteBuf buf, final int maxLength) throws IOException {
		final int i = readVarInt(buf);
		if (i > maxLength * 4) {
			throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
		}
		if (i < 0) {
			throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
		}
		try {

			final byte[] data = ByteBufUtil.getBytes(buf.readBytes(i));
			final String s = new String(data, Charset.defaultCharset());
			if (s.length() > maxLength) {
				throw new IOException("The received string length is longer than maximum allowed (" + s.length() + " > " + maxLength + ")");
			}
			return s;
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e);
		}
	}

	public static void writeString(ByteBuf buf, final String string) throws IOException {
		try {
			final byte[] abyte = string.getBytes(Charset.defaultCharset());
			if (abyte.length > 32767) {
				throw new IOException("String too big (was " + abyte.length + " bytes encoded, max " + 32767 + ")");
			}
			writeVarInt(buf, abyte.length);
			buf.writeBytes(abyte);
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e);
		}
	}
}
