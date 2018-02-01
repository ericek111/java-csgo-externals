package me.lixko.csgoexternals.util.bsp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.lixko.csgoexternals.sdk.BSPFile;
import me.lixko.csgoexternals.sdk.BSPFile.Lumps;
import me.lixko.csgoexternals.sdk.BSPFile.dedge_t;
import me.lixko.csgoexternals.sdk.BSPFile.dface_t;
import me.lixko.csgoexternals.sdk.BSPFile.dleaf_t;
import me.lixko.csgoexternals.sdk.BSPFile.dnode_t;
import me.lixko.csgoexternals.sdk.BSPFile.dplane_t;
import me.lixko.csgoexternals.sdk.BSPFile.dleafface_t;
import me.lixko.csgoexternals.sdk.BSPFile.dvertex_t;
import me.lixko.csgoexternals.sdk.BSPFile.dsurfedge_t;
import me.lixko.csgoexternals.sdk.BSPFile.texinfo_t;
import me.lixko.csgoexternals.util.BufferStruct;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.sdk.BSPFile.dtexdata_t;
import me.lixko.csgoexternals.sdk.BSPFile.dmodel_t;

public class BSPParser {
	public ByteBuffer stream;
	public BSPFile.dheader_t header = new BSPFile.dheader_t();
	public BSPFile.dleaf_t[] leafs = new BSPFile.dleaf_t[1];
	public BSPFile.dplane_t[] planes = new BSPFile.dplane_t[1];
	public BSPFile.dnode_t[] nodes = new BSPFile.dnode_t[1];
	public BSPFile.dface_t[] faces = new BSPFile.dface_t[1];
	public BSPFile.dleafface_t[] leafFaces = new BSPFile.dleafface_t[1];
	public BSPFile.dedge_t[] edges = new BSPFile.dedge_t[1];
	public BSPFile.dsurfedge_t[] surfEdges = new BSPFile.dsurfedge_t[1];
	public BSPFile.dmodel_t[] models = new BSPFile.dmodel_t[1];
	public float[] vertices = new float[1];
	public float[] normals = new float[1];
	public BSPFile.texinfo_t[] texinfo = new BSPFile.texinfo_t[1];
	public BSPFile.dtexdata_t[] texdata = new BSPFile.dtexdata_t[1];

	public Entity[] pEntities;
	public Entity[] ppBrushEntities;
	public Entity[] ppSpecialEntities;
	int nBrushEntities;
	int nSpecialEntities;

	ArrayList<Integer> indices = new ArrayList<Integer>();
	HashMap<String, ModelInfo> modelInfo = new HashMap<String, ModelInfo>();

	public BSPParser(File f) throws IOException {
		this.stream = ByteBuffer.wrap(Files.readAllBytes(f.toPath()));
	}

	public void parse() {
		stream.rewind();
		int ident = stream.getInt();
		if (ident == BSPFile.IDBSPHEADER)
			stream.order(ByteOrder.BIG_ENDIAN);
		else
			stream.order(ByteOrder.LITTLE_ENDIAN);
		stream.rewind();
		header.readFrom(stream);

		leafs = (dleaf_t[]) ReadLump(leafs, Lumps.LUMP_LEAFS);
		planes = (dplane_t[]) ReadLump(planes, Lumps.LUMP_PLANES);
		nodes = (dnode_t[]) ReadLump(nodes, Lumps.LUMP_NODES);
		models = (dmodel_t[]) ReadLump(models, Lumps.LUMP_MODELS);
		faces = (dface_t[]) ReadLump(faces, Lumps.LUMP_FACES);
		vertices = ReadVertices(vertices, Lumps.LUMP_VERTEXES);
		edges = (dedge_t[]) ReadLump(edges, Lumps.LUMP_EDGES);
		surfEdges = (dsurfedge_t[]) ReadLump(surfEdges, Lumps.LUMP_SURFEDGES);
		leafFaces = (dleafface_t[]) ReadLump(leafFaces, Lumps.LUMP_LEAFFACES);
		texinfo = (texinfo_t[]) ReadLump(texinfo, Lumps.LUMP_TEXINFO);
		texdata = (dtexdata_t[]) ReadLump(texdata, Lumps.LUMP_TEXDATA);

		System.out.println("Vertices: " + vertices.length + " floats => " + (float) vertices.length / 3f);
		normals = new float[vertices.length];

		ParseEntities();
		
		for(Entity e : pEntities) {
			boolean cont = false;
			for (Map.Entry<String, String> property : e.properties.entrySet()) {
				if(property.getValue().contains("cell door")) {
					cont = true;
				}
			}
			if(!cont) continue;
			e.properties.forEach((k,v) -> System.out.println(k + ": " + v));
			System.out.println("=======================");
		}
		
		//System.exit(0);

		/*
		 * for (dplane_t plane : planes) {
		 * System.out.println(plane.dist);
		 * }
		 */

		/*
		 * System.exit(0);
		 * int curLeaf = WalkBSPTree(DrawUtils.lppos.getOrigin(), 0);
		 * System.out.println("Current leaf: " + curLeaf);
		 * 
		 * ComputeBSP(0, DrawUtils.lppos.getOrigin());
		 */
	}

