package me.lixko.csgoexternals.sdk;

import me.lixko.csgoexternals.util.BufferStruct;
import me.lixko.csgoexternals.util.BufferStruct.SkipField;

public class BSPFile {
	//========= Copyright Valve Corporation, All rights reserved. ============//
	//
	// Purpose: Defines and structures for the BSP file format.
	//
	// $NoKeywords: $
	//=============================================================================//

	// little-endian "VBSP"
	public static final int IDBSPHEADER = (('P' << 24) + ('S' << 16) + ('B' << 8) + 'V');

	// MINBSPVERSION is the minimum acceptable version.  The engine will load MINBSPVERSION through BSPVERSION
	public static final int MINBSPVERSION = 19;
	public static final int BSPVERSION = 20;

	// This needs to match the value in gl_lightmap.h
	// Need to dynamically allocate the weights and light values in radial_t to make this variable.
	public static final int MAX_BRUSH_LIGHTMAP_DIM_WITHOUT_BORDER = 32;
	// This is one more than what vbsp cuts for to allow for rounding errors
	public static final int MAX_BRUSH_LIGHTMAP_DIM_INCLUDING_BORDER = 35;

	// We can have larger lightmaps on displacements
	public static final int MAX_DISP_LIGHTMAP_DIM_WITHOUT_BORDER = 125;
	public static final int MAX_DISP_LIGHTMAP_DIM_INCLUDING_BORDER = 128;

	// This is the actual max.. (change if you change the brush lightmap dim or disp lightmap dim
	public static final int MAX_LIGHTMAP_DIM_WITHOUT_BORDER = MAX_DISP_LIGHTMAP_DIM_WITHOUT_BORDER;
	public static final int MAX_LIGHTMAP_DIM_INCLUDING_BORDER = MAX_DISP_LIGHTMAP_DIM_INCLUDING_BORDER;

	public static final int MAX_LIGHTSTYLES = 64;

	// upper design bounds
	public static final int MIN_MAP_DISP_POWER = 2; // Minimum and maximum power a displacement can be.
	public static final int MAX_MAP_DISP_POWER = 4;

	// Max # of neighboring displacement touching a displacement's corner.
	public static final int MAX_DISP_CORNER_NEIGHBORS = 4;

	public static final int NUM_DISP_POWER_VERTS(int power) {
		return ((1 << (power)) + 1) * ((1 << (power)) + 1);
	}

	public static final int NUM_DISP_POWER_TRIS(int power) {
		return (1 << (power)) * (1 << (power)) * 2;
	}

	// Common limits
	// leaffaces, leafbrushes, planes, and verts are still bounded by
	// 16 bit short limits
	public static final int MAX_MAP_MODELS = 1024;
	public static final int MAX_MAP_BRUSHES = 8192;
	public static final int MAX_MAP_ENTITIES = 8192;
	public static final int MAX_MAP_TEXINFO = 12288;
	public static final int MAX_MAP_TEXDATA = 2048;
	public static final int MAX_MAP_DISPINFO = 2048;
	public static final int MAX_MAP_DISP_VERTS = (MAX_MAP_DISPINFO * ((1 << MAX_MAP_DISP_POWER) + 1) * ((1 << MAX_MAP_DISP_POWER) + 1));
	public static final int MAX_MAP_DISP_TRIS = ((1 << MAX_MAP_DISP_POWER) * (1 << MAX_MAP_DISP_POWER) * 2);
	public static final int MAX_DISPVERTS = NUM_DISP_POWER_VERTS(MAX_MAP_DISP_POWER);
	public static final int MAX_DISPTRIS = NUM_DISP_POWER_TRIS(MAX_MAP_DISP_POWER);
	public static final int MAX_MAP_AREAS = 256;
	public static final int MAX_MAP_AREA_BYTES = (MAX_MAP_AREAS / 8);
	public static final int MAX_MAP_AREAPORTALS = 1024;
	// Planes come in pairs, thus an even number.
	public static final int MAX_MAP_PLANES = 65536;
	public static final int MAX_MAP_NODES = 65536;
	public static final int MAX_MAP_BRUSHSIDES = 65536;
	public static final int MAX_MAP_LEAFS = 65536;
	public static final int MAX_MAP_VERTS = 65536;
	public static final int MAX_MAP_VERTNORMALS = 256000;
	public static final int MAX_MAP_VERTNORMALINDICES = 256000;
	public static final int MAX_MAP_FACES = 65536;
	public static final int MAX_MAP_LEAFFACES = 65536;
	public static final int MAX_MAP_LEAFBRUSHES = 65536;
	public static final int MAX_MAP_PORTALS = 65536;
	public static final int MAX_MAP_CLUSTERS = 65536;
	public static final int MAX_MAP_LEAFWATERDATA = 32768;
	public static final int MAX_MAP_PORTALVERTS = 128000;
	public static final int MAX_MAP_EDGES = 256000;
	public static final int MAX_MAP_SURFEDGES = 512000;
	public static final int MAX_MAP_LIGHTING = 0x1000000;
	public static final int MAX_MAP_VISIBILITY = 0x1000000; // increased BSPVERSION 7
	public static final int MAX_MAP_TEXTURES = 1024;
	public static final int MAX_MAP_WORLDLIGHTS = 8192;
	public static final int MAX_MAP_CUBEMAPSAMPLES = 1024;
	public static final int MAX_MAP_OVERLAYS = 512;
	public static final int MAX_MAP_WATEROVERLAYS = 16384;
	public static final int MAX_MAP_TEXDATA_STRING_DATA = 256000;
	public static final int MAX_MAP_TEXDATA_STRING_TABLE = 65536;
	// this is stuff for trilist/tristrips, etc.
	public static final int MAX_MAP_PRIMITIVES = 32768;
	public static final int MAX_MAP_PRIMVERTS = 65536;
	public static final int MAX_MAP_PRIMINDICES = 65536;

