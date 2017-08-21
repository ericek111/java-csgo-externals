package me.lixko.csgoexternals.sdk;

public enum BoneList {
	BONE_PELVIS(2),
	BONE_SPINE0(3),
	BONE_SPINE1(4),
	BONE_SPINE2(5),
	BONE_SPINE3(6),
	BONE_NECK(7),
	BONE_HEAD(8),
	// BONE_L_UPPER_ARM = 11,
	// BONE_L_FOREARM = 12,
	// BONE_R_UPPER_ARM = 41,
	// BONE_R_FOREARM = 42,
	// BONE_L_THIGH = 70,
	// BONE_L_CALF = 71,
	// BONE_R_THIGH = 77,
	// BONE_R_CALF = 78,
	BONE_MAX(9);

	public int index = 0;

	BoneList(int index) {
		this.index = index;
	}
}
