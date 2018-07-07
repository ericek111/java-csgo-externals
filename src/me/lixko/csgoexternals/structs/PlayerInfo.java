package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.util.BufferStruct;

public class PlayerInfo extends BufferStruct {
	public long unknown;
	public long xuid; // network xuid
	@StringLength(size = Const.MAX_PLAYER_NAME_LENGTH)
	public String name; // scoreboard information
	public int userID; // local server user ID, unique while server is running
	@StringLength(size = Const.SIGNED_GUID_LEN + 1)
	public String guid; // global unique player identifer
	@UnsignedField(4)
	public long friendsID; // friends identification number
	@StringLength(size = Const.MAX_PLAYER_NAME_LENGTH)
	public String friendsName; // friends name
	public boolean fakeplayer; // true, if player is a bot controlled by game.dll
	public boolean ishltv; // true if player is the HLTV proxy
	// public boolean isreplay; // // true if player is the Replay proxy
	public long[] customFiles = new long[Const.MAX_CUSTOM_FILES]; // CRC32_t
	@UnsignedField(1)
	public int filesDownloaded;
}