	// key / value pair sizes
	public static final int MAX_KEY = 32;
	public static final int MAX_VALUE = 1024;

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
	public enum g_ChildNodeIndexMul {
		CHILDNODE_UPPER_RIGHT,
		CHILDNODE_UPPER_LEFT,
		CHILDNODE_LOWER_LEFT,
		CHILDNODE_LOWER_RIGHT;
		@SkipField
		public static final g_ChildNodeIndexMul values[] = values();
	};

	// Corner indices. Used to index m_CornerNeighbors.
	public enum m_CornerNeighbors {
		CORNER_LOWER_LEFT,
		CORNER_UPPER_LEFT,
		CORNER_UPPER_RIGHT,
		CORNER_LOWER_RIGHT;
		@SkipField
		public static final m_CornerNeighbors values[] = values();
	};

	// These edge indices must match the edge indices of the CCoreDispSurface.
	public enum CCoreDispSurface {
		NEIGHBOREDGE_LEFT,
		NEIGHBOREDGE_TOP,
		NEIGHBOREDGE_RIGHT,
		NEIGHBOREDGE_BOTTOM;
		@SkipField
		public static final CCoreDispSurface values[] = values();
	};

	// These denote where one dispinfo fits on another.
	// Note: tables are generated based on these indices so make sure to update
	//	       them if these indices are changed.
	public enum NeighborSpan {
		CORNER_TO_CORNER,
		CORNER_TO_MIDPOINT,
		MIDPOINT_TO_CORNER;
		@SkipField
		public static final NeighborSpan values[] = values();
	};

	// These define relative orientations of displacement neighbors.
	public enum NeighborOrientation {
		ORIENTATION_CCW_0,
		ORIENTATION_CCW_90,
		ORIENTATION_CCW_180,
		ORIENTATION_CCW_270;
		@SkipField
		public static final NeighborOrientation values[] = values();
	};

	//=============================================================================

	public enum Lumps {
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
		LUMP_OVERLAY_FADES; // Fade distances for overlays

		@SkipField
		public static final Lumps values[] = values();
	};

	public static final int HEADER_LUMPS = 64;

	// level feature flags
	public static final int LVLFLAGS_BAKED_STATIC_PROP_LIGHTING_NONHDR = 0x00000001; // was processed by vrad with -staticproplighting, no hdr data
	public static final int LVLFLAGS_BAKED_STATIC_PROP_LIGHTING_HDR = 0x00000002;// was processed by vrad with -staticproplighting, in hdr

	// 360 only: game lump is compressed, filelen reflects original size
	// use next entry fileofs to determine actual disk lump compressed size
	// compression stage ensures a terminal null dictionary entry
	public static final int GAMELUMPFLAG_COMPRESSED = 0x0001;

	public static final int TEXTURE_NAME_LENGTH = 128; // changed from 64 BSPVERSION 8

	// NOTE: see the section above titled "displacement neighbor rules".
	public static class CDispSubNeighbor extends BufferStruct {
		public short m_iNeighbor; //unsigned		// This indexes into ddispinfos.
		// 0xFFFF if there is no neighbor here.

		public byte m_NeighborOrientation; // unsigned		// (CCW) rotation of the neighbor wrt this displacement.

		// These use the NeighborSpan type.
		public byte m_Span; //unsigned						// Where the neighbor fits onto this side of our displacement.
		public byte m_NeighborSpan; // unsigned 		// Where we fit onto our neighbor.

		public short GetNeighborIndex() {
			return m_iNeighbor;
		} // unsigned

		public NeighborSpan GetSpan() {
			return NeighborSpan.values[m_Span];
		}

		public NeighborSpan GetNeighborSpan() {
			return NeighborSpan.values[m_NeighborSpan];
		}

		public NeighborOrientation GetNeighborOrientation() {
			return NeighborOrientation.values[m_NeighborOrientation];
		}

		public boolean IsValid() {
			return m_iNeighbor != 0xFFFF;
		}

		public void SetInvalid() {
			m_iNeighbor = (short) 0xFFFF;
		}
	};

	// NOTE: see the section above titled "displacement neighbor rules".
	public static class CDispNeighbor extends BufferStruct {
		// Note: if there is a neighbor that fills the whole side (CORNER_TO_CORNER),
		//       then it will always be in CDispNeighbor::m_Neighbors[0]
		public CDispSubNeighbor[] m_SubNeighbors = new CDispSubNeighbor[2];

