package me.lixko.csgoexternals.util.demo;

import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.sdk.DemoFormat;
import me.lixko.csgoexternals.sdk.DemoFormat.SVC_Messages;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.bsp.BSPParser.Entity;

public class DemoParser {
	public ByteBuffer stream;
	public DemoFormat.demoheader_t header = new DemoFormat.demoheader_t();
	public DemoFormat.democmdheader_t cmdh = new DemoFormat.democmdheader_t();
	public DemoFormat.democmdinfo_t[] cmdinfoarr = StringFormat.fill(new DemoFormat.democmdinfo_t[DemoFormat.MAX_SPLITSCREEN_CLIENTS], () -> new DemoFormat.democmdinfo_t());
	public DemoFormat.dem_msg cmd;
	public ArrayList<SendTable> sendTables = new ArrayList<>();
	
	boolean demofinished = false;
	int demotick = 0;

	int ixde = 0;

	public DemoParser(File f) throws IOException {
		this.stream = ByteBuffer.wrap(Files.readAllBytes(f.toPath()));
	}

	public void parse() {
		stream.order(ByteOrder.LITTLE_ENDIAN);
		stream.rewind();
		if (stream.remaining() < header.size())
			throw new IllegalArgumentException("Failed to open demo: file too small.");
		header.readFrom(stream);

		//System.out.println(StringFormat.dumpObj(header, false));

		if (!Arrays.equals(header.demofilestamp, DemoFormat.DEMO_HEADER_ID))
			throw new IllegalArgumentException("Invalid demo header: " + StringFormat.dumpObj(header.demofilestamp, false));

		if (header.demoprotocol != DemoFormat.DEMO_PROTOCOL)
			throw new IllegalArgumentException("Invalid demo protocol (" + header.demoprotocol + "), expected " + DemoFormat.DEMO_PROTOCOL);

		try {
			while (demoLoop())
				;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean demoLoop() throws Exception {
		if (!readCmdHeader())
			return false;
		
		//if (DemoFormat.dem_msg.values()[cmdh.cmd] == DemoFormat.dem_msg.dem_signon) {
			System.out.println(" >>> " + ixde + " / " + DemoFormat.dem_msg.values()[cmdh.cmd].name() + " @ " + cmdh.tick);
			//Thread.sleep(20);
		ixde++;

		//System.out.println(StringFormat.dumpObj(cmdh));

		// COMMAND HANDLERS
		switch (DemoFormat.dem_msg.values()[cmdh.cmd]) {
			case dem_synctick:
				break;

			case dem_stop: {
				demofinished = true;
				return false;
			}

			case dem_consolecmd: {
				ProtoBuf.readIBytes(stream);
			}
				break;

			case dem_datatables: {		
				byte[] chunk = ProtoBuf.readIBytes(stream);
				ByteBuffer chunkbuf = ByteBuffer.wrap(chunk);
				chunkbuf.order(ByteOrder.LITTLE_ENDIAN);
				
				while(chunkbuf.remaining() > 0) {
					int type = ProtoBuf.readVarInt32(chunkbuf);
					// System.out.println(chunkbuf.position() + " / " + chunkbuf.capacity());
					if (type != SVC_Messages.svc_SendTable.ordinal()) {
						if (type < SVC_Messages.values().length && type > 0)
							throw new Exception("Expected SendTable, got " + SVC_Messages.values()[type]);
						else
							throw new Exception("Expected SendTable, got " + type);
					}
					
					byte[] msg = ProtoBuf.readVBytes(chunkbuf);
					ByteBuffer msgbuf = ByteBuffer.wrap(msg);
					chunkbuf.order(ByteOrder.LITTLE_ENDIAN);
					SendTable sendTable = new SendTable().parse(msgbuf);
					
					if(sendTable.isEnd > 0) break;
					sendTables.add(sendTable);
					//System.out.println(sendTable.netTableName);
				}

			}
				break;

			case dem_stringtables: {
				byte[] chunk = ProtoBuf.readIBytes(stream);
				// if(true) break;
				//ByteBuffer chunkbuf = ByteBuffer.wrap(chunk);
				//chunkbuf.order(ByteOrder.LITTLE_ENDIAN);
				
				//System.out.println(chunk.length * 8);
				ArrayDataStream chunkbuf = new ArrayDataStream(chunk);
				
				//System.exit(0);
				int numTables = chunkbuf.get() & 0xff;
				for(int i = 0; i < numTables; i++) {
					//String tableName = readCString(chunkbuf);
					String tableName = chunkbuf.getString();
					
					int numEntries = (chunkbuf.get() & 0xFF) | ((chunkbuf.get() & 0xFF) << 8);
					// int numEntries = chunkbuf.getShort() & 0xffff;
					// System.out.println(numEntries + " entries in " + tableName + " (" + i + "/" + (numTables - 1) + ") " + "   @ " + chunkbuf.bitIndex());
					
					for(int entryIndex = 0; entryIndex < numEntries; entryIndex++) {
						String entry = chunkbuf.getString();
						// System.out.println(" > " + entry + "   @ " + chunkbuf.bitIndex() + ", #" + entryIndex);
						//dumpBuf(chunkbuf, 16);
						if(chunkbuf.getBitBoolean()) { // hasUserData
							/*dumpBuf(chunkbuf, 16);
							int lower = chunkbuf.getByte() & 0xff;
							int upper = chunkbuf.getByte() & 0xff;
							chunkbuf.back(8 * 2);
							int tog = chunkbuf.getBits(16, false);
							System.out.println(StringFormat.bin(tog) + " = " + StringFormat.hex(tog));
							chunkbuf.back(8 * 2);
							
							int togc = lower | upper << 8;
							System.out.println(StringFormat.bin(togc) + " = " + StringFormat.hex(togc));
							
							System.out.println(StringFormat.hex(lower) + " / " + StringFormat.hex(upper));*/
							int userDataSize = chunkbuf.getUShort();
							
							// System.out.println(userDataSize + " = " + StringFormat.hex(userDataSize));
							byte[] userData = new byte[userDataSize];
							chunkbuf.get(userData);
						}
						// System.out.println(" P " + entry + "   @ " + chunkbuf.bitIndex() + ", #" + entryIndex);
					}
					// System.out.println(".@ " + chunkbuf.bitIndex());

					// handle client-side entries
					if(chunkbuf.getBitBoolean()) {
						//System.out.println(tableName + " has client-side entries: ");
						int numStrings = chunkbuf.getUShort();
						
						for(int x = 0; x < numStrings; x++) {
							String entry = chunkbuf.getString();
							// System.out.println(" >  " + entry + "   @ " + chunkbuf.bitIndex() + ", #" + x);
							
							if(chunkbuf.getBitBoolean()) {
								int userDataSize = chunkbuf.getUShort();
								System.out.println(userDataSize + " = " + StringFormat.hex(userDataSize));
								byte[] userData = new byte[userDataSize];
								chunkbuf.get(userData);
							}
							
							// System.out.println(" @ " + chunkbuf.bitIndex() + ", #" + x);

						}
					}
					//dumpBuf(chunkbuf, 16);
					//if(i >= 0) System.exit(0);
				}
			}
				break;

			case dem_usercmd: {
				stream.getInt(); // outgoing sequence
				ProtoBuf.readIBytes(stream);
			}
				break;

			case dem_signon:
			case dem_packet: {
				HandleDemoPacket();
			}
				break;
			case dem_lastcmd:
				System.out.println("Demo ended! Remaining: " + stream.remaining());
				return true;
			default:
				throw new Exception("Unrecognized command: " + cmdh.cmd);
		}
		
		if (DemoFormat.dem_msg.values()[cmdh.cmd] == DemoFormat.dem_msg.dem_stringtables) 
			return false;

		return true;
	}

	public boolean readCmdHeader() {
		try {
			cmdh.readFrom(stream);
		} catch (BufferUnderflowException ex) {
			ex.printStackTrace(System.err);
			System.err.println(stream.position() + " - " + stream.capacity() + " = " + stream.remaining());
		}
		if (cmdh.cmd <= 0) {
			System.out.println("Missing end tag in demo file.");
			cmd = DemoFormat.dem_msg.dem_stop;
			return false;
		}
		if (cmdh.cmd < 1 || cmdh.cmd > DemoFormat.dem_msg.dem_lastcmd.ordinal()) {
			System.err.println("Assert?");
			return false;
		}
		demotick = cmdh.tick;
		return true;
	}

	void HandleDemoPacket() throws Exception {
		for (DemoFormat.democmdinfo_t cmdinfo : cmdinfoarr)
			cmdinfo.readFrom(stream);
		
		stream.getInt(); // SeqNrIn
		stream.getInt(); // SeqNrOut

		int start = stream.position();
		byte[] chunk = ProtoBuf.readIBytes(stream);
		if(true) return;
		ByteBuffer chunkbuf = ByteBuffer.wrap(chunk);
		
		while(chunkbuf.remaining() > 0) {
			int cmd = ProtoBuf.readVarInt32(chunkbuf);
			
			byte[] byteArraydata = ProtoBuf.readVBytes(chunkbuf);
			System.out.println(cmd);
			// Const.NET_MAX_PAYLOAD
		}
		//if (size > Const.NET_MAX_PAYLOAD)
		//	throw new IllegalArgumentException("HandleDemoPacket: buffer overflow: " + size + " / " + Const.NET_MAX_PAYLOAD);

		//System.out.println(StringFormat.dumpObj(data, false));

	}
	
	public static String readCString(ByteBuffer stream) {
		StringBuilder sb = new StringBuilder();
		while(stream.remaining() > 0) {
			char c = (char) (stream.get() & 0xFF);
			if(c == '\0') break;
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void dumpBuf(ByteBuffer stream, int len) {
		byte[] data = new byte[len];
		stream.get(data);
		stream.position(stream.position() - len);
		for(byte b : data) {
			System.out.print(StringFormat.bin(b & 0xFF) + " ");
		}
		System.out.println();
		for(byte b : data) {
			System.out.print((b & 0xFF) + " ");
		}
		System.out.println();
		for(byte b : data) {
			if(b > 31)
				System.out.print((char)(b & 0xFF));
			else
				System.out.print(' ');
		}
		System.out.println();
		System.out.println(StringFormat.dumpObj(data, false));
	}
	
	public static void dumpBuf(ArrayDataStream stream, int len) {
		byte[] data = new byte[len];
		stream.get(data);
		stream.back(len * 8);
		for(byte b : data) {
			System.out.print(StringFormat.bin(b & 0xFF) + " ");
		}
		System.out.println();
		for(byte b : data) {
			System.out.print((b & 0xFF) + " ");
		}
		System.out.println();
		for(byte b : data) {
			if(b > 31)
				System.out.print((char)(b & 0xFF));
			else
				System.out.print(' ');
		}
		System.out.println();
		System.out.println(StringFormat.dumpObj(data, false));
	}
	
	public static void printByteArrAsString(byte[] arr) {
		for(byte b : arr) {
			if(b > 31)
				System.out.print((char)(b & 0xFF));
			else
				System.out.print(' ');
		}
	}

}
