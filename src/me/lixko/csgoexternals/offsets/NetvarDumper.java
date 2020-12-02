package me.lixko.csgoexternals.offsets;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.structs.ClientClassBufStruct;
import me.lixko.csgoexternals.structs.RecvProp;
import me.lixko.csgoexternals.structs.RecvTable;
import me.lixko.csgoexternals.util.FileUtil;
import me.lixko.csgoexternals.util.StringFormat;

public class NetvarDumper {
	
	private RecvProp prop = new RecvProp();
	MemoryBuffer tablebuf = new MemoryBuffer(new RecvTable().size());
	private MemoryBuffer propbuf = new MemoryBuffer(prop.size());
	
	private RecvProp prop2 = new RecvProp();
	private MemoryBuffer prop2buf = new MemoryBuffer(prop2.size());
	
	final String packagename = this.getClass().getPackage().getName();
	String classname = "Netvars";
	StringBuilder sb = new StringBuilder();
	
	public void initialize() {
		ClientClassBufStruct ccbs = new ClientClassBufStruct();
		MemoryBuffer ccbsbuf = new MemoryBuffer(ccbs.size());
		Engine.clientModule().read(Offsets.m_dwClientClassHead, ccbsbuf);
		
		File netvarsjava = new File(new File(FileUtil.formatPath("")).getParent() + "/src/" + packagename.replace('.', File.separatorChar) + "/" + classname + ".java");
		appendln("package " + packagename + ";\n");
		appendln("public class " + classname + " { ");

		while(true) {
			ccbs.readFrom(ccbsbuf);

			String className = Engine.clientModule().readString(ccbs.m_pNetworkName, 64);
			javaWalkTable(ccbs.m_pRecvTable, 2, className, null);
			appendln("");

			if(ccbs.m_pNext == 0) break;
			Engine.clientModule().read(ccbs.m_pNext, ccbsbuf);
		}

		appendln("}");
		FileUtil.writeToFile(sb.toString(), netvarsjava);
				
		ccbsbuf.free();
		tablebuf.free();
		propbuf.free();
	}

	public void dumbWalkTable(long m_pRecvTable, int level) {
		RecvTable table = new RecvTable();
		
		Engine.clientModule().read(m_pRecvTable, tablebuf);
		table.readFrom(tablebuf);
		
		for (int i = 0; i < table.m_nProps; i++) {
			prop.readFrom(Engine.clientModule(), table.m_pProps + i * prop.size(), propbuf);
			if (prop.m_pVarName == 0) 
				continue;
			String propName = Engine.clientModule().readString(prop.m_pVarName, 64);
			
			if (Character.isDigit(propName.charAt(0)) || propName.startsWith("baseclass"))
				continue;
			
			for(int x = 1; x < level; x++)
				append("\t");
			
			SendPropType type = SendPropType.values()[Engine.clientModule().readInt(table.m_pProps + (table.m_nProps-1) * prop.size() + 8)];
			
			append("\t" + propName + " [0x" + StringFormat.hex(prop.m_Offset) + "] " + type.name() + "\n");
			
			if (prop.m_pDataTable != 0) {
				// prop.m_nElements
				dumbWalkTable(prop.m_pDataTable, level + 1);
			}

		}
	}
	
	public void javaWalkTable(long m_pRecvTable, int level, String className, RecvProp parent) {
		RecvTable table = new RecvTable();
		
		Engine.clientModule().read(m_pRecvTable, tablebuf);
		table.readFrom(tablebuf);
		String tableName = Engine.clientModule().readString(table.m_pNetTableName, 64);	
		
		boolean foundProp = false;
		
		for (int i = 0; i < table.m_nProps; i++) {
			prop.readFrom(Engine.clientModule(), table.m_pProps + i * prop.size(), propbuf);
			if (prop.m_pVarName == 0) 
				continue;
			String propName = Engine.clientModule().readString(prop.m_pVarName, 64);
			
			if (Character.isDigit(propName.charAt(0)) || propName.startsWith("baseclass"))
				continue;
			
			if (!foundProp) {
				foundProp = true;
				appendPadding(level - 1);
				appendln("public static final class " + className + " { // " + tableName);
				if (parent != null) {
					appendPadding(level);
					appendln("public static final long BASE_OFFSET = 0x" + Integer.toHexString(parent.m_Offset) + ";");
				}
			}

			if (prop.m_pDataTable == 0) {
				appendPadding(level);
				appendln("public static final long " + propName.replace('.', '_').replace("]", "").replace('[', '_').replace('"', ' ') + " = " + "0x" + Integer.toHexString(prop.m_Offset) + "; // " + SendPropType.values()[prop.m_RecvType].typename + (prop.m_nElements > 1 ? " elements: " + prop.m_nElements : ""));
			} else {
				// appendln(StringFormat.dumpObj(prop));
				RecvProp parentProp = new RecvProp();
				parentProp.readFrom(propbuf);
				javaWalkTable(prop.m_pDataTable, level + 1, propName, parentProp);
			}
		}
		
		if (foundProp) {
			appendPadding(level - 1);
			appendln("}");
		} else {
			// head table with no props
			if (parent == null) {
				appendPadding(level - 1);
				appendln("public static final class " + className + " { // " + tableName);
				appendPadding(level - 1);
				appendln("}");
			} else {
				appendPadding(level - 1);
				append("public static final long " + className + " = 0x" + Integer.toHexString(parent.m_Offset) + ";");
				append(" // " + SendPropType.values()[prop.m_RecvType].typename + "[" + table.m_nProps + "]");
				appendln("");
			}
		}
	}

	public void append(String str) {
		sb.append(str);
	}
	
	public void appendln() {
		sb.append('\n');
	}
	
	public void appendln(String str) {
		sb.append(str + "\n");
	}
	
	private void appendPadding(int times) {
		for(int x = 0; x < times; x++)
			sb.append('\t');
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
