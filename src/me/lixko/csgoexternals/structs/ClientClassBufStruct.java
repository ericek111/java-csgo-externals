package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.util.BufferStruct;

public class ClientClassBufStruct extends BufferStruct {
	public long m_pCreateFn; // CreateClientClassFn m_pCreateFn;
	public long m_pCreateEventF; // CreateEventFn *m_pCreateEventFn;
	public long m_pNetworkName; // char* m_pNetworkName;
	public long m_pRecvTable; // RecvTable *m_pRecvTable;
	public long m_pNext; // ClientClass* m_pNext;
	public int m_ClassID; // EClassIds m_ClassID;
}