		public void SetInvalid() {
			m_SubNeighbors[0].SetInvalid();
			m_SubNeighbors[1].SetInvalid();
		}

		// Returns false if there isn't anything touching this edge.
		public boolean IsValid() {
			return m_SubNeighbors[0].IsValid() || m_SubNeighbors[1].IsValid();
		}
	};

	public static class CDispCornerNeighbors extends BufferStruct {
		public short[] m_Neighbors = new short[MAX_DISP_CORNER_NEIGHBORS]; // unsigned	// indices of neighbors.
		public byte m_nNeighbors; // unsigned

		public void SetInvalid() {
			m_nNeighbors = 0;
		}
	};

	public static class CDispVert extends BufferStruct {
		public float[] m_vVector = new float[3]; // Vector field defining displacement volume.
		public float m_flDist; // Displacement distances.
		public float m_flAlpha; // "per vertex" alpha values.
	};

	public static final int DISPTRI_TAG_SURFACE = (1 << 0);
	public static final int DISPTRI_TAG_WALKABLE = (1 << 1);
	public static final int DISPTRI_TAG_BUILDABLE = (1 << 2);
	public static final int DISPTRI_FLAG_SURFPROP1 = (1 << 3);
	public static final int DISPTRI_FLAG_SURFPROP2 = (1 << 4);
	public static final int DISPTRI_TAG_REMOVE = (1 << 5);

	public static final int MAXLIGHTMAPS = 4;

	public enum dprimitive_type {
		PRIM_TRILIST,
		PRIM_TRISTRIP;
		@SkipField
		public static final dprimitive_type values[] = values();
	};

	// NOTE: Only 7-bits stored!!!
	public static final int LEAF_FLAGS_SKY = 0x01; // This leaf has 3D sky in its PVS
	public static final int LEAF_FLAGS_RADIAL = 0x02; // This leaf culled away some portals due to radial vis
	public static final int LEAF_FLAGS_SKY2D = 0x04; // This leaf has 2D sky in its PVS

	public static final int ANGLE_UP = -1;
	public static final int ANGLE_DOWN = -2;

	// the visibility lump consists of a header with a count, then
	// byte offsets for the PVS and PHS of each cluster, then the raw
	// compressed bit vectors
	public static final int DVIS_PVS = 0;
	public static final int DVIS_PAS = 1;

	// lights that were used to illuminate the world
	public enum emittype_t {
		emit_surface, // 90 degree spotlight
		emit_point, // simple point light source
		emit_spotlight, // spotlight with penumbra
		emit_skylight, // directional light with no falloff (surface must trace to SKY texture)
		emit_quakelight, // linear falloff, non-lambertian
		emit_skyambient; // spherical light source with no falloff (surface must trace to SKY texture)
		@SkipField
		public static final emittype_t values[] = values();
	};

	// Flags for dworldlight_t::flags
	public static final int DWL_FLAGS_INAMBIENTCUBE = 0x0001; // This says that the light was put into the per-leaf ambient cubes.

	public static final int OVERLAY_BSP_FACE_COUNT = 64;

	public static final int OVERLAY_RENDER_ORDER_NUM_BITS = 2;
	public static final int OVERLAY_NUM_RENDER_ORDERS = (1 << OVERLAY_RENDER_ORDER_NUM_BITS);
	public static final int OVERLAY_RENDER_ORDER_MASK = 0xC000; // top 2 bits set

	public static final int WATEROVERLAY_BSP_FACE_COUNT = 256;
	public static final int WATEROVERLAY_RENDER_ORDER_NUM_BITS = 2;
	public static final int WATEROVERLAY_NUM_RENDER_ORDERS = (1 << WATEROVERLAY_RENDER_ORDER_NUM_BITS);
	public static final int WATEROVERLAY_RENDER_ORDER_MASK = 0xC000; // top 2 bits set

	public static class lump_t extends BufferStruct {
		public int fileofs; // offset into file (bytes)
		public int filelen; // length of lump (bytes)
		public int version; // lump format version
		public byte[] fourCC = new byte[4]; // lump ident code
	}

	public static class dheader_t extends BufferStruct {
		public int ident; // BSP file identifier
		public int version; // BSP file version
		public lump_t[] lumps = new lump_t[HEADER_LUMPS]; // lump directory array
		public int mapRevision; // the map's revision (iteration, version) number (added BSPVERSION 6)
	}

	public static class dflagslump_t extends BufferStruct {
		public int m_LevelFlags; // LVLFLAGS_xxx
	};

	public static class lumpfileheader_t extends BufferStruct {
		public int lumpOffset;
		public int lumpID;
		public int lumpVersion;
		public int lumpLength;
		public int mapRevision; // the map's revision (iteration, version) number (added BSPVERSION 6)
	};

	public static class dgamelumpheader_t extends BufferStruct {
		public int lumpCount;
		// dgamelump_t follow this
	};

	public static class dgamelump_t extends BufferStruct {
		public int id; // GameLumpId_t
		public short flags; // unsigned
		public short version; // unsigned
		public int fileofs;
		public int filelen;
	};

