package me.lixko.csgoexternals.structs;

public class CUtlVector extends MemStruct {

	public final StructField DataPtr = new StructField(this, Long.BYTES);
	public final StructField Max = new StructField(this, Integer.BYTES);
	public final StructField unk02 = new StructField(this, Integer.BYTES);
	public final StructField Count = new StructField(this, Integer.BYTES);
	public final StructField DataPtrBack = new StructField(this, Integer.BYTES);

}
