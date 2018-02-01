package me.lixko.csgoexternals.structs;

public class RecvPropB extends MemStruct {
	public final StructField m_pVarName = new StructField(this, Long.BYTES);
	
	public final StructField m_RecvType = new StructField(this, Integer.BYTES);
	public final StructField m_Flags = new StructField(this, Integer.BYTES);
	public final StructField m_StringBufferSize = new StructField(this, Integer.BYTES);
	
	public final StructField m_bInsideArray = new StructField(this, 4); // 4 cuz padding
	
	public final StructField m_pExtraData = new StructField(this, Long.BYTES);
	public final StructField m_pArrayProp = new StructField(this, Long.BYTES);
	public final StructField m_ArrayLengthProxy = new StructField(this, Long.BYTES);
	public final StructField m_ProxyFn = new StructField(this, Long.BYTES);
	public final StructField m_DataTableProxyFn = new StructField(this, Long.BYTES);
	public final StructField m_pDataTable = new StructField(this, Long.BYTES);
	
	public final StructField m_Offset = new StructField(this, Integer.BYTES);
	public final StructField m_ElementStride = new StructField(this, Integer.BYTES);
	public final StructField m_nElements = new StructField(this, Integer.BYTES);
	
	public final StructField pad0 = new StructField(this, Integer.BYTES);
	
	public final StructField m_pParentArrayPropName = new StructField(this, Long.BYTES);
	
}
