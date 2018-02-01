package me.lixko.csgoexternals.sdk;

public class Const {
	// \(\s*([0-9]*)\s*<*\s([0-9]*)\s\)
	// (#define)\s*(\w*)\s*([(0-9 <\-*+/'|)\w]*) > public static final int $2 = $3;
	// (\w*)\s*=\s*([0-9]*), > $1,
	// the command line param that tells the engine to use steam
	public static final String STEAM_PARM = "-steam";
	// the command line param to tell dedicated server to restart
	// if they are out of date
	public static final String AUTO_RESTART = "-autoupdate";

	// the message a server sends when a clients steam login is expired
	public static final String INVALID_STEAM_TICKET = "Invalid STEAM UserID Ticket\n";
	public static final String INVALID_STEAM_VACBANSTATE = "VAC banned from secure server\n";
	public static final String INVALID_STEAM_LOGGED_IN_ELSEWHERE = "This Steam account is being used in another location\n";
	public static final String INVALID_STEAM_LOGON_NOT_CONNECTED = "Client not connected to Steam\n";
	public static final String INVALID_STEAM_LOGON_TICKET_CANCELED = "Client left game (Steam auth ticket has been canceled)\n";

	public static final String CLIENTNAME_TIMED_OUT = "%s timed out";

	// This is the default, see shareddefs.h for mod-specific value, which can override this
	public static final float DEFAULT_TICK_INTERVAL = 0.015f; // 15 msec is the default
	public static final float MINIMUM_TICK_INTERVAL = 0.001f;
	public static final float MAXIMUM_TICK_INTERVAL = 0.1f;

	// Demo stuff:
	public static final int NET_MAX_PAYLOAD = (262144 - 4); // largest message we can send in bytes
	public static final int DEMO_RECORD_BUFFER_SIZE = (2 * 1024 * 1024); // temp buffer big enough to fit both string tables and server classes

	// This is the max # of players the engine can handle
	public static final int ABSOLUTE_PLAYER_LIMIT = 255; // not 256, so we can send the limit as a byte
	public static final int ABSOLUTE_PLAYER_LIMIT_DW = ((ABSOLUTE_PLAYER_LIMIT / 32) + 1);

	// a player name may have 31 chars + 0 on the PC.
	// the 360 only allows 15 char + 0, but stick with the larger PC size for cross-platform communication
	public static final int MAX_PLAYER_NAME_LENGTH = 32;

	public static final int MAX_PLAYERS_PER_CLIENT = 1; // One player per PC

	// Max decorated map name, with things like workshop/cp_foo.ugc123456
	public static final int MAX_MAP_NAME = 96;

	// Max name used in save files. Needs to be left at 32 for SourceSDK compatibility.
	public static final int MAX_MAP_NAME_SAVE = 32;

	// Max non-decorated map name for e.g. server browser (just cp_foo)
	public static final int MAX_DISPLAY_MAP_NAME = 32;

	public static final int MAX_NETWORKID_LENGTH = 64; // num chars for a network (i.e steam) ID

	// BUGBUG: Reconcile with or derive this from the engine's internal definition!
	// FIXME: I added an extra bit because I needed to make it signed
	public static final int SP_MODEL_INDEX_BITS = 13;

	// How many bits to use to encode an edict.
	public static final int MAX_EDICT_BITS = 11; // # of bits needed to represent max edicts
	// Max # of edicts in a level
	public static final int MAX_EDICTS = (1 << MAX_EDICT_BITS);

	// How many bits to use to encode an server class index
	public static final int MAX_SERVER_CLASS_BITS = 9;
	// Max # of networkable server classes
	public static final int MAX_SERVER_CLASSES = (1 << MAX_SERVER_CLASS_BITS);

	public static final int SIGNED_GUID_LEN = 32; // Hashed CD Key (32 hex alphabetic chars + 0 terminator )

	// Used for networking ehandles.
	public static final int NUM_ENT_ENTRY_BITS = (MAX_EDICT_BITS + 1);
	public static final int NUM_ENT_ENTRIES = (1 << NUM_ENT_ENTRY_BITS);
	public static final int ENT_ENTRY_MASK = (NUM_ENT_ENTRIES - 1);
	public static final int INVALID_EHANDLE_INDEX = 0xFFFFFFFF;

	public static final int NUM_SERIAL_NUM_BITS = (32 - NUM_ENT_ENTRY_BITS);

