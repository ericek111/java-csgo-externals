package me.lixko.csgoexternals.structs;

public class ClientClass extends MemStruct {
	public final StructField m_pCreateFn = new StructField(this, Long.BYTES); // CreateClientClassFn m_pCreateFn;
	public final StructField m_pCreateEventFn = new StructField(this, Long.BYTES); // CreateEventFn *m_pCreateEventFn;
	public final StructField m_pNetworkName = new StructField(this, Long.BYTES); // char* m_pNetworkName;
	public final StructField m_pRecvTable = new StructField(this, Long.BYTES); // RecvTable *m_pRecvTable;
	public final StructField m_pNext = new StructField(this, Long.BYTES); // ClientClass* m_pNext;
	public final StructField m_ClassID = new StructField(this, Integer.BYTES); // EClassIds m_ClassID;
}
