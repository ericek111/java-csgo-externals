package me.lixko.csgoexternals.structs;

public class RecvTableB extends MemStruct {
	public final StructField m_pProps = new StructField(this, Long.BYTES);
	public final StructField m_nProps = new StructField(this, Integer.BYTES);
	public final StructField m_pDecoder = new StructField(this, Long.BYTES);
	public final StructField m_pNetTableName = new StructField(this, Long.BYTES);
	public final StructField m_bInitialized = new StructField(this, 1);
	public final StructField m_bInMainList = new StructField(this, 1);
}
