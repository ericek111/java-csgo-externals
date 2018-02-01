package me.lixko.csgoexternals.util.demo;

import me.lixko.csgoexternals.util.StringFormat;

public abstract class DataStream {
	
	// TODO: Implement endianess
	
	private int bitIndex = 0;
	protected int length = 0;
	
	//byte[] arr;
	
	public abstract int readBit(int bitOffset);
	public abstract int readByte(int byteOffset);
	
	public int byteIndex() {
		return bitIndex >> 3;
	}
	
	public int bitIndex() {
		return bitIndex;
	}
	
	public int byteIndex(int bytes) {
		return bitIndex = bytes * 8;
	}
	
	public int bitIndex(int bits) {
		return bitIndex = bits;
	}
	
	public int length() {
		return length;
	}
	
	public int getBits(int offset, int bits, boolean signed) {
		int available = (length - offset);

		if (bits > available) {
			throw new Error("BufferUnderflow: Cannot get " + bits + " bit(s) from offset " + offset + ", " + available + " available");
		}
		
		if (bits > 32) {
			throw new Error("Tried to read more than 32 bits: " + bits + " bit(s) from offset " + offset + ".");
		}

		int value = 0;
		for (int i = 0; i < bits;) {
			int read;

			// Read an entire byte if we can.
			if ((bits - i) >= 8 && ((offset & 7) == 0)) {
				value |= (readByte(offset >> 3) << i);
				read = 8;
			} else {
				value |= (readBit(offset) << i);
				read = 1;
			}

			offset += read;
			i += read;
		}
		
		forward(bits);

		if (signed) {
			// If we're not working with a full 32 bits, check the
			// imaginary MSB for this bit count and convert to a
			// valid 32-bit signed value if set.
			if (bits != 32 && (value & (1 << (bits - 1))) > 0) {
				value |= -1 ^ ((1 << bits) - 1);
			}

			return value;
		}

		return value >>> 0;
	}	
	
	public boolean getBoolean(int offset) {
		return getBits(offset, 1 * 8, true) > 0;
	}
	
	public byte getBit(int offset) {
		return (byte) getBits(offset, 1, false);
	}

	public byte getByte(int offset) {
		return (byte) getBits(offset, 1 * 8, true);
	}

	public short getShort(int offset) {
		return (short) getBits(offset, 2 * 8, true);
	}

	public int getInt(int offset) {
		return getBits(offset, 4 * 8, true);
	}

	public byte getUByte(int offset) {
		return (byte) getBits(offset, 1 * 8, false);
	}

	public int getUShort(int offset) {
		return (int) getBits(offset, 2 * 8, false);
	}

	public int getUInt(int offset) {
		return getBits(offset, 4 * 8, false);
	}
	
	public float getFloat(int offset) {
		return Float.intBitsToFloat(getBits(offset, 4 * 8, false));
	}
	
	public long getLong(int offset) {
		int x = getBits(offset, 4 * 8, false);
		int y = getBits(offset, 4 * 8, false);
		return (((long)x) << 32) | (y & 0xffffffffL);
	}

	public double getDouble(int offset) {
		return Double.longBitsToDouble(getLong(offset));
	}

	public String getFixedString(int offset, int length) {
		byte[] data = new byte[length];
		get(offset, data);
		return new String(data);
	}
	
	public String getString(int offset) {
		StringBuilder sb = new StringBuilder();
		for(int i = offset; i < length; i += 8) {
			char c = (char) (getByte(i) & 0xFF);
			if(c == '\0') break;
			sb.append(c);
		}
		return sb.toString();
	}

	public byte[] get(int offset, byte[] to) {
		for(int i = 0; i < to.length; i++) {
			to[i] = getByte(offset + i * 8);
		}
		return to;
	}
	
	public void forward(int bits) {
		// TODO: Implement overflow
		bitIndex += bits;
	}
	
	public void back(int bits) {
		// TODO: Implement underflow
		bitIndex -= bits;
	}
	
	public int getBits(int bits, boolean signed) {
		return getBits(bitIndex, bits, signed);
	}	
	
	public boolean getBoolean() {
		return getBits(bitIndex, 1 * 8, true) > 0;
	}
	
	public byte getBit() {
		return (byte) getBits(bitIndex, 1, false);
	}
	
	public boolean getBitBoolean() {
		return getBits(bitIndex, 1, false) > 0;
	}

	public byte getByte() {
		return (byte) getBits(bitIndex, 1 * 8, true);
	}
	
	public byte get() {
		return getByte();
	}

	public short getShort() {
		return (short) getBits(bitIndex, 2 * 8, true);
	}

	public int getInt() {
		return getBits(bitIndex, 4 * 8, true);
	}

	public byte getUByte() {
		return (byte) getBits(bitIndex, 1 * 8, false);
	}

	public int getUShort() {
		// TODO: What's wrong? O.o
		return getByte() & 0xff | ((getByte() & 0xff) << 8);
		// return (int) getBits(bitIndex, 2 * 8, false) & 0xffff;
	}

	public long getUInt() {
		return getBits(bitIndex, 4 * 8, false) & 0xffffffff;
	}
	
	public float getFloat() {
		return Float.intBitsToFloat(getBits(bitIndex, 4 * 8, false));
	}
	
	public long getLong() {
		int x = getBits(bitIndex, 4 * 8, false);
		int y = getBits(bitIndex, 4 * 8, false);
		return (((long)x) << 32) | (y & 0xffffffffL);
	}

	public double getDouble() {
		return Double.longBitsToDouble(getLong(bitIndex));
	}

	public String getFixedString(int length) {
		byte[] data = new byte[length];
		get(bitIndex, data);
		return new String(data);
	}
	
	public String getString() {
		StringBuilder sb = new StringBuilder();
		for(int i = bitIndex; i < length; i += 8) {
			char c = (char) (getByte(i) & 0xFF);
			if(c == '\0') break;
			sb.append(c);
		}
		return sb.toString();
	}

	public byte[] get(byte[] to) {
		return get(bitIndex, to);
	}
	
}