	private void ParseEntities() {
		int lumplen = header.lumps[Lumps.LUMP_ENTITIES.ordinal()].filelen;
		if (lumplen == 0)
			return;
		byte[] pszEntityBuffer = new byte[lumplen];
		stream.position(header.lumps[Lumps.LUMP_ENTITIES.ordinal()].fileofs);
		stream.get(pszEntityBuffer);		

		// Count entities
		int nEntities = 0;
		for (int i = 0; i < pszEntityBuffer.length; i++) {
			if (pszEntityBuffer[i] == '{')
				nEntities++;
		}

		pEntities = StringFormat.fill(new Entity[nEntities], () -> new Entity());

		nBrushEntities = 0;
		nSpecialEntities = 0;

		// Start over
		int pchPos = 0;
		int pchClose = 0;

		// Loop for each entity and parse it. count number of solid and special pEntities
		for (int i = 0; i < nEntities; i++) {
			pchPos = StringFormat.find(pszEntityBuffer, '{', pchPos);
			pchClose = StringFormat.find(pszEntityBuffer, '}', pchPos);
			int nLen = pchClose - pchPos - 1;

			//System.out.printf("#%d pchPos: %d, pchClose: %d, nLen: %d\n", i, pchPos, pchClose, nLen);
			//System.out.println(new String(Arrays.copyOfRange(pszEntityBuffer, pchPos, pchClose)));
			pEntities[i].ParseProperties(pszEntityBuffer, pchPos, nLen);

			if (pEntities[i].IsBrushEntity())
				nBrushEntities++;
			else
				nSpecialEntities++;

			pchPos++;
		}
		System.out.printf("nBrushEntities: %d\nnSpecialEntities: %d\n", nBrushEntities, nSpecialEntities);
		ppBrushEntities = new Entity[nBrushEntities];
		ppSpecialEntities = new Entity[nSpecialEntities];

		int iBrush = 0;
		int iSpecial = 0;
		for (int i = 0; i < nEntities; i++) {
			if (pEntities[i].IsBrushEntity()) {
				ppBrushEntities[iBrush] = pEntities[i];

				//if Entity has property "origin" apply to model struct for rendering
				String originstr = pEntities[i].properties.get("origin");
				if (originstr != null) {
					try {
						int iModel = Integer.parseInt(pEntities[i].properties.get("model").replaceAll("[^\\d.]", ""));
						float[] origin = GetCoordsFromString(originstr);
						models[iModel].origin = origin;
					} catch (Exception ex) {
						ex.printStackTrace();
						break;
					}
				}
				iBrush++;
			} else {
				ppSpecialEntities[iSpecial] = pEntities[i];
				iSpecial++;
			}
		}

		// sort brush entities so that these with rendermode texture are at the back
		//qsort(ppBrushEntities, nBrushEntities, sizeof(Entity*), BrushEntityCmp);

	}

