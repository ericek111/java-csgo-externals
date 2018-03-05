package me.lixko.csgoexternals.structs;

import com.github.jonatino.misc.MemoryBuffer;

public class VectorMem extends MemStruct {

	public final StructField x = new StructField(this, Float.BYTES);
	public final StructField y = new StructField(this, Float.BYTES);
	public final StructField z = new StructField(this, Float.BYTES);

	public VectorMem(MemStruct parent) {
		super(parent);
	}

	public VectorMem() {
		super();
	}

	public VectorMem(MemoryBuffer buf) {
		super(buf);
	}

	public float[] getVector() {
		return new float[] { x.getFloat(), y.getFloat(), z.getFloat() };
	}
	
	public float[] copyTo(float[] arr) {
		arr[0] = x.getFloat();
		arr[1] = y.getFloat();
		arr[2] = z.getFloat();
		return arr;
	}
	
	public VectorMem readFrom(float[] arr) {
		x.set(arr[0]);
		y.set(arr[1]);
		z.set(arr[2]);
		return this;
	}

}
