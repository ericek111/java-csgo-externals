package me.lixko.csgoexternals.structs;

public class BaseAttributableItem extends MemStruct {
	
	public final StructField m_OriginalOwnerXuidLow = new StructField(this, Integer.BYTES, 0x39b0 - 0x34c0);
	public final StructField m_OriginalOwnerXuidHigh = new StructField(this, Integer.BYTES, 0x39b4 - 0x34c0);
	public final StructField m_nFallbackPaintKit = new StructField(this, Integer.BYTES, 0x39b8 - 0x34c0);
	public final StructField m_nFallbackSeed = new StructField(this, Integer.BYTES, 0x39bc - 0x34c0);
	public final StructField m_flFallbackWear = new StructField(this, Float.BYTES, 0x39c0 - 0x34c0);
	public final StructField m_nFallbackStatTrak = new StructField(this, Integer.BYTES, 0x39c4 - 0x34c0);
	
	public final StructField m_iItemDefinitionIndex = new StructField(this, Integer.BYTES, 0x60 + 0x268);
	public final StructField m_iEntityLevel = new StructField(this, Integer.BYTES, 0x60 + 0x270);
	public final StructField m_iItemIDHigh = new StructField(this, Integer.BYTES, 0x60 + 0x280);
	public final StructField m_iItemIDLow = new StructField(this, Integer.BYTES, 0x60 + 0x284);
	public final StructField m_iAccountID = new StructField(this, Integer.BYTES, 0x60 + 0x288);
	public final StructField m_iEntityQuality = new StructField(this, Integer.BYTES, 0x60 + 0x26c);
	public final StructField m_szCustomName = new StructField(this, 1, 0x60 + 0x340);

}
