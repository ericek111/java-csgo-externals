package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.util.BufferStruct;

public class RecvProp extends BufferStruct {
	public long m_pVarName; // char *
	public int m_RecvType;
	public int m_Flags;
	public int m_StringBufferSize;
	public boolean m_bInsideArray;
	public byte[] pad0 = new byte[3];
	public long m_pExtraData; // const void *
	public long m_pArrayProp; // RecvProp *
	public long m_ArrayLengthProxy; //  (*ArrayLengthRecvProxyFn)( void *pStruct, int objectID, int currentArrayLength )
	public long m_ProxyFn; // (*RecvVarProxyFn)( const CRecvProxyData *pData, void *pStruct, void *pOut )
	public long m_DataTableProxyFn; // (*DataTableRecvVarProxyFn)(const RecvProp *pProp, void **pOut, void *pData, int objectID)
	public long m_pDataTable; // RecvTable *
	public int m_Offset;
	public int m_ElementStride;
	public int m_nElements;
	public byte[] pad1 = new byte[4];
	public long m_pParentArrayPropName; // const char *
	// 0x59
	// 0x3C on 32b
}