	// Networked ehandles use less bits to encode the serial number.
	public static final int NUM_NETWORKED_EHANDLE_SERIAL_NUMBER_BITS = 10;
	public static final int NUM_NETWORKED_EHANDLE_BITS = (MAX_EDICT_BITS + NUM_NETWORKED_EHANDLE_SERIAL_NUMBER_BITS);
	public static final int INVALID_NETWORKED_EHANDLE_VALUE = ((1 << NUM_NETWORKED_EHANDLE_BITS) - 1);

	// This is the maximum amount of data a PackedEntity can have. Having a limit allows us
	// to use static arrays sometimes instead of allocating memory all over the place.
	public static final int MAX_PACKEDENTITY_DATA = 16384;

	// This is the maximum number of properties that can be delta'd. Must be evenly divisible by 8.
	public static final int MAX_PACKEDENTITY_PROPS = 4096;

	// a client can have up to 4 customization files (logo, sounds, models, txt).
	public static final int MAX_CUSTOM_FILES = 4; // max 4 files
	public static final int MAX_CUSTOM_FILE_SIZE = 524288; // Half a megabyte

	//
	// Constants shared by the engine and dlls
	// This header file included by engine files and DLL files.
	// Most came from server.h

	// CBaseEntity::m_fFlags
	// PLAYER SPECIFIC FLAGS FIRST BECAUSE WE USE ONLY A FEW BITS OF NETWORK PRECISION
	public static final int FL_ONGROUND = (1 << 0); // At rest / on the ground
	public static final int FL_DUCKING = (1 << 1); // Player flag -- Player is fully crouched
	public static final int FL_ANIMDUCKING = (1 << 2); // Player flag -- Player is in the process of crouching or uncrouching but could be in transition
	// examples: Fully ducked: FL_DUCKING & FL_ANIMDUCKING
	// Previously fully ducked, unducking in progress: FL_DUCKING & !FL_ANIMDUCKING
	// Fully unducked: !FL_DUCKING & !FL_ANIMDUCKING
	// Previously fully unducked, ducking in progress: !FL_DUCKING & FL_ANIMDUCKING
	public static final int FL_WATERJUMP = (1 << 3); // player jumping out of water
	public static final int FL_ONTRAIN = (1 << 4); // Player is _controlling_ a train, so movement commands should be ignored on client during prediction.
	public static final int FL_INRAIN = (1 << 5); // Indicates the entity is standing in rain
	public static final int FL_FROZEN = (1 << 6); // Player is frozen for 3rd person camera
	public static final int FL_ATCONTROLS = (1 << 7); // Player can't move, but keeps key inputs for controlling another entity
	public static final int FL_CLIENT = (1 << 8); // Is a player
	public static final int FL_FAKECLIENT = (1 << 9); // Fake client, simulated server side; don't send network messages to them
	// NON-PLAYER SPECIFIC (i.e., not used by GameMovement or the client .dll ) -- Can still be applied to players, though
	public static final int FL_INWATER = (1 << 10); // In water

	// NOTE if you move things up, make sure to change this value
	public static final int PLAYER_FLAG_BITS = 11;

	public static final int FL_FLY = (1 << 11); // Changes the SV_Movestep() behavior to not need to be on ground
	public static final int FL_SWIM = (1 << 12); // Changes the SV_Movestep() behavior to not need to be on ground (but stay in water)
	public static final int FL_CONVEYOR = (1 << 13);
	public static final int FL_NPC = (1 << 14);
	public static final int FL_GODMODE = (1 << 15);
	public static final int FL_NOTARGET = (1 << 16);
	public static final int FL_AIMTARGET = (1 << 17); // set if the crosshair needs to aim onto the entity
	public static final int FL_PARTIALGROUND = (1 << 18); // not all corners are valid
	public static final int FL_STATICPROP = (1 << 19); // Eetsa static prop!
	public static final int FL_GRAPHED = (1 << 20); // worldgraph has this ent listed as something that blocks a connection
	public static final int FL_GRENADE = (1 << 21);
	public static final int FL_STEPMOVEMENT = (1 << 22); // Changes the SV_Movestep() behavior to not do any processing
	public static final int FL_DONTTOUCH = (1 << 23); // Doesn't generate touch functions, generates Untouch() for anything it was touching when this flag was set
	public static final int FL_BASEVELOCITY = (1 << 24); // Base velocity has been applied this frame (used to convert base velocity into momentum)
	public static final int FL_WORLDBRUSH = (1 << 25); // Not moveable/removeable brush entity (really part of the world, but represented as an entity for transparency or something)
	public static final int FL_OBJECT = (1 << 26); // Terrible name. This is an object that NPCs should see. Missiles, for example.
	public static final int FL_KILLME = (1 << 27); // This entity is marked for death -- will be freed by game DLL
	public static final int FL_ONFIRE = (1 << 28); // You know...
	public static final int FL_DISSOLVING = (1 << 29); // We're dissolving!
	public static final int FL_TRANSRAGDOLL = (1 << 30); // In the process of turning into a client side ragdoll.
	public static final int FL_UNBLOCKABLE_BY_PLAYER = (1 << 31); // pusher that can't be blocked by the player

