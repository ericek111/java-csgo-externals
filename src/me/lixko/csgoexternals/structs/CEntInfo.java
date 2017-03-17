package me.lixko.csgoexternals.structs;

public class CEntInfo extends MemStruct {
	
    public final StructField m_pEntity = new StructField(this, Long.BYTES);
 	public final StructField m_SerialNumber = new StructField(this, Long.BYTES);
 	public final StructField m_pPrevious = new StructField(this, Long.BYTES);
 	public final StructField m_pNext = new StructField(this, Long.BYTES);
 	
}
