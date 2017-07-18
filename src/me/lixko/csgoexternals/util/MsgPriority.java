package me.lixko.csgoexternals.util;

public enum MsgPriority {
	ALL(Integer.MAX_VALUE),
	TOGGLESTATUS(1000),
	CONFNOCHANGE(970),
	CONFCHANGE(950),
	MODULESGENERAL(750),
	MODULESWARNINGS(600),
	GENERAL(500),
	INFO(400),
	CLIENT(300),
	WARNINGS(200),
	SYNTAXERRORS(150),
	ERRORS(100),
	MODULESEXPLICIT(50),
	EXPLICIT(20),
	MUTE(0);

	public int value;

	private MsgPriority(int value) {
		this.value = value;
	}
}