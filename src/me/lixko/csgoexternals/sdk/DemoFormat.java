package me.lixko.csgoexternals.sdk;

import me.lixko.csgoexternals.util.BufferStruct;
import java.nio.charset.StandardCharsets;

public class DemoFormat {
	public static final int MAX_OSPATH = 260;
	public static final String DEMO_HEADER_ID_STR = "HL2DEMO";
	public static final byte[] DEMO_HEADER_ID = "HL2DEMO\0".getBytes();
	public static final int DEMO_PROTOCOL = 4;

	public static final int FDEMO_NORMAL = 0;
	public static final int FDEMO_USE_ORIGIN2 = (1 << 0);
	public static final int FDEMO_USE_ANGLES2 = (1 << 1);
	public static final int FDEMO_NOINTERP = (1 << 2); // don't interpolate between this an last view
	public static final int MAX_SPLITSCREEN_CLIENTS = 2;

	public enum dem_msg {
		dem_0,
		// it's a startup message, process as fast as possible
		dem_signon,
		// it's a normal network packet that we stored off
		dem_packet,
		// sync client clock to demo tick
		dem_synctick,
		// console command 
		dem_consolecmd,
		// user input command
		dem_usercmd,
		// network data tables
		dem_datatables,
		// end of time.
		dem_stop,
		// a blob of binary data
		dem_customData,
		dem_stringtables,
		// Last command
		dem_lastcmd
	};

	public static class demoheader_t extends BufferStruct {
		public byte[] demofilestamp = new byte[8]; // Should be HL2DEMO
		//@StringLength(size = 8, charset = "UTF-8")
		//public String demofilestamp = "";
		public int demoprotocol; // Should be DEMO_PROTOCOL
		public int networkprotocol; // Should be PROTOCOL_VERSION
		public byte[] servername = new byte[MAX_OSPATH]; // Name of server
		public byte[] clientname = new byte[MAX_OSPATH]; // Name of client who recorded the game
		public byte[] mapname = new byte[MAX_OSPATH]; // Name of map
		public byte[] gamedirectory = new byte[MAX_OSPATH]; // Name of game directory (com_gamedir)
		public float playback_time; // Time of track
		public int playback_ticks; // # of ticks in track
		public int playback_frames; // # of frames in track
		public int signonlength; // length of sigondata in bytes
	}

	public static class democmdheader_t extends BufferStruct {
		public byte cmd;
		public int tick;
		public byte playerSlot;
	}

	public static class democmdinfo_t extends BufferStruct {
		public int flags;

		// original origin/viewangles
		public float[] viewOrigin = new float[3];
		public float[] viewAngles = new float[3];
		public float[] localViewAngles = new float[3];

		// Resampled origin/viewangles
		public float[] viewOrigin2 = new float[3];
		public float[] viewAngles2 = new float[3];
		public float[] localViewAngles2 = new float[3];

		public void init() {
			flags = FDEMO_NORMAL;
		}

		public float[] GetViewOrigin() {
			if ((flags & FDEMO_USE_ORIGIN2) > 0)
				return viewOrigin2;
			return viewOrigin;
		}

		public float[] GetViewAngles() {
			if ((flags & FDEMO_USE_ANGLES2) > 0)
				return viewAngles2;
			return viewAngles;
		}

		public float[] GetLocalViewAngles() {
			if ((flags & FDEMO_USE_ANGLES2) > 0)
				return localViewAngles2;
			return localViewAngles;
		}

		public void reset() {
			flags = 0;
			viewOrigin2 = viewOrigin;
			viewAngles2 = viewAngles;
			localViewAngles2 = localViewAngles;
		}
	}

	public enum NET_Messages {
		net_NOP,
		net_Disconnect,
		net_File,
		net_SplitScreenUser,
		net_Tick,
		net_StringCmd,
		net_SetConVar,
		net_SignonState
	}

	public enum SVC_Messages {
		_0,
		_1,
		_2,
		_3,
		_4,
		_5,
		_6,
		_7,
		svc_ServerInfo,
		svc_SendTable,
		svc_ClassInfo,
		svc_SetPause,
		svc_CreateStringTable,
		svc_UpdateStringTable,
		svc_VoiceInit,
		svc_VoiceData,
		svc_Print,
		svc_Sounds,
		svc_SetView,
		svc_FixAngle,
		svc_CrosshairAngle,
		svc_BSPDecal,
		svc_SplitScreen,
		svc_UserMessage,
		svc_EntityMessage,
		svc_GameEvent,
		svc_PacketEntities,
		svc_TempEntities,
		svc_Prefetch,
		svc_Menu,
		svc_GameEventList,
		svc_GetCvarValue,
		svc_PaintmapData,
		svc_CmdKeyValues,
		svc_EncryptedData
	}
}