	public static class dmodel_t extends BufferStruct {
		public float[] mins = new float[3], maxs = new float[3];
		public float[] origin = new float[3]; // for sounds or lights
		public int headnode;
		public int firstface, numfaces; // submodels just draw faces without walking the bsp tree
	};

	public static class dphysmodel_t extends BufferStruct {
		public int modelIndex;
		public int dataSize;
		public int keydataSize;
		public int solidCount;
	};

	// contains the binary blob for each displacement surface's virtual hull
	public static class dphysdisp_t extends BufferStruct {
		public short numDisplacements; // unsigned
		//unsigned short dataSize[numDisplacements];
	};

	public static class dvertex_t extends BufferStruct {
		public float[] point = new float[3];
	};

	// planes (x&~1) and (x&~1)+1 are always opposites
	public static class dplane_t extends BufferStruct {
		public float[] normal = new float[3];
		public float dist;
		public int type; // PLANE_X - PLANE_ANYZ ?remove? trivial to regenerate
	};

	public static class dnode_t extends BufferStruct {
		public int planenum;	
		public int[] children = new int[2]; // negative numbers are -(leafs+1), not nodes
		public short[] mins = new short[3]; // for frustom culling
		public short[] maxs = new short[3];
		@UnsignedField(2)
		public int firstface; // unsigned short
		@UnsignedField(2)
		public int numfaces; // unsigned // counting both sides
		public short area; // If all leaves below this node are in the same area, then
		// this is the area index. If not, this is -1.
		public short padding; // pad to 32 bytes length
	};

	public static class texinfo_t extends BufferStruct {
		public float[][] textureVecs = new float[2][4]; // textureVecsTexelsPerWorldUnits - [s/t][xyz offset]
		public float[][] lightmapVecs = new float[2][4]; // lightmapVecsLuxelsPerWorldUnits - [s/t][xyz offset] - length is in units of texels/area
		public int flags; // miptex flags + overrides
		public int texdata; // Pointer to texture name, size, etc.
	};

	public static class dtexdata_t extends BufferStruct {
		public float[] reflectivity = new float[3];
		public int nameStringTableID; // index into g_StringTable for the texture name
		public int width, height; // source image
		public int view_width, view_height; //
	};

	public static class doccluderdata_t extends BufferStruct {
		public int flags;
		public int firstpoly; // index into doccluderpolys
		public int polycount;
		public float[] mins = new float[3];
		public float[] maxs = new float[3];
		public int area;
	};

	public static class doccluderdataV1_t extends BufferStruct {
		public int flags;
		public int firstpoly; // index into doccluderpolys
		public int polycount;
		public float[] mins = new float[3];
		public float[] maxs = new float[3];
	};

	public static class doccluderpolydata_t extends BufferStruct {
		public int firstvertexindex; // index into doccludervertindices
		public int vertexcount;
		public int planenum;
	};

	//note that edge 0 is never used, because negative edge nums are used for
	//counterclockwise use of the edge in a face
	public static class dedge_t extends BufferStruct {
		@UnsignedField(2)
		public int v[] = new int[2]; // vertex numbers
	};

	// Surface edges are used to reference the edge array, in a somewhat complex way.
	// The value in the surfedge array can be positive or negative. The absolute
	// value of this number is an index into the edge array: if positive, it means
	// the edge is defined from the first to the second vertex; if negative,
	// from the second to the first vertex.
	public static class dsurfedge_t extends BufferStruct {
		public int value;
	};

	public static class dprimitive_t extends BufferStruct {
		public byte type; // unsigned
		public short firstIndex; // unsigned
		public short indexCount; // unsigned
		public short firstVert; // unsigned
		public short vertCount; // unsigned
	};

	public static class dprimvert_t extends BufferStruct {
		public float[] pos = new float[3];
	};

	public static class dface_t extends BufferStruct {
		@UnsignedField(2)
		public int planenum; // unsigned short
		public byte side; // faces opposite to the node's plane direction
		public byte onNode; // 1 of on node, 0 if in leaf

		public int firstedge; // we must support > 64k edges
		public short numedges;
		public short texinfo;
		// This is a union under the assumption that a fog volume boundary (ie. water surface)
		// isn't a displacement map.
		// FIX-ME: These should be made a union with a flags or type field for which one it is
		// if we can add more to this.
		//	union
		//	{
		public short dispinfo;
		// This is only for surfaces that are the boundaries of fog volumes
		// (ie. water surfaces)
		// All of the rest of the surfaces can look at their leaf to find out
		// what fog volume they are in.
		@UnsignedField(2)
		public int surfaceFogVolumeID;
		//	};

		// lighting info
		public byte[] styles = new byte[MAXLIGHTMAPS];
		// Each face gives a byte offset into the lighting lump in its lightofs member (if no lighting
		// information is used for this face e.g. faces with skybox, nodraw and invisible textures,
		// lightofs is -1.) There are (number of lightstyles)*(number of luxels) lightmap samples
		// for each face, where each sample is a 4-byte ColorRGBExp32 structure.
		public int lightofs; // start of [numstyles*surfsize] samples 
		public float area;

