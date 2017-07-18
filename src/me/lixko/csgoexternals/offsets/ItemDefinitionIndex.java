package me.lixko.csgoexternals.offsets;

import java.util.HashMap;

public enum ItemDefinitionIndex {
	WEAPON_NONE(0, AttributableItemType.OTHER),
	WEAPON_DEAGLE(1, AttributableItemType.PISTOL),
	WEAPON_ELITE(2, AttributableItemType.PISTOL),
	WEAPON_FIVESEVEN(3, AttributableItemType.PISTOL),
	WEAPON_GLOCK(4, AttributableItemType.PISTOL),
	WEAPON_AK47(7, AttributableItemType.AUTOMATIC),
	WEAPON_AUG(8, AttributableItemType.AUTOMATIC),
	WEAPON_AWP(9, AttributableItemType.SNIPER),
	WEAPON_FAMAS(10, AttributableItemType.AUTOMATIC),
	WEAPON_G3SG1(11, AttributableItemType.AUTOMATIC),
	WEAPON_GALILAR(13, AttributableItemType.AUTOMATIC),
	WEAPON_M249(14, AttributableItemType.AUTOMATIC),
	WEAPON_M4A1(16, AttributableItemType.AUTOMATIC),
	WEAPON_MAC10(17, AttributableItemType.AUTOMATIC),
	WEAPON_P90(19, AttributableItemType.AUTOMATIC),
	WEAPON_UMP45(24, AttributableItemType.AUTOMATIC),
	WEAPON_XM1014(25, AttributableItemType.SHOTGUN),
	WEAPON_BIZON(26, AttributableItemType.AUTOMATIC),
	WEAPON_MAG7(27, AttributableItemType.SHOTGUN),
	WEAPON_NEGEV(28, AttributableItemType.AUTOMATIC),
	WEAPON_SAWEDOFF(29, AttributableItemType.SHOTGUN),
	WEAPON_TEC9(30, AttributableItemType.PISTOL),
	WEAPON_TASER(31, AttributableItemType.OTHER),
	WEAPON_HKP2000(32, AttributableItemType.PISTOL),
	WEAPON_MP7(33, AttributableItemType.AUTOMATIC),
	WEAPON_MP9(34, AttributableItemType.AUTOMATIC),
	WEAPON_NOVA(35, AttributableItemType.SHOTGUN),
	WEAPON_P250(36, AttributableItemType.PISTOL),
	WEAPON_SCAR20(38, AttributableItemType.AUTOMATIC),
	WEAPON_SG556(39, AttributableItemType.AUTOMATIC),
	WEAPON_SSG08(40, AttributableItemType.SNIPER),
	WEAPON_KNIFE(42, AttributableItemType.KNIFE),
	WEAPON_FLASHBANG(43, AttributableItemType.GRENADE),
	WEAPON_HEGRENADE(44, AttributableItemType.GRENADE),
	WEAPON_SMOKEGRENADE(45, AttributableItemType.GRENADE),
	WEAPON_MOLOTOV(46, AttributableItemType.GRENADE),
	WEAPON_DECOY(47, AttributableItemType.GRENADE),
	WEAPON_INCGRENADE(48, AttributableItemType.GRENADE),
	WEAPON_C4(49, AttributableItemType.OTHER),
	WEAPON_KNIFE_T(59, AttributableItemType.KNIFE),
	WEAPON_M4A1_SILENCER(60, AttributableItemType.AUTOMATIC),
	WEAPON_USP_SILENCER(61, AttributableItemType.PISTOL),
	WEAPON_CZ75A(63, AttributableItemType.PISTOL),
	WEAPON_REVOLVER(64, AttributableItemType.PISTOL),
	WEAPON_KNIFE_BAYONET(500, AttributableItemType.KNIFE),
	WEAPON_KNIFE_FLIP(505, AttributableItemType.KNIFE),
	WEAPON_KNIFE_GUT(506, AttributableItemType.KNIFE),
	WEAPON_KNIFE_KARAMBIT(507, AttributableItemType.KNIFE),
	WEAPON_KNIFE_M9_BAYONET(508, AttributableItemType.KNIFE),
	WEAPON_KNIFE_TACTICAL(509, AttributableItemType.KNIFE),
	WEAPON_KNIFE_FALCHION(512, AttributableItemType.KNIFE),
	WEAPON_KNIFE_SURVIVAL_BOWIE(514, AttributableItemType.KNIFE),
	WEAPON_KNIFE_BUTTERFLY(515, AttributableItemType.KNIFE),
	WEAPON_KNIFE_PUSH(516, AttributableItemType.KNIFE);

	private final static HashMap<Integer, ItemDefinitionIndex> ids = new HashMap<Integer, ItemDefinitionIndex>();
	private final int id;
	private final AttributableItemType type;

	static {
		if (ids.size() != ItemDefinitionIndex.values().length)
			for (int i = 0; i < values().length; i++) {
				ids.put(values()[i].id, values()[i]);
			}
	}

	ItemDefinitionIndex(int id, AttributableItemType type) {
		this.id = id;
		this.type = type;
	}

	public int id() {
		return id;
	}

	public AttributableItemType type() {
		return type;
	}

	public static ItemDefinitionIndex byID(int x) {
		if (ids.get(x) == null)
			return WEAPON_NONE;
		else
			return ids.get(x);
	}

}