	// m_lifeState values
	public static final int LIFE_ALIVE = 0; // alive
	public static final int LIFE_DYING = 1; // playing death animation or still falling off of a ledge waiting to hit ground
	public static final int LIFE_DEAD = 2; // dead. lying still.
	public static final int LIFE_RESPAWNABLE = 3;
	public static final int LIFE_DISCARDBODY = 4;

	public static final int EF_PARITY_BITS = 3;
	public static final int EF_PARITY_MASK = ((1 << EF_PARITY_BITS) - 1);

	// How many bits does the muzzle flash parity thing get?
	public static final int EF_MUZZLEFLASH_BITS = 2;

	// plats
	public static final int PLAT_LOW_TRIGGER = 1;

	// Trains
	public static final int SF_TRAIN_WAIT_RETRIGGER = 1;
	public static final int SF_TRAIN_PASSABLE = 8; // Train is not solid -- used to make water trains

	// view angle update types for CPlayerState::fixangle
	public static final int FIXANGLE_NONE = 0;
	public static final int FIXANGLE_ABSOLUTE = 1;
	public static final int FIXANGLE_RELATIVE = 2;

	// Break Model Defines

	public static final int BREAK_GLASS = 0x01;
	public static final int BREAK_METAL = 0x02;
	public static final int BREAK_FLESH = 0x04;
	public static final int BREAK_WOOD = 0x08;

	public static final int BREAK_SMOKE = 0x10;
	public static final int BREAK_TRANS = 0x20;
	public static final int BREAK_CONCRETE = 0x40;

	// If this is set, then we share a lighting origin with the last non-slave breakable sent down to the client
	public static final int BREAK_SLAVE = 0x80;

	// Colliding temp entity sounds

	public static final int BOUNCE_GLASS = BREAK_GLASS;
	public static final int BOUNCE_METAL = BREAK_METAL;
	public static final int BOUNCE_FLESH = BREAK_FLESH;
	public static final int BOUNCE_WOOD = BREAK_WOOD;
	public static final int BOUNCE_SHRAP = 0x10;
	public static final int BOUNCE_SHELL = 0x20;
	public static final int BOUNCE_CONCRETE = BREAK_CONCRETE;
	public static final int BOUNCE_SHOTSHELL = 0x80;

	// Temp entity bounce sound types
	public static final int TE_BOUNCE_NULL = 0;
	public static final int TE_BOUNCE_SHELL = 1;
	public static final int TE_BOUNCE_SHOTSHELL = 2;

	public static float SOUND_NORMAL_CLIP_DIST = 1000.0f;

	// How many networked area portals do we allow?
	public static final int MAX_AREA_STATE_BYTES = 32;
	public static final int MAX_AREA_PORTAL_STATE_BYTES = 24;

	// user message max payload size (note, this value is used by the engine, so MODs cannot change it)
	public static final int MAX_USER_MSG_DATA = 255;
	public static final int MAX_ENTITY_MSG_DATA = 255;
}

// edict->movetype values
enum MoveType_t {
	MOVETYPE_NONE, // never moves
	MOVETYPE_ISOMETRIC, // For players -- in TF2 commander view, etc.
	MOVETYPE_WALK, // Player only - moving on the ground
	MOVETYPE_STEP, // gravity, special edge handling -- monsters use this
	MOVETYPE_FLY, // No gravity, but still collides with stuff
	MOVETYPE_FLYGRAVITY, // flies through the air + is affected by gravity
	MOVETYPE_VPHYSICS, // uses VPHYSICS for simulation
	MOVETYPE_PUSH, // no clip to world, push and crush
	MOVETYPE_NOCLIP, // No gravity, no collisions, still do velocity/avelocity
	MOVETYPE_LADDER, // Used by players only when going onto a ladder
	MOVETYPE_OBSERVER, // Observer movement, depends on player's observer mode
	MOVETYPE_CUSTOM, // Allows the entity to describe its own physics

