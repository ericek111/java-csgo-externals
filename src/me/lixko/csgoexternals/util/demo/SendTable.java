package me.lixko.csgoexternals.util.demo;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import me.lixko.csgoexternals.util.StringFormat;

public class SendTable {

	public int isEnd;
	public int needsDecoder;
	public String netTableName;
	public ArrayList<SendProp> sendProps = new ArrayList<>();
	
	public SendTable parse(ByteBuffer chunk) throws Exception {
		return parse(chunk, chunk.remaining());
	}
	
	public SendTable parse(ByteBuffer stream, int length) throws Exception {
		sendProps = new ArrayList<>();

		int read = 0;
		int start = stream.position();
		if (length > stream.remaining())
			throw new Exception("Buffer underflow: " + length + " - " + stream.remaining() + " = " + (length - stream.remaining()));

		while (read < length) {
			int desc = ProtoBuf.readVarInt32(stream);
			int wireType = desc & 7;
			int fieldNum = desc >> 3;

			if (wireType == 2) {
				if (fieldNum == 2) {
					netTableName = ProtoBuf.readString(stream);
				} else if (fieldNum == 4) {
					// Props are special.
					// We'll simply hope that gaben is nice and sends props last, just like he should.
					// System.out.println("___________________________________________________");
					int len = ProtoBuf.readVarInt32(stream);
					sendProps.add(new SendProp().parse(stream, len));
				}
			} else if (wireType == 0) {
				int val = ProtoBuf.readVarInt32(stream);
				switch (fieldNum) {
					case 1:
						isEnd = val;
						break;
					case 3:
						needsDecoder = val;
						break;
					default:
						// silently drop
						break;
				}
			} else {
				throw new Exception("Invalid data: " + wireType + " / " + fieldNum);
			}
			read = stream.position() - start;
		}
		return this;
	}

	public class SendProp {

		public int type;
		public String varName;
		public int flags;
		public int priority;
		public String DTName;
		public int numElements;
		public float lowValue;
		public float highValue;
		public int numBits;

		public SendProp parse(ByteBuffer chunk) throws Exception {
			return parse(chunk, chunk.remaining());
		}
		
		public SendProp parse(ByteBuffer stream, int length) throws Exception {
			int start = stream.position();
			int read = 0;

			if (length > stream.remaining())
				throw new Exception("Buffer underflow: " + length + " - " + stream.remaining() + " = " + (length - stream.remaining()));
			
			// https://developers.google.com/protocol-buffers/docs/encoding#structure
			// System.out.println(" > " + length + " @ " + stream.position());
			while (read < length) {
				int desc = ProtoBuf.readVarInt32(stream);
				int wireType = desc & 7;
				int fieldNum = desc >> 3;

				if (wireType == 2) {
					if (fieldNum == 2) {
						varName = ProtoBuf.readString(stream);
					} else if (fieldNum == 5) {
						DTName = ProtoBuf.readString(stream);
					} else {
						// int len = ProtoBuf.readVarInt32(stream);
						throw new Exception("Invalid fieldNum (" + fieldNum + "), maybe they added a new big field?");
					}
				} else if (wireType == 0) {
					//if(fieldNum == 9) System.out.println("0: " + fieldNum + " | " + read + " / " + length + ", left " + stream.remaining());
					
					int val = ProtoBuf.readVarInt32_simple(stream);
					switch (fieldNum) {
						case 1:
							type = val;
							break;
						case 3:
							flags = val;
							break;
						case 4:
							priority = val;
							break;
						case 6:
							numElements = val;
							break;
						case 9:
							numBits = val;
							break;
						default:
							// silently drop
							break;
					}
				} else if (wireType == 5) {
					float val = stream.getFloat();

					switch (fieldNum) {
						case 7:
							lowValue = val;
							break;
						case 8:
							highValue = val;
							break;
						default:
							// silently drop
							break;
					}
				} else {
					throw new Exception("Invalid data: " + wireType + " / " + fieldNum);
				}
				read = stream.position() - start;
				//System.out.println(read + " / " + length + "   |   " + wireType + " / " + fieldNum);
			}
			return this;
		}
	}
}