	// https://github.com/aKalisch/gosx-public-external/blob/master/Engine/ToolManager/bsp.cpp
	public dleaf_t GetLeafFromPoint(float[] point) {
		int nodenum = 0;
		dnode_t node;
		dplane_t plane;

		float d = 0.0f;

		while (nodenum >= 0) {
			node = nodes[nodenum];
			plane = planes[node.planenum];
			d = MathUtils.dotProduct(point, plane.normal) - plane.dist;
			if (d > 0) {
				nodenum = node.children[0];
			} else {
				nodenum = node.children[1];
			}
		}

		return leafs[-nodenum - 1];
	}

	public boolean isVisible(float[] start, float[] end) {
		float[] direction = MathUtils.csubtract(end, start);
		float[] point = start.clone();

		int steps = (int) MathUtils.VecLength(direction);

		if (steps > 4000) {
			return false;
		}

		MathUtils.divide(direction, steps);

		dleaf_t leaf;

		while (steps > 0) {
			MathUtils.add(point, direction);
			leaf = GetLeafFromPoint(point);
			if ((leaf.contents & BSPFile.Flags.CONTENTS_SOLID) != 0 || (leaf.contents & BSPFile.Flags.CONTENTS_DETAIL) != 0) {
				return false;
			}

			--steps;
		}
		return true;
	}

	public void LoadBrushEntities() {
		float[] mat1 = new float[16];
		Arrays.fill(mat1, 1f);
		for (int i = 0; i < ppBrushEntities.length; i++) {
			Entity pCurEnt = ppBrushEntities[i];

			String modelName = pCurEnt.properties.get("model");
			if (modelName == null)
				continue;

			int iModel = Integer.parseInt(modelName.substring(1));

			String originStr = pCurEnt.properties.get("origin");
			if (originStr == null)
				continue;

			ModelInfo info = new ModelInfo();

			float[] origin = GetCoordsFromString(originStr);

			info.origin = origin;
			info.matrix = MathUtils.Matrix4x4.translate(mat1, origin);

			info.startIndex = indices.size() + 1;

			for (int x = 0; x < models[iModel].numfaces; x++) {
				LoadFace(models[iModel].firstface + x);
			}

			info.length = indices.size() - info.startIndex;
			modelInfo.put(modelName, info);
		}
	}

	public static float[] GetCoordsFromString(String str) {
		if (str == "") {
			System.out.printf("GetCoordsFromString: string is empty!\n");
			return new float[3];
		}
		String posarr[] = str.split(" ");
		if (posarr.length < 3) {
			System.out.printf("GetCoordsFromString: invalid origin string: %s\n", str);
			return new float[3];
		}

		float[] pos = new float[3];
		try {
			for (int i = 0; i < 3; i++)
				pos[i] = Float.parseFloat(posarr[i]);
		} catch (NumberFormatException ex) {
			System.out.printf("GetCoordsFromString: invalid origin string: %s\n", str);
			return new float[3];
		}
		return pos;
	}

	public Entity FindEntity(String classname) {
		if (pEntities != null)
			for (Entity e : pEntities)
				if (e.properties.get("classname").equals(classname))
					return e;
		return null;
	}

	public int WalkBSPTree(float[] pos, int node) {
		// https://github.com/Bumrang/OpenBSP/blob/860d28e31be5e635ba4d4c8e4ad022d1f55441d0/bsp.cpp#L176
		for (int x = 0; x < 2; x++) {
			if (nodes[node].children[x] >= 0) { // node
				if (MathUtils.pointIsInRange(pos, nodes[nodes[node].children[x]].mins, nodes[nodes[node].children[x]].maxs))
					return WalkBSPTree(pos, nodes[node].children[x]);
			} else if (~nodes[node].children[x] != 0) { // NOT x = -x - 1, so this an index for the leaves
				if (MathUtils.pointIsInRange(pos, leafs[~(nodes[node].children[x])].mins, leafs[~(nodes[node].children[x])].maxs))
					return ~nodes[node].children[x];
			}
		}
		return -1;
	}

