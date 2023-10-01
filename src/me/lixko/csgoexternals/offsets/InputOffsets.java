package me.lixko.csgoexternals.offsets;

public class InputOffsets {
	
	public long grenade2; // === zoom_out
	public long grenade1; // === zoom_in
	public long in_break;
	public long score;
	public long alt2;
	public long alt1;
	public long reload;
	public long duck;
	public long movedown;
	public long moveup;
	public long jump;
	public long use;
	public long lookdown;
	public long lookup;
	public long right;
	public long left;
	public long klook;
	

	
	public long zoom;
	public long attack2;
	public long attack;
	public long use_or_reload;
	public long lookspin;
	// 3 unknown items
	public long graph;
	public long moveright;
	public long moveleft;
	public long back;
	public long forward;
	public long commandermousemove;
	public long strafe;
	public long jlook;
	public long walk;
	public long speed;
	

	public void init() {
		
		// https://github.com/ValveSoftware/source-sdk-2013/blob/master/mp/src/game/client/in_main.cpp#L112
		int i = -5; // positions between alt1 and grenade2
		grenade2 = 	alt1 + 0x10 * i++;
		grenade1 = 	alt1 + 0x10 * i++;
		in_break = 	alt1 + 0x10 * i++;
		score = 	alt1 + 0x10 * i++;
		alt2 = 		alt1 + 0x10 * i++;
		alt1 = 		alt1 + 0x10 * i++;
		reload = 	alt1 + 0x10 * i++;
		duck = 		alt1 + 0x10 * i++;
		movedown = 	alt1 + 0x10 * i++;
		moveup = 	alt1 + 0x10 * i++;
		jump = 		alt1 + 0x10 * i++;
		use = 		alt1 + 0x10 * i++;
		lookdown = 	alt1 + 0x10 * i++;
		lookup = 	alt1 + 0x10 * i++;
		right = 	alt1 + 0x10 * i++;
		left = 		alt1 + 0x10 * i++;
		klook = 	alt1 + 0x10 * i++;
				
		int j = -2; // positions between attack and zoom
		zoom = 		attack + 0x10 * j++;
		attack2 = 	attack + 0x10 * j++;
		attack = 	attack + 0x10 * j++;
		use_or_reload = attack + 0x10 * j++;
		lookspin = 	attack + 0x10 * j++;
		j += 3; // 3 unknown items
		graph = 	attack + 0x10 * j++;
		moveright = attack + 0x10 * j++;
		moveleft = 	attack + 0x10 * j++;
		back = 		attack + 0x10 * j++;
		forward = 	attack + 0x10 * j++;
		commandermousemove = attack + 0x10 * j++;
		strafe = 	attack + 0x10 * j++;
		jlook = 	attack + 0x10 * j++;
		walk = 		attack + 0x10 * j++;
		speed = 	attack + 0x10 * j++;
		
		// TODO: Hash registers, some anti-cheat measure? Should the cmd be also written to them (-0x8 for all)?
		
	}
}
