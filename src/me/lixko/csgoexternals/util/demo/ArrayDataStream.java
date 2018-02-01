package me.lixko.csgoexternals.util.demo;

public class ArrayDataStream extends DataStream {
	
	private byte[] arr;
	
	public ArrayDataStream(byte[] data) {
		this.arr = data;
		this.length = data.length * 8;
	}
	
	public byte[] array() {
		return arr;
	}
			
	@Override
	public int readBit(int bitOffset) {
		return arr[bitOffset >> 3] >> (bitOffset & 7) & 1;
	}
	
	@Override
	public int readByte(int byteOffset) {
		return arr[byteOffset];
	}
}