	public void ComputeBSP(int node, float[] pos) {
		if (node < 0) {
			if (node == -1)
				return;

			LoadLeaf(~node);
			return;
		}

		float location;
		int planeIndex = nodes[node].planenum;

		if (planes[planeIndex].type == 0) // plane x
			location = pos[0] - planes[planeIndex].dist;
		else if (planes[planeIndex].type == 1) // plane y
			location = pos[1] - planes[planeIndex].dist;
		else if (planes[planeIndex].type == 2) // plane z
			location = pos[2] - planes[planeIndex].dist;
		else
			location = MathUtils.dotProduct(planes[planeIndex].normal, pos) - planes[planeIndex].dist;

		if (location > 0) {
			ComputeBSP(nodes[node].children[1], pos);
			ComputeBSP(nodes[node].children[0], pos);
		} else {
			ComputeBSP(nodes[node].children[0], pos);
			ComputeBSP(nodes[node].children[1], pos);
		}

	}

	void LoadLeaf(int leaf) {
		for (int x = 0; x < leafs[leaf].numleaffaces; x++) {
			LoadFace(leafFaces[leafs[leaf].firstleafface + x].value);
		}
	}

	void LoadFace(int face) {
		if (faces[face].styles[0] == 0xFF)
			return;

		// key indices for each face
		int vertPoint = 0;
		int rootPoint = 0; // if it is the first run through the first vertex is the "hub" index that all of the triangles in the plane will refer to
		int firstPoint = 0; // the first point after the hub
		int secondPoint = 0; // last point to create a full triangle

		// normal
		float[] vNormal = planes[faces[face].planenum].normal;

		if (faces[face].side > 0)
			MathUtils.negate(vNormal);

		// loop through every single edge in a face, this will end up making a triangle fan
		for (int x = 0; x < faces[face].numedges; x++) {
			int edgeIndex = surfEdges[faces[face].firstedge + x].value;
			dedge_t edge = edges[Math.abs(edgeIndex)];
			boolean reverse = (edgeIndex >= 0);

			if (x == 0) {
				rootPoint = edge.v[reverse ? 0 : 1];
				vertPoint = edge.v[reverse ? 1 : 0];
			} else {
				vertPoint = edge.v[reverse ? 0 : 1];
				if (vertPoint == rootPoint)
					continue;
				else
					firstPoint = vertPoint;

				vertPoint = edge.v[reverse ? 1 : 0];
				if (vertPoint == rootPoint)
					continue;
				else
					secondPoint = vertPoint;

				SetNormal(rootPoint, vNormal);
				SetNormal(firstPoint, vNormal);
				SetNormal(secondPoint, vNormal);
				//normals[rootPoint].point = vNormal;
				//normals[firstPoint].point = vNormal;
				//normals[secondPoint].point = vNormal;

				// push back every index 
				indices.add(rootPoint);
				indices.add(firstPoint);
				indices.add(secondPoint);
			}
		}
		indices.add(-1); // once we are done rendering our plane we put in our primitive restart index to start a new plane

	}

	public float[] ReadVertices(float[] arr, Lumps lump) {
		int filelen = header.lumps[lump.ordinal()].filelen;
		if (filelen == 0)
			return null;

		System.out.println(lump.name() + " / " + lump.ordinal() + " of " + filelen + " bytes \t + " + header.lumps[lump.ordinal()].fileofs);

		stream.position(header.lumps[lump.ordinal()].fileofs);
		int verticescount = filelen / Float.BYTES / 3;
		arr = new float[verticescount * 3];
		for (int v = 0; v < verticescount; v++) {
			for (int i = 0; i < 3; i++) {
				arr[v + i] = stream.getFloat();
			}
		}
		return arr;
	}

	public void SetNormal(int offset, float[] value) {
		for (int i = 0; i < value.length; i++) {
			normals[offset * 3 + i] = value[i];
		}
	}

	public float[] GetVertex(float[] dest, int index) {
		return new float[] { dest[index], dest[index + 1], dest[index + 2] };
	}