		// TO-DO: make these unsigned bytes?
		public int[] m_LightmapTextureMinsInLuxels = new int[2];
		public int[] m_LightmapTextureSizeInLuxels = new int[2];

		public int origFace; // reference the original face this face was derived from, TODO: SWAP ???
		@UnsignedField(2)
		public int m_NumPrims;
		@UnsignedField(2)
		public int firstPrimID;
		public int smoothingGroups;

		public short GetNumPrims() {
			return (short) (m_NumPrims & 0x7FFF);
		}

		public void SetNumPrims(short nPrims) {
			m_NumPrims &= ~0x7FFF;
			m_NumPrims |= (nPrims & 0x7FFF);
		}

		public boolean AreDynamicShadowsEnabled() {
			return (m_NumPrims & 0x8000) == 0;
		}

		public void SetDynamicShadowsEnabled(boolean bEnabled) {
			if (bEnabled)
				m_NumPrims &= ~0x8000;
			else
				m_NumPrims |= 0x8000;
		}
	};

	public static class dfaceid_t extends BufferStruct {
		public short hammerfaceid;
	};

	public static class dleaf_version_0_t extends BufferStruct {
		public int contents; // OR of all brushes (not needed?)

		public short cluster;

		public short areaFlags;
		@SkipField
		public short area;
		@SkipField
		public short flags; // Per leaf flags.

		public short[] mins = new short[3]; // for frustum culling
		public short[] maxs = new short[3];

		public short firstleafface;
		public short numleaffaces;

		public short firstleafbrush;
		public short numleafbrushes;
		public short leafWaterDataID; // -1 for not in water

		// Precaculated light info for entities.
		public CompressedLightCube m_AmbientLighting;

		@Override
		public void afterRead() {
			this.area = (short) (areaFlags & 0x1FF);
			this.flags = (short) ((areaFlags >> 9) & 0x1FF);
		}
	};

	// version 1
	public static class dleaf_t extends BufferStruct {
		public int contents; // OR of all brushes (not needed?)

		public short cluster;
		
		public short areaFlags;
		@SkipField
		public short area;
		@SkipField
		public short flags; // Per leaf flags.

		public short[] mins = new short[3]; // for frustum culling
		public short[] maxs = new short[3];

		@UnsignedField(2)
		public int firstleafface;
		@UnsignedField(2)
		public int numleaffaces;

		@UnsignedField(2)
		public int firstleafbrush;
		@UnsignedField(2)
		public int numleafbrushes;
		public short leafWaterDataID; // -1 for not in water
		
		public short padding;

		@Override
		public void afterRead() {
			this.area = (short) (areaFlags & 0x1FF);
			this.flags = (short) ((areaFlags >> 9) & 0x1FF);
		}

		//!!! NOTE: for maps of version 19 or lower uncomment this block
		/*
		 * CompressedLightCube ambientLighting; // Precaculated light info for entities.
		 * short padding; // padding to 4-byte boundary
		 */
	};

	// used to map faces referenced in the leaf structure to indices in the face array
	public static class dleafface_t extends BufferStruct {
		@UnsignedField(2)
		public int value;
	}

	// used to map brushes referenced in the leaf structure to indices in the face array
	public static class dleafbrush_t extends BufferStruct {
		@UnsignedField(2)
		public int value;
	}

	// each leaf contains N samples of the ambient lighting
	// each sample contains a cube of ambient light projected on to each axis
	// and a sampling position encoded as a 0.8 fraction (mins=0,maxs=255) of the leaf's bounding box
	public static class dleafambientlighting_t extends BufferStruct {
		public CompressedLightCube cube;
		public byte x; // fixed point fraction of leaf bounds
		public byte y; // fixed point fraction of leaf bounds
		public byte z; // fixed point fraction of leaf bounds
		public byte pad; // unused
	};

	public static class dleafambientindex_t extends BufferStruct {
		public short ambientSampleCount;
		public short firstAmbientSample;
	};

	public static class dbrushside_t extends BufferStruct {
		public short planenum; // facing out of the leaf
		public short texinfo;
		public short dispinfo; // displacement info (BSPVERSION 7)
		public short bevel; // is the side a bevel plane? (BSPVERSION 7)
	};

	public static class dbrush_t extends BufferStruct {
		public int firstside;
		public int numsides;
		public int contents;
	};

	// the visibility lump consists of a header with a count, then
	// byte offsets for the PVS and PHS of each cluster, then the raw
	// compressed bit vectors
	public static class dvis_t extends BufferStruct {
		public int numclusters;
		public int[][] bitofs = new int[8][2]; // bitofs[numclusters][2]
	};

	// each area has a list of portals that lead into other areas
	// when portals are closed, other areas may not be visible or
	// hearable even if the vis info says that it should be
	public static class dareaportal_t extends BufferStruct {
		public short m_PortalKey; // Entities have a key called portalnumber (and in vbsp a variable
		// called areaportalnum) which is used
		// to bind them to the area portals by comparing with this value.

		public short otherarea; // The area this portal looks into.

		public short m_FirstClipPortalVert; // Portal geometry.
		public short m_nClipPortalVerts;

		public int planenum;
	};

