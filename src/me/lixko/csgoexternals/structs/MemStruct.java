package me.lixko.csgoexternals.structs;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.process.Module;

public class MemStruct {
	
	public long OFFSET = 0;
	public int SIZE = 0;
	public MemoryBuffer membuf;

	public MemStruct(MemoryBuffer membuf, long offset) {
		this.membuf = membuf;
		this.OFFSET = offset;
	}

	public MemStruct(MemoryBuffer membuf, int offset) {
		this.membuf = membuf;
		this.OFFSET = offset;
	}

	public MemStruct(MemoryBuffer membuf) {
		this.membuf = membuf;
	}
	
	public MemStruct(long offset) {
		this.OFFSET = offset;
	}

	public MemStruct(int offset) {
		this.OFFSET = offset;
	}

	public MemStruct() {
	}

	public void setSource(MemoryBuffer membuf) {
		this.membuf = membuf;
		this.OFFSET = 0;
	}

	public void setSource(MemoryBuffer membuf, int offset) {
		this.membuf = membuf;
		this.OFFSET = offset;
	}

	public long off() {
		return this.OFFSET;
	}

	public int size() {
		return SIZE;
	}
	


}