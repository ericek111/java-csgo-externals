package me.lixko.csgoexternals.offsets;

import java.io.File;
import java.util.HashMap;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.structs.ClientClassBufStruct;
import me.lixko.csgoexternals.structs.RecvProp;
import me.lixko.csgoexternals.structs.RecvTable;
import me.lixko.csgoexternals.util.FileUtil;

public class NetvarDumper {
	public static HashMap<String, HashMap<String, Integer>> tables = new HashMap<>();
	
	private RecvProp prop = new RecvProp();
	MemoryBuffer tablebuf = new MemoryBuffer(new RecvTable().size());
	private MemoryBuffer propbuf = new MemoryBuffer(prop.size());
	
	public static boolean DUMP = true;
	public static boolean JAVASTYLE = true;
	final String packagename = this.getClass().getPackage().getName();
	String classname = "Netvars";
	StringBuilder sb = new StringBuilder();
	
	public void initialize() {
		ClientClassBufStruct ccbs = new ClientClassBufStruct();
		MemoryBuffer ccbsbuf = new MemoryBuffer(ccbs.size());
		Engine.clientModule().read(Offsets.m_dwClientClassHead, ccbsbuf);
		
		File netvarsjava = null;
		if(JAVASTYLE && DUMP) {
			netvarsjava = new File(new File(FileUtil.formatPath("")).getParent() + "/src/" + packagename.replace('.', File.separatorChar) + "/" + classname + ".java");
			appendln("package " + packagename + ";\n");
			appendln("public class " + classname + " { ");
		}
		while(true) {
			ccbs.readFrom(ccbsbuf);

			String className = Engine.clientModule().readString(ccbs.m_pNetworkName, 64);
			walkTable(ccbs.m_pRecvTable, 2, className);

			if(ccbs.m_pNext == 0) break;
			Engine.clientModule().read(ccbs.m_pNext, ccbsbuf);
		}
		if(JAVASTYLE && DUMP) {
			appendln("}");
			FileUtil.writeToFile(sb.toString(), netvarsjava);
		}
			
		ccbsbuf.free();
		tablebuf.free();
		propbuf.free();
	}
	
	public void walkTable(long m_pRecvTable, int level, String className) {
		RecvTable table = new RecvTable();
		
		Engine.clientModule().read(m_pRecvTable, tablebuf);
		table.readFrom(tablebuf);
		if(table.m_nProps == 0) {
			return;
		}
		
		String tablemapkey = className;
		HashMap<String, Integer> maptable = new HashMap<>();
		
		//System.out.println(className + " / " + table.m_nProps);
		table.readFrom(Engine.clientModule(), m_pRecvTable, tablebuf);
		String tableName = Engine.clientModule().readString(table.m_pNetTableName, 64);
		
		for(int x = 1; x < level; x++)
			append("\t");
		
		if (JAVASTYLE)
			append("public static class ");
		else
			append("struct ");
		
		if(className == "") {
			appendln(tableName + " {");
			tablemapkey = tableName;
		} else {
			appendln(className + " { // " + tableName);
		}
		
		for(int i = 1; i < table.m_nProps; i++) {
			prop.readFrom(Engine.clientModule(), table.m_pProps + i * prop.size(), propbuf);
			
			if(prop.m_pVarName == 0) continue;
			
			String propName = Engine.clientModule().readString(prop.m_pVarName, 64);
			if(Character.isDigit(propName.charAt(0))) continue;

			if(SendPropType.values()[prop.m_RecvType] == SendPropType.DPT_DataTable) {
				if(Character.isDigit(propName.charAt(0))) continue;
				dumpDataTable(prop.m_pDataTable, level, prop.m_Offset);
				//walkTable(prop.m_pDataTable, level, "");
				if(JAVASTYLE)
					appendln(propName);
				else
					appendln(" // " + "0x" + Integer.toHexString(prop.m_Offset) + (propName != "" ? " - " + propName : ""));
				maptable.put(propName, prop.m_Offset);
				continue;
			}
			
			for(int x = 0; x < level; x++)
				append("\t");
			
			maptable.put(propName, prop.m_Offset);
			if(JAVASTYLE) {
				appendln("public static final long " + propName.replace('.', '_').replace("]", "").replace('[', '_').replace('"', ' ') + " = " + "0x" + Integer.toHexString(prop.m_Offset) + "; // " + SendPropType.values()[prop.m_RecvType].typename + (prop.m_nElements > 1 ? " elements: " + prop.m_nElements : ""));
			} else {
				appendln(SendPropType.values()[prop.m_RecvType].typename + " " + propName +"; // " + "0x" + Integer.toHexString(prop.m_Offset) + (prop.m_nElements > 1 ? " elements: " + prop.m_nElements : ""));
			}
			
			if(prop.m_pDataTable == 0) continue;
			walkTable(prop.m_pDataTable, level + 1, tableName);
		}
		for(int x = 1; x < level; x++)
			append("\t");
		appendln("}\n");
		tables.put(tablemapkey, maptable);
	}
	
	public void dumpDataTable(long m_pRecvTable, int level, int offset) {
		RecvTable table = new RecvTable();
		table.readFrom(Engine.clientModule(), m_pRecvTable, tablebuf);
		String tableName = Engine.clientModule().readString(table.m_pNetTableName, 64);
		SendPropType type = SendPropType.values()[Engine.clientModule().readInt(table.m_pProps + (table.m_nProps-1) * prop.size() + 8)];
		if(type == SendPropType.DPT_DataTable) {
			walkTable(Engine.clientModule().readLong(table.m_pProps + (table.m_nProps-1) * prop.size() + 64), level + 1, tableName);
		}
	
		for(int y = 0; y < level; y++)
			append("\t");
		if(JAVASTYLE)
			append("public static final long " + tableName + " = 0x" + Integer.toHexString(offset) + "; // " + type.typename + "[" + (table.m_nProps - 1) + "] / ");
		else
			append(type.typename + " " + tableName + "[" + (table.m_nProps - 1) + "];");
	}
	
	public int get(String classname, String netvar) {
		HashMap<String, Integer> classtable = tables.get(classname);
		if(classtable == null) {
			throw new IllegalArgumentException("Unknown class: " + classname + " / " + netvar);
		}
		Integer offset = classtable.get(netvar);
		if(offset == null) {
			throw new IllegalArgumentException("Unknown netvar: " + classname + " / " + netvar);
		}
		return offset;
	}
	
	public void append(String str) {
		if(DUMP) sb.append(str);
	}
	
	public void appendln(String str) {
		if(DUMP) sb.append(str + "\n");
	}
	
	public static enum SendPropType {
        DPT_Int("int"),
        DPT_Float("float"),
        DPT_Vector("Vector"),
        DPT_VectorXY("VectorXY"),
        DPT_String("const char *"),
        DPT_Array("array"),
        DPT_DataTable("DataTable"),
        DPT_Int64("long"),
        DPT_NUMSendPropTypes("");
        
		public String typename = "";
        SendPropType(String str) {
        	this.typename = str;
        }
	}
}