	public static class darea_t extends BufferStruct {
		public int numareaportals;
		public int firstareaportal;
	};

	public static class dleafwaterdata_t extends BufferStruct {
		public float surfaceZ;
		public float minZ;
		public short surfaceTexInfoID;
	};

	public static class dworldlight_t extends BufferStruct {
		public float[] origin = new float[3];
		public float[] intensity = new float[3];
		public float[] normal = new float[3]; // for surfaces and spotlights
		public int cluster;
		public emittype_t type;
		public int style;
		public float stopdot; // start of penumbra for emit_spotlight
		public float stopdot2; // end of penumbra for emit_spotlight
		public float exponent; // 
		public float radius; // cutoff distance
		// falloff for emit_spotlight + emit_point: 
		// 1 / (constant_attn + linear_attn * dist + quadratic_attn * dist^2)
		public float constant_attn;
		public float linear_attn;
		public float quadratic_attn;
		public int flags; // Uses a combination of the DWL_FLAGS_ defines.
		public int texinfo; // 
		public int owner; // entity that this light it relative to
	};

	public static class dcubemapsample_t extends BufferStruct {
		public int[] origin = new int[3]; // position of light snapped to the nearest integer
		// the filename for the vtf file is derived from the position
		public byte size; // 0 - default
		// otherwise, 1<<(size-1)
	};

	public static class doverlay_t extends BufferStruct {
		public int nId;
		public short nTexInfo;

		private short m_nFaceCountAndRenderOrder;

		public int[] aFaces = new int[OVERLAY_BSP_FACE_COUNT];
		public float[] flU = new float[2];
		public float[] flV = new float[2];
		public float[][] vecUVPoints = new float[4][3];
		public float[] vecOrigin = new float[3];
		public float[] vecBasisNormal = new float[3];

		public void SetFaceCount(short count) {
			m_nFaceCountAndRenderOrder &= OVERLAY_RENDER_ORDER_MASK;
			m_nFaceCountAndRenderOrder |= (count & ~OVERLAY_RENDER_ORDER_MASK);
		}

		public short GetFaceCount() {
			return (short) (m_nFaceCountAndRenderOrder & ~OVERLAY_RENDER_ORDER_MASK);
		}

		public void SetRenderOrder(short order) {
			m_nFaceCountAndRenderOrder &= ~OVERLAY_RENDER_ORDER_MASK;
			m_nFaceCountAndRenderOrder |= (order << (16 - OVERLAY_RENDER_ORDER_NUM_BITS)); // leave 2 bits for render order.
		}

		public short GetRenderOrder() {
			return (short) (m_nFaceCountAndRenderOrder >> (16 - OVERLAY_RENDER_ORDER_NUM_BITS));
		}
	};

	public static class doverlayfade_t extends BufferStruct {
		public float flFadeDistMinSq;
		public float flFadeDistMaxSq;
	};

	public static class dwateroverlay_t extends BufferStruct {
		public int nId;
		public short nTexInfo;

		private short m_nFaceCountAndRenderOrder;

		public int[] aFaces = new int[WATEROVERLAY_BSP_FACE_COUNT];
		public float[] flU = new float[2];
		public float[] flV = new float[2];
		public float[][] vecUVPoints = new float[4][3];
		public float[] vecOrigin = new float[3];
		public float[] vecBasisNormal = new float[3];

		public void SetFaceCount(short count) {
			m_nFaceCountAndRenderOrder &= WATEROVERLAY_RENDER_ORDER_MASK;
			m_nFaceCountAndRenderOrder |= (count & ~WATEROVERLAY_RENDER_ORDER_MASK);
		}

		public short GetFaceCount() {
			return (short) (m_nFaceCountAndRenderOrder & ~WATEROVERLAY_RENDER_ORDER_MASK);
		}

		public void SetRenderOrder(short order) {
			m_nFaceCountAndRenderOrder &= ~WATEROVERLAY_RENDER_ORDER_MASK;
			m_nFaceCountAndRenderOrder |= (order << (16 - WATEROVERLAY_RENDER_ORDER_NUM_BITS)); // leave 2 bits for render order.
		}

		public short GetRenderOrder() {
			return (short) (m_nFaceCountAndRenderOrder >> (16 - WATEROVERLAY_RENDER_ORDER_NUM_BITS));
		}
	};

	// finalized page of surface's lightmaps
	public static final int MAX_LIGHTMAPPAGE_WIDTH = 256;
	public static final int MAX_LIGHTMAPPAGE_HEIGHT = 128;

	public static class dlightmappage_t extends BufferStruct // unnamed structs collide in the datadesc macros
	{
		public byte[] data = new byte[MAX_LIGHTMAPPAGE_WIDTH * MAX_LIGHTMAPPAGE_HEIGHT];
		public byte[] palette = new byte[256 * 4];
	};

	// compressed color format
	// Standard RGB format can be obtained from this by multiplying each colour component by 2^(exponent).
	// For faces with bumpmapped textures, there are four times the usual number of lightmap samples,
	// presumably containing samples used to compute the bumpmapping.
	public static class ColorRGBExp32 extends BufferStruct {
		public byte r, g, b;
		public byte exponent;
	};

