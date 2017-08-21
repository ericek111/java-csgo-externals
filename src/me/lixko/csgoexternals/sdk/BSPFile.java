package me.lixko.csgoexternals.sdk;

public class BSPFile {
	//========= Copyright Valve Corporation, All rights reserved. ============//
	//
	// Purpose: Defines and structures for the BSP file format.
	//
	// $NoKeywords: $
	//=============================================================================//

	// little-endian "VBSP"
	public static int IDBSPHEADER = (('P' << 24) + ('S' << 16) + ('B' << 8) + 'V');

	// MINBSPVERSION is the minimum acceptable version.  The engine will load MINBSPVERSION through BSPVERSION
	public static int MINBSPVERSION = 19;
	public static int BSPVERSION = 20;

	// This needs to match the value in gl_lightmap.h
	// Need to dynamically allocate the weights and light values in radial_t to make this variable.
	public static int MAX_BRUSH_LIGHTMAP_DIM_WITHOUT_BORDER = 32;
	// This is one more than what vbsp cuts for to allow for rounding errors
	public static int MAX_BRUSH_LIGHTMAP_DIM_INCLUDING_BORDER = 35;

	// We can have larger lightmaps on displacements
	public static int MAX_DISP_LIGHTMAP_DIM_WITHOUT_BORDER = 125;
	public static int MAX_DISP_LIGHTMAP_DIM_INCLUDING_BORDER = 128;

	// This is the actual max.. (change if you change the brush lightmap dim or disp lightmap dim
	public static int MAX_LIGHTMAP_DIM_WITHOUT_BORDER = MAX_DISP_LIGHTMAP_DIM_WITHOUT_BORDER;
	public static int MAX_LIGHTMAP_DIM_INCLUDING_BORDER = MAX_DISP_LIGHTMAP_DIM_INCLUDING_BORDER;

	public static int MAX_LIGHTSTYLES = 64;

	// upper design bounds
	public static int MIN_MAP_DISP_POWER = 2; // Minimum and maximum power a displacement can be.
	public static int MAX_MAP_DISP_POWER = 4;

	// Max # of neighboring displacement touching a displacement's corner.
	public static int MAX_DISP_CORNER_NEIGHBORS = 4;

	public static int NUM_DISP_POWER_VERTS(int power) {
		return ((1 << (power)) + 1) * ((1 << (power)) + 1);
	}

	public static int NUM_DISP_POWER_TRIS(int power) {
		return (1 << (power)) * (1 << (power)) * 2;
	}

	// Common limits
	// leaffaces, leafbrushes, planes, and verts are still bounded by
	// 16 bit short limits
	public static int MAX_MAP_MODELS = 1024;
	public static int MAX_MAP_BRUSHES = 8192;
	public static int MAX_MAP_ENTITIES = 8192;
	public static int MAX_MAP_TEXINFO = 12288;
	public static int MAX_MAP_TEXDATA = 2048;
	public static int MAX_MAP_DISPINFO = 2048;
	public static int MAX_MAP_DISP_VERTS = (MAX_MAP_DISPINFO * ((1 << MAX_MAP_DISP_POWER) + 1) * ((1 << MAX_MAP_DISP_POWER) + 1));
	public static int MAX_MAP_DISP_TRIS = ((1 << MAX_MAP_DISP_POWER) * (1 << MAX_MAP_DISP_POWER) * 2);
	public static int MAX_DISPVERTS = NUM_DISP_POWER_VERTS(MAX_MAP_DISP_POWER);
	public static int MAX_DISPTRIS = NUM_DISP_POWER_TRIS(MAX_MAP_DISP_POWER);
	public static int MAX_MAP_AREAS = 256;
	public static int MAX_MAP_AREA_BYTES = (MAX_MAP_AREAS / 8);
	public static int MAX_MAP_AREAPORTALS = 1024;
	// Planes come in pairs, thus an even number.
	public static int MAX_MAP_PLANES = 65536;
	public static int MAX_MAP_NODES = 65536;
	public static int MAX_MAP_BRUSHSIDES = 65536;
	public static int MAX_MAP_LEAFS = 65536;
	public static int MAX_MAP_VERTS = 65536;
	public static int MAX_MAP_VERTNORMALS = 256000;
	public static int MAX_MAP_VERTNORMALINDICES = 256000;
	public static int MAX_MAP_FACES = 65536;
	public static int MAX_MAP_LEAFFACES = 65536;
	public static int MAX_MAP_LEAFBRUSHES = 65536;
	public static int MAX_MAP_PORTALS = 65536;
	public static int MAX_MAP_CLUSTERS = 65536;
	public static int MAX_MAP_LEAFWATERDATA = 32768;
	public static int MAX_MAP_PORTALVERTS = 128000;
	public static int MAX_MAP_EDGES = 256000;
	public static int MAX_MAP_SURFEDGES = 512000;
	public static int MAX_MAP_LIGHTING = 0x1000000;
	public static int MAX_MAP_VISIBILITY = 0x1000000; // increased BSPVERSION 7
	public static int MAX_MAP_TEXTURES = 1024;
	public static int MAX_MAP_WORLDLIGHTS = 8192;
	public static int MAX_MAP_CUBEMAPSAMPLES = 1024;
	public static int MAX_MAP_OVERLAYS = 512;
	public static int MAX_MAP_WATEROVERLAYS = 16384;
	public static int MAX_MAP_TEXDATA_STRING_DATA = 256000;
	public static int MAX_MAP_TEXDATA_STRING_TABLE = 65536;
	// this is stuff for trilist/tristrips, etc.
	public static int MAX_MAP_PRIMITIVES = 32768;
	public static int MAX_MAP_PRIMVERTS = 65536;
	public static int MAX_MAP_PRIMINDICES = 65536;

