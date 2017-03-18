package me.lixko.csgoexternals.structs;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;

public class StructField {

	private int size = 0;
	private int offset = 0;
	MemStruct memstr;

	public StructField(MemStruct memstr, int size) {
		this.memstr = memstr;
		this.size = size;
		this.offset = memstr.SIZE;
		memstr.SIZE += size;
	}

	public StructField(MemStruct memstr, int size, int offset) {
		this.memstr = memstr;
		this.size = size;
		this.offset = offset;
		memstr.SIZE += size;
		if (memstr.SIZE < offset + size) {
			memstr.SIZE = offset + size;
		}
	}

	public int offset() {
		return this.offset;
	}

	public int size() {
		return this.size;
	}

	public void set(int value) {
		memstr.membuf.setInt(memstr.OFFSET + this.offset, value);
	}

	public void set(float value) {
		memstr.membuf.setFloat(memstr.OFFSET + this.offset, value);
	}

	public void set(double value) {
		memstr.membuf.setDouble(memstr.OFFSET + this.offset, value);
	}

	public void set(long value) {
		memstr.membuf.setLong(memstr.OFFSET + this.offset, value);
	}

	public void set(boolean value) {
		memstr.membuf.setByte(memstr.OFFSET + this.offset, value ? (byte) 1 : (byte) 0);
	}

	public void set(short value) {
		memstr.membuf.setShort(memstr.OFFSET + this.offset, value);
	}

	public void set(char value) {
		memstr.membuf.setChar(memstr.OFFSET + this.offset, value);
	}

	public void set(Pointer value) {
		memstr.membuf.setPointer(memstr.OFFSET + this.offset, value);
	}

	public ByteBuffer get() {
		return memstr.membuf.getByteBuffer(memstr.OFFSET + this.offset, this.size);
	}

	public int getInt() {
		return memstr.membuf.getInt(memstr.OFFSET + this.offset);
	}

	public long getLong() {
		return memstr.membuf.getLong(memstr.OFFSET + this.offset);
	}

	public float getFloat() {
		return memstr.membuf.getFloat(memstr.OFFSET + this.offset);
	}

	public double getDouble() {
		return memstr.membuf.getDouble(memstr.OFFSET + this.offset);
	}

	public Pointer getPointer() {
		return memstr.membuf.getPointer(memstr.OFFSET + this.offset);
	}

	public short getShort() {
		return memstr.membuf.getShort(memstr.OFFSET + this.offset);
	}

	public byte getByte() {
		return memstr.membuf.getByte(memstr.OFFSET + this.offset);
	}

	public boolean getBoolean() {
		return memstr.membuf.getByte(memstr.OFFSET + this.offset) > 0;
	}

	public char getChar() {
		return memstr.membuf.getChar(memstr.OFFSET + this.offset);
	}

	public int[] getIntArray() {
		return memstr.membuf.getIntArray(memstr.OFFSET + this.offset, this.size / Integer.BYTES);
	}

	public long[] getLongArray() {
		return memstr.membuf.getLongArray(memstr.OFFSET + this.offset, this.size / Long.BYTES);
	}

	public float[] getFloatArray() {
		return memstr.membuf.getFloatArray(memstr.OFFSET + this.offset, this.size / Float.BYTES);
	}

	public double[] getDoubleArray() {
		return memstr.membuf.getDoubleArray(memstr.OFFSET + this.offset, this.size / Double.BYTES);
	}

	public short[] getShortArray() {
		return memstr.membuf.getShortArray(memstr.OFFSET + this.offset, this.size / Short.BYTES);
	}

	public byte[] getByteArray() {
		return memstr.membuf.getByteArray(memstr.OFFSET + this.offset, this.size);
	}

	public char[] getCharArray() {
		return memstr.membuf.getCharArray(memstr.OFFSET + this.offset, this.size);
	}

}