	public static class CompressedLightCube extends BufferStruct {
		public ColorRGBExp32[] m_Color = new ColorRGBExp32[6];
	};

	public static class dlightmappageinfo_t extends BufferStruct // unnamed structs collide in the datadesc macros
	{
		public byte page; // lightmap page [0..?]
		public byte[] offset = new byte[2]; // offset into page (s,t)
		public byte pad; // unused
		public ColorRGBExp32 avgColor; // average used for runtime lighting calcs
	};

	public static class entity_t extends BufferStruct {
		public float[] origin = new float[3];
		public int firstbrush;
		public int numbrushes;
		public long epairs; // epair_t*

		// only valid for func_areaportals
		public int areaportalnum;
		public int[] portalareas = new int[2];
		public long[] m_pPortalsLeadingIntoAreas = new long[2]; // portals leading into portalareas, portal_t*
	};

	public static class epair_t extends BufferStruct {
		public long next; // epair_t*
		public long key; // key*
		public long value; // byte*
	};

	public static class TexCoords extends BufferStruct {
		public float fS; // S coordinate
		public float fT; // T coordinate
	};

	public static class Flags {
		// contents flags are seperate bits
		// a given brush can contribute multiple content bits
		// multiple brushes can be in a single leaf

		// these definitions also need to be in q_shared.h!

		// lower bits are stronger, and will eat weaker brushes completely
		public static final int CONTENTS_EMPTY = 0; // No contents

		public static final int CONTENTS_SOLID = 0x1; // an eye is never valid in a solid
		public static final int CONTENTS_WINDOW = 0x2; // translucent, but not watery (glass)
		public static final int CONTENTS_AUX = 0x4;
		public static final int CONTENTS_GRATE = 0x8; // alpha-tested "grate" textures.  Bullets/sight pass through, but solids don't
		public static final int CONTENTS_SLIME = 0x10;
		public static final int CONTENTS_WATER = 0x20;
		public static final int CONTENTS_BLOCKLOS = 0x40; // block AI line of sight
		public static final int CONTENTS_OPAQUE = 0x80; // things that cannot be seen through (may be non-solid though)
		public static final int LAST_VISIBLE_CONTENTS = 0x80;

		public static final int ALL_VISIBLE_CONTENTS = (LAST_VISIBLE_CONTENTS | (LAST_VISIBLE_CONTENTS - 1));

		public static final int CONTENTS_TESTFOGVOLUME = 0x100;
		public static final int CONTENTS_UNUSED = 0x200;

		// unused 
		// NOTE: If it's visible, grab from the top + update LAST_VISIBLE_CONTENTS
		// if not visible, then grab from the bottom.
		public static final int CONTENTS_UNUSED6 = 0x400;

		public static final int CONTENTS_TEAM1 = 0x800; // per team contents used to differentiate collisions 
		public static final int CONTENTS_TEAM2 = 0x1000; // between players and objects on different teams

		// ignore CONTENTS_OPAQUE on surfaces that have SURF_NODRAW
		public static final int CONTENTS_IGNORE_NODRAW_OPAQUE = 0x2000;

		// hits entities which are MOVETYPE_PUSH (doors, plats, etc.)
		public static final int CONTENTS_MOVEABLE = 0x4000;

		// remaining contents are non-visible, and don't eat brushes
		public static final int CONTENTS_AREAPORTAL = 0x8000;

		public static final int CONTENTS_PLAYERCLIP = 0x10000;
		public static final int CONTENTS_MONSTERCLIP = 0x20000;

		// currents can be added to any other contents, and may be mixed
		public static final int CONTENTS_CURRENT_0 = 0x40000;
		public static final int CONTENTS_CURRENT_90 = 0x80000;
		public static final int CONTENTS_CURRENT_180 = 0x100000;
		public static final int CONTENTS_CURRENT_270 = 0x200000;
		public static final int CONTENTS_CURRENT_UP = 0x400000;
		public static final int CONTENTS_CURRENT_DOWN = 0x800000;

		public static final int CONTENTS_ORIGIN = 0x1000000; // removed before bsping an entity

		public static final int CONTENTS_MONSTER = 0x2000000; // should never be on a brush, only in game
		public static final int CONTENTS_DEBRIS = 0x4000000;
		public static final int CONTENTS_DETAIL = 0x8000000; // brushes to be added after vis leafs
		public static final int CONTENTS_TRANSLUCENT = 0x10000000; // auto set if any surface has trans
		public static final int CONTENTS_LADDER = 0x20000000;
		public static final int CONTENTS_HITBOX = 0x40000000; // use accurate hitboxes on trace

		// NOTE: These are stored in a short in the engine now.  Don't use more than 16 bits
		public static final int SURF_LIGHT = 0x0001; // value will hold the light strength
		public static final int SURF_SKY2D = 0x0002; // don't draw, indicates we should skylight + draw 2d sky but not draw the 3D skybox
		public static final int SURF_SKY = 0x0004; // don't draw, but add to skybox
		public static final int SURF_WARP = 0x0008; // turbulent water warp
		public static final int SURF_TRANS = 0x0010;
		public static final int SURF_NOPORTAL = 0x0020; // the surface can not have a portal placed on it
		public static final int SURF_TRIGGER = 0x0040; // FIXME: This is an xbox hack to work around elimination of trigger surfaces, which breaks occluders
		public static final int SURF_NODRAW = 0x0080; // don't bother referencing the texture

