package me.lixko.csgoexternals.offsets;

import me.lixko.csgoexternals.elf.GameInterface;

public class Convar {
	
	private long base = 0;
	private String name;
	private final GameInterface cvarif;
	
	public Convar(GameInterface materialModule, long base, String name) {
		this.base = base;
		this.cvarif = materialModule;
		this.name = name;
	}

	public long getBase() {
		return this.base;
	}
	
	public String getName() {
		return this.name;
	}
	
	public GameInterface getGameInterface() {
		return this.cvarif;
	}
	
	public int getInt() {
		return (int) (this.cvarif.getModule().readInt(this.base + 0x58) ^ this.base);
	}
	
	public float getFloat() {
		return Float.intBitsToFloat((int) (this.cvarif.getModule().readInt(this.base + 0x54) ^ this.base));
	}
	
	public String getString() {
		long strptr = this.cvarif.getModule().readLong(this.base + 0x48);
		return this.cvarif.getModule().readString(strptr, 1024);
	}
	
	public void setInt(int val) throws Exception {
		if (true) {
			throw new Exception("This is detected! You must write into a backup register, too. See: https://www.unknowncheats.me/forum/3556676-post25.html");
		}
		this.cvarif.getModule().writeInt(this.base + 0x58, (int) (val ^ this.base));
	}
	
	public void setFloat(float val) throws Exception {
		if (true) {
			throw new Exception("This is detected! You must write into a backup register, too.");
		}
		this.cvarif.getModule().writeInt(this.base + 0x58, (int) (Float.floatToIntBits(val) ^ this.base));
	}
	
	public void setString(String str) {
		// TODO: Not yet implemented
	}
}
 