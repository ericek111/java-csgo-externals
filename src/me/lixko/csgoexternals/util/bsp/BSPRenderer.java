package me.lixko.csgoexternals.util.bsp;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;

import me.lixko.csgoexternals.sdk.BSPFile.Lumps;
import me.lixko.csgoexternals.sdk.BSPFile.TexCoords;
import me.lixko.csgoexternals.sdk.BSPFile.texinfo_t;

import java.awt.DisplayMode;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.bsp.BSPParser.Entity;

public class BSPRenderer extends BSPParser implements GLEventListener {

	private int[] pnLightmapLookUp;
	private byte[] lightMapData;
	private int[] m_vao = new int[1], m_normalVBO = new int[1], m_ebo = new int[1], m_vbo = new int[1];
	int m_MVPUniform, m_modelUniform, m_viewUniform, m_projectionUniform;
	int m_lightUniform, m_cameraUniform;
	int m_useUserColorUniform, m_userColorUniform;
	int m_positionAttribute, m_normalPositionAttribute;
	boolean m_bWireframe;
	boolean m_bBrushEntityNames;

	private Shader m_bspShader;
	Camera cam;
	float[] origin, angles;
	int worldSize;

	public static ShaderManager shaderManager = new ShaderManager();
	public static DisplayMode dm, dm_old;
	private GLU glu = new GLU();
	private boolean needsDataUpdate = false;

	private float[] mat1 = new float[4 * 4];

	public BSPRenderer(File f) throws IOException {
		super(f);
		Arrays.fill(mat1, 1f);
	}

	@Override
	public void parse() {

		super.parse();
		//System.exit(0);

		Entity info_player = FindEntity("info_player_counterterrorist");
		if (info_player != null) {
			origin = GetCoordsFromString(info_player.properties.get("origin"));
			angles = GetCoordsFromString(info_player.properties.get("angles"));
		} else {
			System.err.println("CT info player (starting point) not found!");
		}
		
		int curLeaf = WalkBSPTree(origin, 0);
		System.out.println("Current leaf: " + curLeaf);

		ComputeBSP(0, origin);

		pnLightmapLookUp = new int[faces.length];
		//LoadLightMaps();
		worldSize = indices.size();
		LoadBrushEntities();
		
		
	}

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		begin(drawable, gl);
		render(drawable, gl);
		end(drawable, gl);

		gl.glFlush();
		this.needsDataUpdate = true;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		DrawUtils.gl = drawable.getGL().getGL2();
		DrawUtils.glu = new GLU();
		DrawUtils.drawable = drawable;
		this.needsDataUpdate = true;
		
		DrawUtils.gl.glEnable(GL.GL_DEPTH_TEST);
		DrawUtils.gl.glDepthFunc(GL.GL_LESS);
		
		DrawUtils.gl.glEnable(GL2.GL_PRIMITIVE_RESTART);
		DrawUtils.gl.glPrimitiveRestartIndex(-1);
		
		DrawUtils.gl.glEnable(GL.GL_CULL_FACE);
		DrawUtils.gl.glCullFace(GL.GL_FRONT);

		// Initialize basic shader
		m_bspShader = shaderManager.GetShader("basic_shader");
		if (m_bspShader == null) {
			System.out.println("[Shaders] Failed to load basic_shader!");
			return;
		}
		m_bspShader.use();
		
		cam = new Camera(DrawUtils.getScreenWidth(), DrawUtils.getScreenHeight());

		m_MVPUniform = m_bspShader.GetUniformLocation("MVP");
		m_modelUniform = m_bspShader.GetUniformLocation("model");
		m_viewUniform = m_bspShader.GetUniformLocation("view");
		m_projectionUniform = m_bspShader.GetUniformLocation("projection");
		m_lightUniform = m_bspShader.GetUniformLocation("lightPosition");
		m_useUserColorUniform = m_bspShader.GetUniformLocation("useUserColor");
		m_userColorUniform = m_bspShader.GetUniformLocation("userColor");

		DrawUtils.gl.glGenVertexArrays(1, m_vao, 0);
		DrawUtils.gl.glBindVertexArray(m_vao[0]);

		// FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);
		DrawUtils.gl.glGenBuffers(1, m_vbo, 0);
		DrawUtils.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, m_vbo[0]);
		DrawUtils.gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, FloatBuffer.wrap(vertices), GL.GL_STATIC_DRAW);

		DrawUtils.gl.glGenBuffers(1, m_normalVBO, 0);
		DrawUtils.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, m_normalVBO[0]);
		DrawUtils.gl.glBufferData(GL.GL_ARRAY_BUFFER, normals.length * Float.BYTES, FloatBuffer.wrap(normals), GL.GL_STATIC_DRAW);

		DrawUtils.gl.glGenBuffers(1, m_ebo, 0);
		DrawUtils.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, m_ebo[0]);
		int[] indicesarr = indices.stream().mapToInt(i -> i).toArray();
		DrawUtils.gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indicesarr.length * Integer.BYTES, IntBuffer.wrap(indicesarr), GL.GL_STATIC_DRAW);

		m_positionAttribute = m_bspShader.GetAttribLocation("position");
		DrawUtils.gl.glEnableVertexAttribArray(m_positionAttribute);
		DrawUtils.gl.glVertexAttribPointer(m_positionAttribute, 3, GL.GL_FLOAT, false, 0, 0);

		m_normalPositionAttribute = m_bspShader.GetAttribLocation("normal");
		DrawUtils.gl.glEnableVertexAttribArray(m_normalPositionAttribute);
		DrawUtils.gl.glVertexAttribPointer(m_normalPositionAttribute, 3, GL.GL_FLOAT, false, 0, 0);

		m_bspShader.use();

		updateLoop.start();
	}

	private void begin(GLAutoDrawable drawable, GL2 gl) {
		cam.position = DrawUtils.lppos.getViewOffset();
		cam.pitch = DrawUtils.lppos.getPitch();
		cam.yaw = DrawUtils.lppos.getYaw();
		gl.glBindVertexArray(m_vao[0]);

		gl.glEnableVertexAttribArray(m_positionAttribute);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, m_vbo[0]);
		gl.glVertexAttribPointer(m_positionAttribute, 3, GL.GL_FLOAT, false, 0, 0);

		gl.glEnableVertexAttribArray(m_normalPositionAttribute);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, m_normalVBO[0]);
		gl.glVertexAttribPointer(m_normalPositionAttribute, 3, GL.GL_FLOAT, false, 0, 0);
		
		if(m_bspShader == null) {
			System.out.println("wtf");
			return;
		}
		m_bspShader.use();
	}

	private void render(GLAutoDrawable drawable, GL2 gl) {
		float[] lightUniformTrans = new float[] { -626.0f, -4289.3f, 375.5f };
		gl.glUniformMatrix4fv(m_MVPUniform, 1, false, MathUtils.Matrix4x4.mul(cam.getProjection(), cam.getView(), mat1), 0);
		gl.glUniformMatrix4fv(m_modelUniform, 1, false, mat1, 0);
		gl.glUniformMatrix4fv(m_viewUniform, 1, false, cam.getView(), 0);
		gl.glUniformMatrix4fv(m_projectionUniform, 1, false, cam.getProjection(), 0);

		gl.glUniform3fv(m_lightUniform, 1, lightUniformTrans, 0);

		gl.glUniform1i(m_useUserColorUniform, GL.GL_TRUE);

		gl.glUniform4f(m_userColorUniform, 0.9f, 0.9f, 0.9f, 1.0f);

		if (!m_bWireframe)
			gl.glDrawElements(GL.GL_TRIANGLES, worldSize, GL.GL_UNSIGNED_INT, 0);
		//else

		gl.glUniform4f(m_userColorUniform, 0.0f, 0.0f, 1.0f, 1.0f);
		gl.glDrawElements(GL.GL_LINE_LOOP, worldSize, GL.GL_UNSIGNED_INT, 0);
		gl.glUniform1i(m_useUserColorUniform, GL.GL_FALSE);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		
		RenderBrushEntities(m_modelUniform, m_userColorUniform, m_useUserColorUniform, drawable, gl);
	}
	
	private void RenderBrushEntities(int uniformModel, int uniformColor, int useUserColorUniform, GLAutoDrawable drawable, GL2 gl) {
		for (int i = 0; i < nBrushEntities; i++)
		{
			;
			if (ppBrushEntities[i] == null)
				continue;
			String modelName = ppBrushEntities[i].properties.get("model");
			if(modelName == null) continue;
			
			ModelInfo info = modelInfo.get(modelName);
			if(info == null) continue;
			gl.glUniformMatrix4fv(uniformModel, 1, false, info.matrix, 0);
	        
	        //glUniform4f(uniformColor, 1.0f, 1.0f, 1.0f, 1.0f); // white
	        //glDrawElements(GL_TRIANGLES, info.length, GL_UNSIGNED_INT, (void*)(info.startIndex * sizeof(GLuint)));
	        gl.glUniform1i(useUserColorUniform, GL.GL_TRUE);
	        gl.glUniform4f(uniformColor, 0.75f, 0.0f, 0.75f, 1.0f); // purp
	        gl.glDrawElements(GL.GL_LINE_LOOP, info.length, GL.GL_UNSIGNED_INT, info.startIndex * 4);
	        gl.glUniform1i(useUserColorUniform, GL.GL_FALSE);
		}
	}

	private void end(GLAutoDrawable drawable, GL2 gl) {
		gl.glDisableVertexAttribArrayARB(m_positionAttribute);
		gl.glDisableVertexAttribArrayARB(m_normalPositionAttribute);
		
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_LIGHT0);
		gl.glDisable(GL2.GL_NORMALIZE);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if (height <= 0)
			height = 1;
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90.0f, h, 1.0, 8000.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	private void LoadLightMaps() {
		int lumplen = header.lumps[Lumps.LUMP_LIGHTING.ordinal()].filelen;
		if (lumplen == 0)
			return;
		lightMapData = new byte[lumplen];
		stream.position(header.lumps[Lumps.LUMP_LIGHTING.ordinal()].fileofs);
		stream.get(lightMapData);

		int nLoadedData = 0;
		int nLoadedLightmaps = 0;
		//int nErrors = 0;

		for (int i = 0; i < faces.length; i++) {
			if (faces[i].styles[0] == 0 && faces[i].lightofs >= -1) {
				//Allocate pLightmapCoords
				TexCoords[] pLightmapCoords = new TexCoords[faces[i].numedges];

				/* *********** QRAD ********** */
				float fMinU = 999999;
				float fMinV = 999999;
				float fMaxU = -99999;
				float fMaxV = -99999;

				texinfo_t info = texinfo[faces[i].texinfo];

				for (int j = 0; j < faces[i].numedges; j++) {
					int iEdge = surfEdges[faces[i].firstedge + j].value;
					float[] vertex = new float[3];
					if (iEdge >= 0)
						vertex = GetVertex(vertices, edges[iEdge].v[0]); // vertices[edges[iEdge].v[0]];
					else
						vertex = GetVertex(vertices, edges[-iEdge].v[1]); // vertices[edges[-iEdge].v[1]];

					float fU = info.lightmapVecs[0][0] * vertex[0] + info.lightmapVecs[0][1] * vertex[1] + info.lightmapVecs[0][2] * vertex[2] + info.lightmapVecs[0][3]; //DotProduct(texInfo.vS, vertex) + texInfo.fSShift;
					if (fU < fMinU)
						fMinU = fU;
					if (fU > fMaxU)
						fMaxU = fU;

					float fV = info.lightmapVecs[1][0] * vertex[0] + info.lightmapVecs[1][1] * vertex[1] + info.lightmapVecs[1][2] * vertex[2] + info.lightmapVecs[1][3];
					if (fV < fMinV)
						fMinV = fV;
					if (fV > fMaxV)
						fMaxV = fV;
				}

				int nWidth = (int) (Math.ceil(fMaxU / 16.0f) - Math.floor(fMinU / 16.0f)) + 1;
				int nHeight = (int) (Math.ceil(fMaxV / 16.0f) - Math.floor(fMinV / 16.0f)) + 1;

				/* *********** end QRAD ********* */

				/* http://www.gamedev.net/community/forums/topic.asp?topic_id=538713 (last refresh: 20.02.2010) */

				float fMidPolyU = (float) (fMinU + fMaxU) / 2.0f;
				float fMidPolyV = (float) (fMinV + fMaxV) / 2.0f;
				float fMidTexU = (float) (nWidth) / 2.0f;
				float fMidTexV = (float) (nHeight) / 2.0f;

				for (int j = 0; j < faces[i].numedges; ++j) {

					int iEdge = surfEdges[faces[i].firstedge + j].value;
					float[] vertex = new float[3];
					if (iEdge >= 0)
						vertex = GetVertex(vertices, edges[iEdge].v[0]); // vertices[edges[iEdge].v[0]];
					else
						vertex = GetVertex(vertices, edges[-iEdge].v[1]); // vertices[edges[-iEdge].v[1]];

					float fU = info.lightmapVecs[0][0] * vertex[0] + info.lightmapVecs[0][1] * vertex[1] + info.lightmapVecs[0][2] * vertex[2] + info.lightmapVecs[0][3];
					float fV = info.lightmapVecs[1][0] * vertex[0] + info.lightmapVecs[1][1] * vertex[1] + info.lightmapVecs[1][2] * vertex[2] + info.lightmapVecs[1][3];

					float fLightMapU = fMidTexU + (fU - fMidPolyU) / 16.0f;
					float fLightMapV = fMidTexV + (fV - fMidPolyV) / 16.0f;

					pLightmapCoords[j].fS = fLightMapU / (float) nWidth;
					pLightmapCoords[j].fT = fLightMapV / (float) nHeight;
				}

				/* end http://www.gamedev.net/community/forums/topic.asp?topic_id=538713 */

				// Find unbound texture slots
				DrawUtils.gl.glGenTextures(1, pnLightmapLookUp, i);

				Image pImg = new Image(3, nWidth, nHeight);
				for (int b = 0; b < nWidth * nHeight * 3; b++) {
					pImg.pData[b] = lightMapData[faces[i].lightofs + b];
				}

				AdjustTextureToPowerOfTwo(pImg);

				// Bind the texture
				DrawUtils.gl.glBindTexture(GL.GL_TEXTURE_2D, pnLightmapLookUp[i]);

				// Set up Texture Filtering Parameters
				DrawUtils.gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
				DrawUtils.gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);

				DrawUtils.gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

				DrawUtils.gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, pImg.nWidth, pImg.nHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(pImg.pData));

				nLoadedLightmaps++;
				//printf("#%4d Loaded lightmap %2d x %2d\n", nLoadedLightmaps, nWidth, nHeight);

				nLoadedData += nWidth * nHeight * 3;
			} else {
				pnLightmapLookUp[i] = 0;
			}
		}

		System.out.printf("Loaded %d lightmaps, lightmapdatadiff: %d Bytes ", nLoadedLightmaps, nLoadedData - header.lumps[Lumps.LUMP_LIGHTING.ordinal()].filelen);
		if ((nLoadedData - header.lumps[Lumps.LUMP_LIGHTING.ordinal()].filelen) == 0)
			System.out.println("OK");
		else
			System.out.println("ERRORS");
	}

	private void AdjustTextureToPowerOfTwo(Image pImg) {
		if (((pImg.nWidth & (pImg.nWidth - 1)) == 0) && ((pImg.nHeight & (pImg.nHeight - 1)) == 0))
			return;

		DrawUtils.gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
		DrawUtils.gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

		int nPOT = 1;
		while (nPOT < pImg.nHeight || nPOT < pImg.nWidth)
			nPOT *= 2;

		// Scale image
		byte[] pNewData = new byte[nPOT * nPOT * pImg.nChannels];
		DrawUtils.glu.gluScaleImage(pImg.nChannels == 4 ? GL.GL_RGBA : GL.GL_RGB, pImg.nWidth, pImg.nHeight, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(pImg.pData), nPOT, nPOT, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(pNewData));

		pImg.nWidth = nPOT;
		pImg.nHeight = nPOT;
		pImg.pData = pNewData;
	}

}