	public BufferStruct[] ReadLump(BufferStruct[] bufstruct, Lumps lump) {
		int filelen = header.lumps[lump.ordinal()].filelen;
		if (filelen == 0)
			return null;

		System.out.println(lump.name() + " / " + lump.ordinal() + " of " + filelen + " bytes \t + " + header.lumps[lump.ordinal()].fileofs);

		int structsize = 0;
		try {
			structsize = ((BufferStruct) bufstruct.getClass().getComponentType().newInstance()).size();
			if (structsize == 0)
				return null;
			//@formatter:off
			if(lump == Lumps.LUMP_AREAPORTALS 			&& filelen/structsize > BSPFile.MAX_MAP_AREAPORTALS
				|| lump == Lumps.LUMP_AREAS 			&& filelen/structsize > BSPFile.MAX_MAP_AREAS
				|| lump == Lumps.LUMP_BRUSHES 			&& filelen/structsize > BSPFile.MAX_MAP_BRUSHES
				|| lump == Lumps.LUMP_BRUSHSIDES 		&& filelen/structsize > BSPFile.MAX_MAP_BRUSHSIDES
				|| lump == Lumps.LUMP_CLIPPORTALVERTS 	&& filelen/structsize > BSPFile.MAX_MAP_PORTALVERTS
				|| lump == Lumps.LUMP_DISPINFO 			&& filelen/structsize > BSPFile.MAX_MAP_DISPINFO
				|| lump == Lumps.LUMP_DISP_TRIS 		&& filelen/structsize > BSPFile.MAX_MAP_DISP_TRIS
				|| lump == Lumps.LUMP_DISP_VERTS 		&& filelen/structsize > BSPFile.MAX_MAP_DISP_VERTS
				|| lump == Lumps.LUMP_EDGES 			&& filelen/structsize > BSPFile.MAX_MAP_EDGES
				|| lump == Lumps.LUMP_ENTITIES 			&& filelen/structsize > BSPFile.MAX_MAP_ENTITIES
				|| lump == Lumps.LUMP_FACES 			&& filelen/structsize > BSPFile.MAX_MAP_FACES
				|| lump == Lumps.LUMP_LEAFBRUSHES 		&& filelen/structsize > BSPFile.MAX_MAP_LEAFBRUSHES
				|| lump == Lumps.LUMP_LEAFFACES 		&& filelen/structsize > BSPFile.MAX_MAP_LEAFFACES
				|| lump == Lumps.LUMP_LEAFS 			&& filelen/structsize > BSPFile.MAX_MAP_LEAFS
				|| lump == Lumps.LUMP_LEAFWATERDATA 	&& filelen/structsize > BSPFile.MAX_MAP_LEAFWATERDATA
				|| lump == Lumps.LUMP_LIGHTING 			&& filelen/structsize > BSPFile.MAX_MAP_LIGHTING
				|| lump == Lumps.LUMP_MODELS 			&& filelen/structsize > BSPFile.MAX_MAP_MODELS
				|| lump == Lumps.LUMP_NODES 			&& filelen/structsize > BSPFile.MAX_MAP_NODES
				|| lump == Lumps.LUMP_OVERLAYS 			&& filelen/structsize > BSPFile.MAX_MAP_OVERLAYS
				|| lump == Lumps.LUMP_PLANES 			&& filelen/structsize > BSPFile.MAX_MAP_PLANES
				|| lump == Lumps.LUMP_PRIMINDICES 		&& filelen/structsize > BSPFile.MAX_MAP_PRIMINDICES
				|| lump == Lumps.LUMP_PRIMITIVES 		&& filelen/structsize > BSPFile.MAX_MAP_PRIMITIVES
				|| lump == Lumps.LUMP_PRIMVERTS 		&& filelen/structsize > BSPFile.MAX_MAP_PRIMVERTS
				|| lump == Lumps.LUMP_SURFEDGES 		&& filelen/structsize > BSPFile.MAX_MAP_SURFEDGES
				|| lump == Lumps.LUMP_TEXDATA 			&& filelen/structsize > BSPFile.MAX_MAP_TEXDATA
				|| lump == Lumps.LUMP_TEXDATA_STRING_DATA && filelen/structsize > BSPFile.MAX_MAP_TEXDATA_STRING_DATA
				|| lump == Lumps.LUMP_TEXDATA_STRING_TABLE && filelen/structsize > BSPFile.MAX_MAP_TEXDATA_STRING_TABLE
				|| lump == Lumps.LUMP_TEXINFO 			&& filelen/structsize > BSPFile.MAX_MAP_TEXINFO
				|| lump == Lumps.LUMP_VERTEXES 			&& filelen/structsize > BSPFile.MAX_MAP_VERTS
				|| lump == Lumps.LUMP_VERTNORMALINDICES && filelen/structsize > BSPFile.MAX_MAP_VERTNORMALINDICES
				|| lump == Lumps.LUMP_VERTNORMALS 		&& filelen/structsize > BSPFile.MAX_MAP_VERTNORMALS
				|| lump == Lumps.LUMP_VISIBILITY 		&& filelen/structsize > BSPFile.MAX_MAP_VISIBILITY
				|| lump == Lumps.LUMP_WATEROVERLAYS 	&& filelen/structsize > BSPFile.MAX_MAP_WATEROVERLAYS
				|| lump == Lumps.LUMP_WORLDLIGHTS 		&& filelen/structsize > BSPFile.MAX_MAP_WORLDLIGHTS)
				throw new IndexOutOfBoundsException("Invalid " + lump.name() + " count: " + filelen/structsize);
			//@formatter:on
			int structcount = (int) Math.ceil((float) filelen / (float) structsize);
			bufstruct = (BufferStruct[]) Array.newInstance(bufstruct.getClass().getComponentType(), structcount);

			stream.position(header.lumps[lump.ordinal()].fileofs);

			System.out.println(structcount + " structs , size " + structsize + "\t\tWTF factor: " + filelen % structsize);
			for (int i = 0; i < structcount; i++) {
				bufstruct[i] = (BufferStruct) bufstruct.getClass().getComponentType().newInstance();
				bufstruct[i].readFrom(stream);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return bufstruct;
	}

	public class Entity {
		public HashMap<String, String> properties = new HashMap<String, String>();

		public void ParseProperties(byte[] pszEntityBuffer, int off, int len) {
			// Count num of properties
			int nProperties = 0;
			for (int i = 0; i < len; i++) {
				if (pszEntityBuffer[off + i] == '"')
					nProperties++;
			}

			// There are 4 quotes per line, so this gives the num of lines/entity properties
			nProperties /= 4;

			// Start over
			int pchPos = off;

			// Run for each line
			for (int i = 0; i < nProperties; ++i) {
				// Find opening of name
				pchPos = StringFormat.find(pszEntityBuffer, '"', pchPos);
				// Advance past the opening "
				pchPos++;
				// Find the closing "
				int pchClose = StringFormat.find(pszEntityBuffer, '"', pchPos);

				int nLen = pchClose - pchPos;
				byte[] propertyNameBuf = Arrays.copyOfRange(pszEntityBuffer, pchPos, pchPos + nLen);

				// Move past the closing "
				pchPos = pchClose + 1;
				// Find opening of value
				pchPos = StringFormat.find(pszEntityBuffer, '"', pchPos);
				// Advance past the opening "
				pchPos++;
				// Find the closing "
				pchClose = pchPos;
				pchClose = StringFormat.find(pszEntityBuffer, '"', pchClose);

				nLen = pchClose - pchPos;
				byte[] propertyValueBuf = Arrays.copyOfRange(pszEntityBuffer, pchPos, pchPos + nLen);

				properties.put(new String(propertyNameBuf), new String(propertyValueBuf));

				// Move past the closing "
				pchPos = pchClose + 1;
			}
		}

		public boolean IsBrushEntity() {
			if (!properties.containsKey("model"))
				return false;

			String className = properties.get("classname");

			//@formatter:off
			return (className.equals("func_door_rotating") ||
					className.equals("func_door") ||
					className.equals("func_illusionary") ||
					className.equals("func_wall") ||
					className.equals("func_breakable") ||
					className.equals("func_button") ||
					className.equals("func_rotating") ||
					className.equals("func_brush") ||
					className.equals("func_buyzone") ||
					className.equals("func_bomb_target") );
			//@formatter:on
		}

	}

	public class ModelInfo {
		int startIndex;
		int length;
		float[] origin = new float[3];
		float[] matrix = new float[4 * 4];
	}
}
