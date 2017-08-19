package me.lixko.csgoexternals.structs;

public class Mstudiobone_t extends MemStruct {

	// https://github.com/AimTuxOfficial/AimTux/blob/3d529e8c0a130527d5b963d35eb81e16853883ba/src/SDK/IVModelInfo.h#L100
	public final StructField sznameindex = new StructField(this, Integer.BYTES);
	public final StructField parent = new StructField(this, Integer.BYTES); // parent bone
	public final StructField bonecontroller = new StructField(this, 6 * Integer.BYTES); // bone controller index, -1 == none

	// default values
	public final MemStruct pos = new VectorMem(this);
	public final MemStruct quat = new QuaternionMem(this);
	public final MemStruct rot = new VectorMem(this); // RadianEuler
	// compression scale
	public final MemStruct posscale = new VectorMem(this);
	public final MemStruct rotscale = new VectorMem(this);

	public final MemStruct poseToBone = new Matrix3x4Mem(this);
	public final MemStruct qAlignment = new QuaternionMem(this);
	public final StructField flags = new StructField(this, Integer.BYTES);
	public final StructField proctype = new StructField(this, Integer.BYTES);
	public final StructField procindex = new StructField(this, Integer.BYTES); // procedural rule
	public final StructField physicsbone = new StructField(this, Integer.BYTES); // index into physically simulated bone

	public final StructField surfacepropidx = new StructField(this, Integer.BYTES); // index into string tablefor property name
	public final StructField contents = new StructField(this, Integer.BYTES); // See BSPFlags.h for the contents flags
	public final StructField surfacepropLookup = new StructField(this, Integer.BYTES); // this index must be cached by the loader, not saved in the file

	public final StructField unused = new StructField(this, 28);

	// > sizeof() = D8

}