		public static final int SURF_HINT = 0x0100; // make a primary bsp splitter

		public static final int SURF_SKIP = 0x0200; // completely ignore, allowing non-closed brushes
		public static final int SURF_NOLIGHT = 0x0400; // Don't calculate light
		public static final int SURF_BUMPLIGHT = 0x0800; // calculate three lightmaps for the surface for bumpmapping
		public static final int SURF_NOSHADOWS = 0x1000; // Don't receive shadows
		public static final int SURF_NODECALS = 0x2000; // Don't receive decals
		public static final int SURF_NOCHOP = 0x4000; // Don't subdivide patches on this surface 
		public static final int SURF_HITBOX = 0x8000; // surface is part of a hitbox

		// -----------------------------------------------------
		// spatial content masks - used for spatial queries (traceline,etc.)
		// -----------------------------------------------------
		public static final int MASK_ALL = (0xFFFFFFFF);
		// everything that is normally solid
		public static final int MASK_SOLID = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_WINDOW | CONTENTS_MONSTER | CONTENTS_GRATE);
		// everything that blocks player movement
		public static final int MASK_PLAYERSOLID = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_PLAYERCLIP | CONTENTS_WINDOW | CONTENTS_MONSTER | CONTENTS_GRATE);
		// blocks npc movement
		public static final int MASK_NPCSOLID = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_MONSTERCLIP | CONTENTS_WINDOW | CONTENTS_MONSTER | CONTENTS_GRATE);
		// water physics in these contents
		public static final int MASK_WATER = (CONTENTS_WATER | CONTENTS_MOVEABLE | CONTENTS_SLIME);
		// everything that blocks lighting
		public static final int MASK_OPAQUE = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_OPAQUE);
		// everything that blocks lighting, but with monsters added.
		public static final int MASK_OPAQUE_AND_NPCS = (MASK_OPAQUE | CONTENTS_MONSTER);
		// everything that blocks line of sight for AI
		public static final int MASK_BLOCKLOS = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_BLOCKLOS);
		// everything that blocks line of sight for AI plus NPCs
		public static final int MASK_BLOCKLOS_AND_NPCS = (MASK_BLOCKLOS | CONTENTS_MONSTER);
		// everything that blocks line of sight for players
		public static final int MASK_VISIBLE = (MASK_OPAQUE | CONTENTS_IGNORE_NODRAW_OPAQUE);
		// everything that blocks line of sight for players, but with monsters added.
		public static final int MASK_VISIBLE_AND_NPCS = (MASK_OPAQUE_AND_NPCS | CONTENTS_IGNORE_NODRAW_OPAQUE);
		// bullets see these as solid
		public static final int MASK_SHOT = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_MONSTER | CONTENTS_WINDOW | CONTENTS_DEBRIS | CONTENTS_HITBOX);
		// non-raycasted weapons see this as solid (includes grates)
		public static final int MASK_SHOT_HULL = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_MONSTER | CONTENTS_WINDOW | CONTENTS_DEBRIS | CONTENTS_GRATE);
		// hits solids (not grates) and passes through everything else
		public static final int MASK_SHOT_PORTAL = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_WINDOW | CONTENTS_MONSTER);
		// everything normally solid, except monsters (world+brush only)
		public static final int MASK_SOLID_BRUSHONLY = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_WINDOW | CONTENTS_GRATE);
		// everything normally solid for player movement, except monsters (world+brush only)
		public static final int MASK_PLAYERSOLID_BRUSHONLY = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_WINDOW | CONTENTS_PLAYERCLIP | CONTENTS_GRATE);
		// everything normally solid for npc movement, except monsters (world+brush only)
		public static final int MASK_NPCSOLID_BRUSHONLY = (CONTENTS_SOLID | CONTENTS_MOVEABLE | CONTENTS_WINDOW | CONTENTS_MONSTERCLIP | CONTENTS_GRATE);
		// just the world, used for route rebuilding
		public static final int MASK_NPCWORLDSTATIC = (CONTENTS_SOLID | CONTENTS_WINDOW | CONTENTS_MONSTERCLIP | CONTENTS_GRATE);
		// These are things that can split areaportals
		public static final int MASK_SPLITAREAPORTAL = (CONTENTS_WATER | CONTENTS_SLIME);

		// UNDONE: This is untested, any moving water
		public static final int MASK_CURRENT = (CONTENTS_CURRENT_0 | CONTENTS_CURRENT_90 | CONTENTS_CURRENT_180 | CONTENTS_CURRENT_270 | CONTENTS_CURRENT_UP | CONTENTS_CURRENT_DOWN);

		// everything that blocks corpse movement
		// UNDONE: Not used yet / may be deleted
		public static final int MASK_DEADSOLID = (CONTENTS_SOLID | CONTENTS_PLAYERCLIP | CONTENTS_WINDOW | CONTENTS_GRATE);

	}

}