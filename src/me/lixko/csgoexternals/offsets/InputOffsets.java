package me.lixko.csgoexternals.offsets;

public class InputOffsets {
	
	public long speed;
	public long walk;
	public long jlook;
	public long strafe;
	public long commandermousemove;
	public long forward;
	public long back;
	public long moveleft;
	public long moveright;
	public long graph;
	
	public long klook;
	public long left;
	public long right;
	public long lookup;
	public long lookdown;
	public long use;
	public long jump;
	public long attack; // <<<
	public long attack2;
	public long duck;
	public long reload;
	public long alt1; // <<<
	public long alt2;
	public long score;
	public long in_break;
	public long zoom_in;
	public long zoom_out;
	public long grenade1;
	public long grenade2;
	public long attack3;
	public long duck_toggle;
	

	public void init() {
		
		// https://github.com/ValveSoftware/source-sdk-2013/blob/master/mp/src/game/client/in_main.cpp#L112
		speed = attack + 0xC * 15;
		walk = attack + 0xC * 14;
		jlook = attack + 0xC * 13;
		strafe = attack + 0xC * 12;
		commandermousemove = attack + 0xC * 11;
		forward = attack + 0xC * 9;
		back = attack + 0xC * 8;
		moveleft = attack + 0xC * 7;
		moveright = attack + 0xC * 6;
		graph = attack + 0xC * 5;
		attack2 = attack - 0xC;
		
		klook = alt1 + 0xC * 11;
		left = alt1 + 0xC * 10;
		right = alt1 + 0xC * 9;
		lookup = alt1 + 0xC * 8;
		lookdown = alt1 + 0xC * 7;
		use = alt1 + 0xC * 6;
		jump = alt1 + 0xC * 5;
		duck = alt1 + 0xC * 2;
		reload = alt1 + 0xC * 1;
		// alt1
		alt2 = alt1 - 0xC * 1;
		score = alt1 - 0xC * 2;
		in_break = alt1 - 0xC * 3;
		zoom_in = alt1 - 0xC * 4;
		zoom_out = alt1 - 0xC * 5;
		
		grenade1 = zoom_in;
		grenade2 = zoom_out;
		
	}
}
