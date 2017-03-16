package me.lixko.csgoexternals.structs;

public class CGlowObjectManager extends MemStruct {

	public final StructField m_GlowObjectDefinitions = new StructField(this, new CUtlVector().size());
	public final StructField m_nFirstFreeSlot = new StructField(this, Integer.BYTES);
	public final StructField unk = new StructField(this, Integer.BYTES * 5);

}
