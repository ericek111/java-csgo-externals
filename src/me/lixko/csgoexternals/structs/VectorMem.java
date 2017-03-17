package me.lixko.csgoexternals.structs;

import com.github.jonatino.misc.MemoryBuffer;

public class VectorMem extends MemStruct {

	public final StructField x = new StructField(this, Float.BYTES);
	public final StructField z = new StructField(this, Float.BYTES);
	public final StructField y = new StructField(this, Float.BYTES);

}
