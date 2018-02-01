package me.lixko.csgoexternals.util.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import me.lixko.csgoexternals.util.StringFormat;

public class ProtoBuf {
	public static int readVarInt32_simple(ByteBuffer stream) throws Exception {
		byte b = (byte) 0x80;
		int result = 0;
		for (int count = 0; (b & 0x80) != 0; count++) {
			b = stream.get();

			if ((count < 4) || ((count == 4) && (((b & 0xF8) == 0) || ((b & 0xF8) == 0xF8))))
				result |= (b & ~0x80) << (7 * count);
			else {
				if (count >= 10)
					throw new Exception("Nope nope nope nope! 10 bytes max!");
				if ((count == 9) ? (b != 1) : ((b & 0x7F) != 0x7F))
					throw new Exception("more than 32 bits are not supported");
			}
		}

		return result;
	}

	public static int readVarInt32(ByteBuffer stream) throws IOException {
		byte tmp = stream.get();
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7f;
		if ((tmp = stream.get()) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7f) << 7;
			if ((tmp = stream.get()) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7f) << 14;
				if ((tmp = stream.get()) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7f) << 21;
					result |= (tmp = stream.get()) << 28;
					if (tmp < 0) {
						stream.position(stream.position() - 5);
						byte[] data = new byte[16];
						stream.get(data);
						throw new IOException("Malformed varint detected: " + StringFormat.hex(data));
					}
				}
			}
		}
		return result;
	}

	public static String readString(ByteBuffer stream) throws Exception {
		int length = readVarInt32(stream);
		if (length < 0)
			throw new Exception("Negative string length: " + length);

		if (length == 0)
			return "";

		String str = new String(stream.array(), stream.position(), length, StandardCharsets.UTF_8);
		stream.position(stream.position() + length);
		return str;
	}

	public static byte[] readIBytes(ByteBuffer stream) throws Exception {
		int length = stream.getInt();
		if (length > stream.remaining())
			throw new Exception("Buffer underflow: " + length + " - " + stream.remaining() + " = " + (length - stream.remaining()));

		byte[] data = new byte[length];
		stream.get(data);
		return data;
	}

	public static byte[] readVBytes(ByteBuffer stream) throws Exception {
		int length = readVarInt32(stream);
		if (length > stream.remaining())
			throw new Exception("Buffer underflow: " + length + " - " + stream.remaining() + " = " + (length - stream.remaining()));

		byte[] data = new byte[length];
		stream.get(data);
		return data;
	}
}