	// key / value pair sizes
	public static int MAX_KEY = 32;
	public static int MAX_VALUE = 1024;

	// ------------------------------------------------------------------------------------------------ //
	// Displacement neighbor rules
	// ------------------------------------------------------------------------------------------------ //
	//
	// Each displacement is considered to be in its own space:
	//
	//	               NEIGHBOREDGE_TOP
	//
	//	                   1 --- 2
	//	                   |     |
	// NEIGHBOREDGE_LEFT |     | NEIGHBOREDGE_RIGHT
	//	                   |     |
	//	                   0 --- 3
	//
	//	   			NEIGHBOREDGE_BOTTOM
	//
	//
	// Edge edge of a displacement can have up to two neighbors. If it only has one neighbor
	// and the neighbor fills the edge, then SubNeighbor 0 uses CORNER_TO_CORNER (and SubNeighbor 1
	// is undefined).
	//
	// CORNER_TO_MIDPOINT means that it spans [bottom edge,midpoint] or [left edge,midpoint] depending
	// on which edge you're on.
	//
	// MIDPOINT_TO_CORNER means that it spans [midpoint,top edge] or [midpoint,right edge] depending
	// on which edge you're on.
	//
	// Here's an illustration (where C2M=CORNER_TO_MIDPOINT and M2C=MIDPOINT_TO_CORNER
	//
	//
	//					 C2M			  M2C
	//
	//	       1 --------------> x --------------> 2
	//
	//	       ^                                   ^
	//	       |                                   |
	//	       |                                   |
	//  M2C  |                                   |	M2C
	//	       |                                   |
	//	       |                                   |
	//
	//	       x                 x                 x 
	//
	//	       ^                                   ^
	//	       |                                   |
	//	       |                                   |
	//  C2M  |                                   |	C2M
	//	       |                                   |
	//	       |                                   |
	// 
	//	       0 --------------> x --------------> 3
	//
	//	               C2M			  M2C
	//
	//
	// The CHILDNODE_ defines can be used to refer to a node's child nodes (this is for when you're
	// recursing into the node tree inside a displacement):
	//
	// ---------
	// |   |   |
	// | 1 | 0 |
	// |   |   |
	// |---x---|
	// |   |   |
	// | 2 | 3 |
	// |   |   |
	// ---------
	// 
	// ------------------------------------------------------------------------------------------------ //

	// These can be used to index g_ChildNodeIndexMul.
	enum g_ChildNodeIndexMul {
		CHILDNODE_UPPER_RIGHT,
		CHILDNODE_UPPER_LEFT,
		CHILDNODE_LOWER_LEFT,
		CHILDNODE_LOWER_RIGHT
	};