	// should always be defined as the last item in the list
	MOVETYPE_LAST;

	public static final int MOVETYPE_MAX_BITS = 4;
};

// edict->movecollide values
enum MoveCollide_t {
	MOVECOLLIDE_DEFAULT,

	// These ones only work for MOVETYPE_FLY + MOVETYPE_FLYGRAVITY
	MOVECOLLIDE_FLY_BOUNCE, // bounces, reflects, based on elasticity of surface and object - applies friction (adjust velocity)
	MOVECOLLIDE_FLY_CUSTOM, // Touch() will modify the velocity however it likes
	MOVECOLLIDE_FLY_SLIDE, // slides along surfaces (no bounce) - applies friciton (adjusts velocity)

	MOVECOLLIDE_COUNT; // Number of different movecollides

	// When adding new movecollide types, make sure this is correct
	public static final int MOVECOLLIDE_MAX_BITS = 3;
};

// edict->solid values
// NOTE: Some movetypes will cause collisions independent of SOLID_NOT/SOLID_TRIGGER when the entity moves
// SOLID only effects OTHER entities colliding with this one when they move - UGH!

// Solid type basically describes how the bounding volume of the object is represented
// NOTE: SOLID_BBOX MUST BE 2, and SOLID_VPHYSICS MUST BE 6
// NOTE: These numerical values are used in the FGD by the prop code (see prop_dynamic)
enum SolidType_t {
	SOLID_NONE, // no solid model
	SOLID_BSP, // a BSP tree
	SOLID_BBOX, // an AABB
	SOLID_OBB, // an OBB (not implemented yet)
	SOLID_OBB_YAW, // an OBB, constrained so that it can only yaw
	SOLID_CUSTOM, // Always call into the entity for tests
	SOLID_VPHYSICS, // solid vphysics object, get vcollide from the model and collide with that
	SOLID_LAST;

	public static boolean IsSolid(SolidType_t solidType, int nSolidFlags) {
		return (solidType != SOLID_NONE) && ((nSolidFlags & SolidFlags_t.FSOLID_NOT_SOLID) == 0);
	}
};

class SolidFlags_t {
	public static final int FSOLID_CUSTOMRAYTEST = 0x0001; // Ignore solid type + always call into the entity for ray tests
	public static final int FSOLID_CUSTOMBOXTEST = 0x0002; // Ignore solid type + always call into the entity for swept box tests
	public static final int FSOLID_NOT_SOLID = 0x0004; // Are we currently not solid?
	public static final int FSOLID_TRIGGER = 0x0008; // This is something may be collideable but fires touch functions
	// even when it's not collideable (when the FSOLID_NOT_SOLID flag is set)
	public static final int FSOLID_NOT_STANDABLE = 0x0010; // You can't stand on this
	public static final int FSOLID_VOLUME_CONTENTS = 0x0020; // Contains volumetric contents (like water)
	public static final int FSOLID_FORCE_WORLD_ALIGNED = 0x0040; // Forces the collision rep to be world-aligned even if it's SOLID_BSP or SOLID_VPHYSICS
	public static final int FSOLID_USE_TRIGGER_BOUNDS = 0x0080; // Uses a special trigger bounds separate from the normal OBB
	public static final int FSOLID_ROOT_PARENT_ALIGNED = 0x0100; // Collisions are defined in root parent's local coordinate space
	public static final int FSOLID_TRIGGER_TOUCH_DEBRIS = 0x0200; // This trigger will touch debris objects

	public static final int FSOLID_MAX_BITS = 10;

};

// entity effects
class EntityEffects {
	public static final int EF_BONEMERGE = 0x001; // Performs bone merge on client side
	public static final int EF_BRIGHTLIGHT = 0x002; // DLIGHT centered at entity origin
	public static final int EF_DIMLIGHT = 0x004; // player flashlight
	public static final int EF_NOINTERP = 0x008; // don't interpolate the next frame
	public static final int EF_NOSHADOW = 0x010; // Don't cast no shadow
	public static final int EF_NODRAW = 0x020; // don't draw entity
	public static final int EF_NORECEIVESHADOW = 0x040; // Don't receive no shadow
	public static final int EF_BONEMERGE_FASTCULL = 0x080;
	// For use with EF_BONEMERGE. If this is set, then it places this ent's origin at its
	// parent and uses the parent's bbox + the max extents of the aiment.
	// Otherwise, it sets up the parent's bones every frame to figure out where to place
	// the aiment, which is inefficient because it'll setup the parent's bones even if
	// the parent is not in the PVS.
	public static final int EF_ITEM_BLINK = 0x100; // blink an item so that the user notices it.
	public static final int EF_PARENT_ANIMATES = 0x200; // always assume that the parent entity is animating
	public static final int EF_MAX_BITS = 10;
};

