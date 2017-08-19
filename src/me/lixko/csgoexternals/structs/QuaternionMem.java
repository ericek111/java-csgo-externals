package me.lixko.csgoexternals.structs;

public class QuaternionMem extends MemStruct {

	public final StructField x = new StructField(this, Float.BYTES);
	public final StructField y = new StructField(this, Float.BYTES);
	public final StructField z = new StructField(this, Float.BYTES);
	public final StructField w = new StructField(this, Float.BYTES);

	public QuaternionMem(MemStruct parent) {
		super(parent);
	}

	public QuaternionMem() {
		super();
	}

}