	// Corner indices. Used to index m_CornerNeighbors.
	enum m_CornerNeighbors {
		CORNER_LOWER_LEFT,
		CORNER_UPPER_LEFT,
		CORNER_UPPER_RIGHT,
		CORNER_LOWER_RIGHT
	};

	// These edge indices must match the edge indices of the CCoreDispSurface.
	enum CCoreDispSurface {
		NEIGHBOREDGE_LEFT,
		NEIGHBOREDGE_TOP,
		NEIGHBOREDGE_RIGHT,
		NEIGHBOREDGE_BOTTOM
	};

	// These denote where one dispinfo fits on another.
	// Note: tables are generated based on these indices so make sure to update
	//	       them if these indices are changed.
	enum NeighborSpan {
		CORNER_TO_CORNER,
		CORNER_TO_MIDPOINT,
		MIDPOINT_TO_CORNER
	};

	// These define relative orientations of displacement neighbors.
	enum NeighborOrientation {
		ORIENTATION_CCW_0,
		ORIENTATION_CCW_90,
		ORIENTATION_CCW_180,
		ORIENTATION_CCW_270
	};

	//=============================================================================

	enum Lumps {
		LUMP_ENTITIES, 
		LUMP_PLANES, 
		LUMP_TEXDATA, 
		LUMP_VERTEXES, 
		LUMP_VISIBILITY, 
		LUMP_NODES, 
		LUMP_TEXINFO, 
		LUMP_FACES, 
		LUMP_LIGHTING, 
		LUMP_OCCLUSION,
		LUMP_LEAFS, 
		LUMP_FACEIDS,
		LUMP_EDGES, 
		LUMP_SURFEDGES, 
		LUMP_MODELS, 
		LUMP_WORLDLIGHTS, 
		LUMP_LEAFFACES, 
		LUMP_LEAFBRUSHES, 
		LUMP_BRUSHES, 
		LUMP_BRUSHSIDES, 
		LUMP_AREAS, 
		LUMP_AREAPORTALS, 
		LUMP_UNUSED0,
		LUMP_UNUSED1,
		LUMP_UNUSED2,
		LUMP_UNUSED3,
		LUMP_DISPINFO,
		LUMP_ORIGINALFACES,
		LUMP_PHYSDISP,
		LUMP_PHYSCOLLIDE,
		LUMP_VERTNORMALS,
		LUMP_VERTNORMALINDICES,
		LUMP_DISP_LIGHTMAP_ALPHAS,
		LUMP_DISP_VERTS, // CDispVerts
		LUMP_DISP_LIGHTMAP_SAMPLE_POSITIONS, // For each displacement
		//     For each lightmap sample
		//         byte for index
		//         if 255, then index = next byte + 255
		//         3 bytes for barycentric coordinates
		// The game lump is a method of adding game-specific lumps
		// FIX-ME: Eventually, all lumps could use the game lump system
		LUMP_GAME_LUMP,
		LUMP_LEAFWATERDATA,
		LUMP_PRIMITIVES,
		LUMP_PRIMVERTS,
		LUMP_PRIMINDICES,
		// A pak file can be embedded in a .bsp now, and the file system will search the pak
		//  file first for any referenced names, before deferring to the game directory 
		//  file system/pak files and finally the base directory file system/pak files.
		LUMP_PAKFILE,
		LUMP_CLIPPORTALVERTS,
		// A map can have a number of cubemap entities in it which cause cubemap renders
		// to be taken after running vrad.
		LUMP_CUBEMAPS,
		LUMP_TEXDATA_STRING_DATA,
		LUMP_TEXDATA_STRING_TABLE,
		LUMP_OVERLAYS,
		LUMP_LEAFMINDISTTOWATER,
		LUMP_FACE_MACRO_TEXTURE_INFO,
		LUMP_DISP_TRIS,
		LUMP_PHYSCOLLIDESURFACE, // deprecated.  We no longer use win32-specific havok compression on terrain
		LUMP_WATEROVERLAYS,
		LUMP_LEAF_AMBIENT_INDEX_HDR, // index of LUMP_LEAF_AMBIENT_LIGHTING_HDR
		LUMP_LEAF_AMBIENT_INDEX, // index of LUMP_LEAF_AMBIENT_LIGHTING