// Rendering constants
// if this is changed, update common/MaterialSystem/Sprite.cpp
enum RenderMode_t {
	kRenderNormal, // src
	kRenderTransColor, // c*a+dest*(1-a)
	kRenderTransTexture, // src*a+dest*(1-a)
	kRenderGlow, // src*a+dest -- No Z buffer checks -- Fixed size in screen space
	kRenderTransAlpha, // src*srca+dest*(1-srca)
	kRenderTransAdd, // src*a+dest
	kRenderEnvironmental, // not drawn, used for environmental effects
	kRenderTransAddFrameBlend, // use a fractional frame value to blend between animation frames
	kRenderTransAlphaAdd, // src + dest*(1-a)
	kRenderWorldGlow, // Same as kRenderGlow but not fixed size in screen space
	kRenderNone, // Don't render.

	kRenderModeCount, // must be last
};

enum RenderFx_t {
	kRenderFxNone,
	kRenderFxPulseSlow,
	kRenderFxPulseFast,
	kRenderFxPulseSlowWide,
	kRenderFxPulseFastWide,
	kRenderFxFadeSlow,
	kRenderFxFadeFast,
	kRenderFxSolidSlow,
	kRenderFxSolidFast,
	kRenderFxStrobeSlow,
	kRenderFxStrobeFast,
	kRenderFxStrobeFaster,
	kRenderFxFlickerSlow,
	kRenderFxFlickerFast,
	kRenderFxNoDissipation,
	kRenderFxDistort, // Distort/scale/translate flicker
	kRenderFxHologram, // kRenderFxDistort + distance fade
	kRenderFxExplode, // Scale up really big!
	kRenderFxGlowShell, // Glowing Shell
	kRenderFxClampMinScale, // Keep this sprite from getting very small (SPRITES only!)
	kRenderFxEnvRain, // for environmental rendermode, make rain
	kRenderFxEnvSnow, // " " " , make snow
	kRenderFxSpotlight, // TEST CODE for experimental spotlight
	kRenderFxRagdoll, // HACKHACK: TEST CODE for signalling death of a ragdoll character
	kRenderFxPulseFastWider,
	kRenderFxMax
};

enum Collision_Group_t {
	COLLISION_GROUP_NONE,
	COLLISION_GROUP_DEBRIS, // Collides with nothing but world and static stuff
	COLLISION_GROUP_DEBRIS_TRIGGER, // Same as debris, but hits triggers
	COLLISION_GROUP_INTERACTIVE_DEBRIS, // Collides with everything except other interactive debris or debris
	COLLISION_GROUP_INTERACTIVE, // Collides with everything except interactive debris or debris
	COLLISION_GROUP_PLAYER,
	COLLISION_GROUP_BREAKABLE_GLASS,
	COLLISION_GROUP_VEHICLE,
	COLLISION_GROUP_PLAYER_MOVEMENT, // For HL2, same as Collision_Group_Player, for
	// TF2, this filters out other players and CBaseObjects
	COLLISION_GROUP_NPC, // Generic NPC group
	COLLISION_GROUP_IN_VEHICLE, // for any entity inside a vehicle
	COLLISION_GROUP_WEAPON, // for any weapons that need collision detection
	COLLISION_GROUP_VEHICLE_CLIP, // vehicle clip brush to restrict vehicle movement
	COLLISION_GROUP_PROJECTILE, // Projectiles!
	COLLISION_GROUP_DOOR_BLOCKER, // Blocks entities not permitted to get near moving doors
	COLLISION_GROUP_PASSABLE_DOOR, // Doors that the player shouldn't collide with
	COLLISION_GROUP_DISSOLVING, // Things that are dissolving are in this group
	COLLISION_GROUP_PUSHAWAY, // Nonsolid on client and server, pushaway in player code

	COLLISION_GROUP_NPC_ACTOR, // Used so NPCs in scripts ignore the player.
	COLLISION_GROUP_NPC_SCRIPTED, // USed for NPCs in scripts that should not collide with each other

	LAST_SHARED_COLLISION_GROUP
};
