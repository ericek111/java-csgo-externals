package me.lixko.csgoexternals.offsets;

public class InputOffsets {

	public long use;
	public long jump;
	public long duck;
	public long alt1; // <<<
	public long alt2;
	public long score;
	public long attack2;
	public long attack; // <<<
	public long moveright;
	public long moveleft;
	public long forward;
	public long strafe;
	public long jlook;
	public long walk;
	public long speed;
	public long back;
	public long commandermousemove;

	public void init() {
		use = alt1 - 0xC * 3;
		jump = alt1 - 0xC * 2;
		duck = alt1 - 0xC * 1;
		alt2 = alt1 + 0xC;
		score = duck + 0x40;

		attack2 = attack - 0xC;
		moveright = attack + 0x48;
		moveleft = attack + 0x48 + 0xC * 1;
		// ??? = attack + 0x48 + 0xC * 2;
		forward = attack + 0x48 + 0xC * 3;
		back = attack + 0x48 + 0xC * 4;
		commandermousemove = attack + 0x48 + 0xC * 5;
		strafe = attack + 0x48 + 0xC * 6;
		jlook = attack + 0x48 + 0xC * 7;
		walk = attack + 0x48 + 0xC * 8;
		speed = attack + 0x48 + 0xC * 9;
	}
}