		// optional lumps for HDR
		LUMP_LIGHTING_HDR,
		LUMP_WORLDLIGHTS_HDR,
		LUMP_LEAF_AMBIENT_LIGHTING_HDR, // NOTE: this data overrides part of the data stored in LUMP_LEAFS.
		LUMP_LEAF_AMBIENT_LIGHTING, // NOTE: this data overrides part of the data stored in LUMP_LEAFS.

		LUMP_XZIPPAKFILE, // deprecated. xbox 1: xzip version of pak file
		LUMP_FACES_HDR, // HDR maps may have different face data.
		LUMP_MAP_FLAGS, // extended level-wide flags. not present in all levels
		LUMP_OVERLAY_FADES // Fade distances for overlays
	};

	public static int HEADER_LUMPS = 64;

	// level feature flags
	public static int LVLFLAGS_BAKED_STATIC_PROP_LIGHTING_NONHDR = 0x00000001; // was processed by vrad with -staticproplighting, no hdr data
	public static int LVLFLAGS_BAKED_STATIC_PROP_LIGHTING_HDR = 0x00000002;// was processed by vrad with -staticproplighting, in hdr

	// 360 only: game lump is compressed, filelen reflects original size
	// use next entry fileofs to determine actual disk lump compressed size
	// compression stage ensures a terminal null dictionary entry
	public static int GAMELUMPFLAG_COMPRESSED = 0x0001;

	public static int TEXTURE_NAME_LENGTH = 128; // changed from 64 BSPVERSION 8

	public static int DISPTRI_TAG_SURFACE = (1 << 0);
	public static int DISPTRI_TAG_WALKABLE = (1 << 1);
	public static int DISPTRI_TAG_BUILDABLE = (1 << 2);
	public static int DISPTRI_FLAG_SURFPROP1 = (1 << 3);
	public static int DISPTRI_FLAG_SURFPROP2 = (1 << 4);
	public static int DISPTRI_TAG_REMOVE = (1 << 5);

	public static int MAXLIGHTMAPS = 4;

	enum dprimitive_type {
		PRIM_TRILIST,
		PRIM_TRISTRIP,
	};

	// NOTE: Only 7-bits stored!!!
	public static int LEAF_FLAGS_SKY = 0x01; // This leaf has 3D sky in its PVS
	public static int LEAF_FLAGS_RADIAL = 0x02; // This leaf culled away some portals due to radial vis
	public static int LEAF_FLAGS_SKY2D = 0x04; // This leaf has 2D sky in its PVS

	public static int ANGLE_UP = -1;
	public static int ANGLE_DOWN = -2;

	// the visibility lump consists of a header with a count, then
	// byte offsets for the PVS and PHS of each cluster, then the raw
	// compressed bit vectors
	public static int DVIS_PVS = 0;
	public static int DVIS_PAS = 1;

	// lights that were used to illuminate the world
	enum emittype_t {
		emit_surface, // 90 degree spotlight
		emit_point, // simple point light source
		emit_spotlight, // spotlight with penumbra
		emit_skylight, // directional light with no falloff (surface must trace to SKY texture)
		emit_quakelight, // linear falloff, non-lambertian
		emit_skyambient, // spherical light source with no falloff (surface must trace to SKY texture)
	};

	// Flags for dworldlight_t::flags
	public static int DWL_FLAGS_INAMBIENTCUBE = 0x0001; // This says that the light was put into the per-leaf ambient cubes.

	public static int OVERLAY_BSP_FACE_COUNT = 64;

	public static int OVERLAY_RENDER_ORDER_NUM_BITS = 2;
	public static int OVERLAY_NUM_RENDER_ORDERS = (1 << OVERLAY_RENDER_ORDER_NUM_BITS);
	public static int OVERLAY_RENDER_ORDER_MASK = 0xC000; // top 2 bits set

	public static int WATEROVERLAY_BSP_FACE_COUNT = 256;
	public static int WATEROVERLAY_RENDER_ORDER_NUM_BITS = 2;
	public static int WATEROVERLAY_NUM_RENDER_ORDERS = (1 << WATEROVERLAY_RENDER_ORDER_NUM_BITS);
	public static int WATEROVERLAY_RENDER_ORDER_MASK = 0xC000; // top 2 bits set
}
