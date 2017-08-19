package me.lixko.csgoexternals.structs;

public class Studiohdr_t extends MemStruct {

	// https://github.com/ValveSoftware/source-sdk-2013/blob/master/mp/src/public/bone_setup.cpp#L1537
	public final StructField id = new StructField(this, Integer.BYTES);
	public final StructField version = new StructField(this, Integer.BYTES);
	public final StructField checksum = new StructField(this, Integer.BYTES);
	public final StructField name = new StructField(this, 64);
	public final StructField length = new StructField(this, Integer.BYTES);

	public final MemStruct eyeposition = new VectorMem(this);
	public final MemStruct illumposition = new VectorMem(this);
	public final MemStruct hull_min = new VectorMem(this);
	public final MemStruct hull_max = new VectorMem(this);
	public final MemStruct view_bbmin = new VectorMem(this);
	public final MemStruct view_bbmax = new VectorMem(this);

	public final StructField flags = new StructField(this, Integer.BYTES);
	public final StructField numbones = new StructField(this, Integer.BYTES);
	public final StructField boneindex = new StructField(this, Integer.BYTES);

}
