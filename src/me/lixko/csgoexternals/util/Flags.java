package me.lixko.csgoexternals.util;

public class Flags {
	public static final int TEAM_SPECTATOR = 		1;
	public static final int TEAM_T = 				2;
	public static final int TEAM_CT =				3;

	// m_lifeState values
	public static final int LIFE_ALIVE =			0; // alive
	public static final int LIFE_DYING =			1; // playing death animation or still falling off of a ledge waiting to hit ground
	public static final int LIFE_DEAD =				2; // dead. lying still.
	public static final int LIFE_RESPAWNABLE =		3;
	public static final int LIFE_DISCARDBODY =		4;

	// CBaseEntity::m_fFlags
	// PLAYER SPECIFIC FLAGS FIRST BECAUSE WE USE ONLY A FEW BITS OF NETWORK PRECISION
	public static final int FL_ONGROUND =			(1<<0);  // At rest / on the ground
	public static final int FL_DUCKING =			(1<<1);  // Player flag -- Player is fully crouched
	public static final int FL_WATERJUMP =			(1<<3);  // player jumping out of water
	public static final int FL_ONTRAIN =			(1<<4);  // Player is _controlling_ a train, so movement commands should be ignored on client during prediction.
	public static final int FL_INRAIN =				(1<<5);  // Indicates the entity is standing in rain
	public static final int FL_FROZEN =				(1<<6); // Player is frozen for 3rd person camera
	public static final int FL_ATCONTROLS = 		(1<<7); // Player can't move, but keeps key inputs for controlling another entity
	public static final int FL_CLIENT =				(1<<8);  // Is a player
	public static final int FL_FAKECLIENT =			(1<<9);  // Fake client, simulated server side; don't send network messages to them
	public static final int FL_INWATER =			(1<<10); // In water

	// NON-PLAYER SPECIFIC (i.e., not used by GameMovement or the client .dll ) -- Can still be applied to players, though
	public static final int FL_FLY =				(1<<11); // Changes the SV_Movestep() behavior to not need to be on ground
	public static final int FL_SWIM =				(1<<12); // Changes the SV_Movestep() behavior to not need to be on ground (but stay in water)
	public static final int FL_CONVEYOR =			(1<<13);
	public static final int FL_NPC =				(1<<14);
	public static final int FL_GODMODE =			(1<<15);
	public static final int FL_NOTARGET =			(1<<16);
	public static final int FL_AIMTARGET =			(1<<17); // set if the crosshair needs to aim onto the entity
	public static final int FL_PARTIALGROUND =		(1<<18); // not all corners are valid
	public static final int FL_STATICPROP =			(1<<19); // Eetsa static prop!       
	public static final int FL_GRAPHED =			(1<<20); // worldgraph has this ent listed as something that blocks a connection
	public static final int FL_GRENADE =			(1<<21);
	public static final int FL_STEPMOVEMENT =		(1<<22); // Changes the SV_Movestep() behavior to not do any processing
	public static final int FL_DONTTOUCH =			(1<<23); // Doesn't generate touch functions, generates Untouch() for anything it was touching when this flag was set
	public static final int FL_BASEVELOCITY =		(1<<24); // Base velocity has been applied this frame (used to convert base velocity into momentum)
	public static final int FL_WORLDBRUSH =			(1<<25); // Not moveable/removeable brush entity (really part of the world, but represented as an entity for transparency or something)
	public static final int FL_OBJECT =				(1<<26); // Terrible name. This is an object that NPCs should see. Missiles, for example.
	public static final int FL_KILLME =				(1<<27); // This entity is marked for death -- will be freed by game DLL
	public static final int FL_ONFIRE =				(1<<28); // You know...
	public static final int FL_DISSOLVING =			(1<<29); // We're dissolving!
	public static final int FL_TRANSRAGDOLL =		(1<<30); // In the process of turning into a client side ragdoll.
	public static final int FL_UNBLOCKABLE_BY_PLAYER = (1<<31); // pusher that can't be blocked by the player
}
