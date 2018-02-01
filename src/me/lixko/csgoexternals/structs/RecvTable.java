package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.util.BufferStruct;

public class RecvTable extends BufferStruct {
	public long m_pProps; // RecvProp*
	public int m_nProps;
	public int pad0;
	public long m_pDecoder; // void*
	public long m_pNetTableName; // char*
	public boolean m_bInitialized;
	public boolean m_bInMainList;
}