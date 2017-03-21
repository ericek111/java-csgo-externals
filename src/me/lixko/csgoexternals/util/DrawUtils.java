package me.lixko.csgoexternals.util;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextureRenderer;

import me.lixko.csgoexternals.themes.BasicTheme;

@SuppressWarnings("static-access")
public class DrawUtils {

	public static GL2 gl;

	public static GLAutoDrawable drawable;
	public static BasicTheme theme = new BasicTheme();
	public static FontRenderer fontRenderer = theme.fontRenderer;
	public static TextAlign align = TextAlign.LEFT;
	public static LocalPlayerPosition lppos = new LocalPlayerPosition();

	public static TextureRenderer mTextureRenderer;
	private static Graphics2D tTextureGraphics2D;
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

	private static boolean textBackground = true;
	private static float[] color = new float[4];

	static {
		String texturespath = FileUtil.mainpath + "/textures/";
		System.out.println(texturespath);
		for (File rankimg : (new File(texturespath + "ranks")).listFiles()) {
			addTexture(rankimg.getName().substring(0, rankimg.getName().lastIndexOf(".")), rankimg);
		}
		addTexture("defuser", new File(texturespath + "defuser.png"));
		addTexture("bomb", new File(texturespath + "bomb.png"));
		loadTextures();
	}

	public static void addTexture(String key, File imgfile) {
		try {
			textures.put(key, new Texture(imgfile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadTextures() {
		int totalwidth = 0;
		int maxheight = 0;
		for (Map.Entry<String, Texture> entry : textures.entrySet()) {
			Texture tex = entry.getValue();
			maxheight = Math.max(maxheight, tex.height);
			tex.texx = totalwidth;
			totalwidth += tex.width;
		}
		mTextureRenderer = new TextureRenderer(totalwidth, maxheight, true);
		tTextureGraphics2D = mTextureRenderer.createGraphics();
		for (Map.Entry<String, Texture> entry : textures.entrySet()) {
			Texture tex = entry.getValue();
			tTextureGraphics2D.drawImage(tex.img, tex.texx, 0, null);
		}
		tTextureGraphics2D.dispose();
		mTextureRenderer.markDirty(0, 0, totalwidth, maxheight);
	}

	public static void drawTexture(String key, int x, int y) {
		Texture tex = textures.get(key);

		mTextureRenderer.beginOrthoRendering(getScreenWidth() * 1, getScreenHeight() * 1);
		mTextureRenderer.drawOrthoRect(x, y, tex.texx, mTextureRenderer.getHeight() - tex.height, tex.width, tex.height);
		mTextureRenderer.endOrthoRendering();
	}

	public static void drawTexture(String key, int x, int y, int width, int height) {
		Texture tex = textures.get(key);
		float ratx = (tex.width / width);
		float raty = 0f;
		if (height > 0)
			raty = (tex.height / height);
		else
			raty = ratx;
		mTextureRenderer.beginOrthoRendering((int) (getScreenWidth() * ratx), (int) (getScreenHeight() * raty));
		mTextureRenderer.drawOrthoRect((int) (x * ratx), (int) (y * raty), tex.texx, mTextureRenderer.getHeight() - tex.height, tex.width, tex.height);
		mTextureRenderer.endOrthoRendering();
	}

	public static void setAlign(TextAlign ta) {
		align = ta;
	}

	public static void setLineWidth(float w) {
		gl.glLineWidth(w);
	}

	public static void setColor(int color) {
		setColor((float) ((color >> 24) & 0xFF) / 255, (float) ((color >> 16) & 0xFF) / 255, (float) ((color >> 8) & 0xFF) / 255, (float) ((color >> 0) & 0xFF) / 255);
	}

	public static void setColor(float r, float g, float b) {
		gl.glColor3f(r, g, b);
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = 1f;
	}

	public static void setColor(float r, float g, float b, float a) {
		gl.glColor4f(r, g, b, a);
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}

	public static void setColor(int ir, int ig, int ib) {
		setColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f);
	}

	public static void setColor(int ir, int ig, int ib, int ia) {
		setColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f, (float) ia / 255f);
	}

	public static void setTextColor(int color) {
		fontRenderer.textRenderer.setColor((color >> 24) & 0xFF / 255, (color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF);
	}

	public static void setTextColor(float r, float g, float b) {
		fontRenderer.textRenderer.setColor(r, g, b, 1.0f);
	}

	public static void setTextColor(float r, float g, float b, float a) {
		fontRenderer.textRenderer.setColor(r, g, b, a);
	}

	public static void setTextColor(int ir, int ig, int ib) {
		fontRenderer.textRenderer.setColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f, 1.0f);
	}

	public static void setTextColor(int ir, int ig, int ib, int ia) {
		fontRenderer.textRenderer.setColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f, (float) ia / 255f);
	}

	public static void enableStringBackground() {
		textBackground = true;
		setColor(theme.stringBackgroundColor);
	}

	public static void disableStringBackground() {
		textBackground = false;
		setColor(0);
	}

	public static boolean isNoColor() {
		return (color[0] == 0 && color[1] == 0 && color[2] == 0 && color[3] == 0);
	}

	public static void drawRectangle(float x1, float y1, float x2, float y2) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(x2, y1);
		gl.glVertex2d(x1, y1);
		gl.glVertex2d(x1, y2);
		gl.glVertex2d(x2, y2);
		gl.glEnd();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

	public static void fillRectangle(float x1, float y1, float x2, float y2) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glEnable(GL2.GL_BLEND); // Enable blending.
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(x2, y1);
		gl.glVertex2d(x1, y1);
		gl.glVertex2d(x1, y2);
		gl.glVertex2d(x2, y2);
		gl.glEnd();
	}

	public static void drawRectanglew(float x, float y, float w, float h) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(x + w, y);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x, y + h);
		gl.glVertex2d(x + w, y + h);
		gl.glEnd();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

	public static void fillRectanglew(float x, float y, float w, float h) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glEnable(GL2.GL_BLEND); // Enable blending.
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(x + w, y);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x, y + h);
		gl.glVertex2d(x + w, y + h);
		gl.glEnd();
	}

	public static void drawLine(int x1, int y1, int x2, int y2) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
	}

	public static void drawLinew(int x, int y, int w, int h) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + w, y + h);
		gl.glEnd();
	}

	public static void drawString(int x, int y, String str) {
		if (textBackground)
			setColor(theme.stringBackgroundColor);
		if (str.length() < 1)
			return;
		float txtw = fontRenderer.getStringWidth(str);
		float txth = fontRenderer.getStringHeight(str);
		int xoffset = 0;
		switch (align) {
		case LEFT:
			xoffset = 0;
			break;
		case CENTER:
			xoffset = (int) (txtw) / 2;
			break;
		case RIGHT:
			xoffset = (int) txtw;
			break;
		}

		fillRectangle(x - xoffset - theme.stringBackgroundPadding[3 % theme.stringBackgroundPadding.length], y - theme.stringBackgroundPadding[0 % theme.stringBackgroundPadding.length] - fontRenderer.getStringMinDescend(str), txtw + x - xoffset + theme.stringBackgroundPadding[1 % theme.stringBackgroundPadding.length], y + txth - theme.stringBackgroundPadding[2 % theme.stringBackgroundPadding.length]);
		fontRenderer.textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		fontRenderer.textRenderer.draw(str, x - xoffset, y);
		fontRenderer.textRenderer.endRendering();
	}

	public static void drawRectangleAroundString(String str, int x, int y) {
		float txtw = DrawUtils.fontRenderer.getStringWidth(str);
		float txth = DrawUtils.fontRenderer.getStringHeight(str);

		int xoffset = 0;
		switch (DrawUtils.align) {
		case LEFT:
			xoffset = 0;
			break;
		case CENTER:
			xoffset = (int) (txtw) / 2;
			break;
		case RIGHT:
			xoffset = (int) txtw;
			break;
		}

		drawRectangle(x - xoffset - theme.stringBackgroundPadding[3 % theme.stringBackgroundPadding.length], y - theme.stringBackgroundPadding[0 % theme.stringBackgroundPadding.length] - fontRenderer.getStringMinDescend(str), txtw + x - xoffset + theme.stringBackgroundPadding[1 % theme.stringBackgroundPadding.length], y + txth - theme.stringBackgroundPadding[2 % theme.stringBackgroundPadding.length]);
	}

	public static void draw3DString(String str, float x, float y, float z, float rotation, float scale) {
		fontRenderer.textRenderer.begin3DRendering();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glRotatef(rotation, 0f, 1f, 0f);
		fontRenderer.textRenderer.draw3D("Hello, world!", x, y, z, scale);
		fontRenderer.textRenderer.end3DRendering();
		gl.glPopMatrix();
	}

	public static int getScreenWidth() {
		return drawable.getSurfaceWidth();
	}

	public static int getScreenHeight() {
		return drawable.getSurfaceHeight();
	}

	/*
	 * 6----7 /| /| 3----2 | | 5--|-4 |/ |/ 0----1
	 */
	// float x1[], float x2[], float[] x3, float[] x4, float[] x5, float[] x6,
	// float x7, float[] x8

	public static void drawCube() {
		gl.glPushMatrix();
		gl.glTranslatef(10f, 0f, 0f);
		gl.glScalef(1f, 2f, 1f);
		// gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f); // Rotate The Cube On X, Y & Z
		gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
		DrawUtils.setColor(1f, 0f, 0f, 0.5f); // red color
		gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
		gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
		gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
		gl.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)
		DrawUtils.setColor(0f, 1f, 0f, 0.5f); // green color
		gl.glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad
		gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad
		gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad
		gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad
		DrawUtils.setColor(0f, 0f, 1f, 0.5f); // blue color
		gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
		gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
		gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad
		gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad
		DrawUtils.setColor(1f, 1f, 0f, 0.5f); // yellow color
		gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad
		gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad
		gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
		gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)
		gl.glColor3f(1f, 0f, 1f); // purple (red + green)
		DrawUtils.setColor(1f, 0f, 1f, 0.5f); // purple color
		gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
		gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
		gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad
		gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad
		DrawUtils.setColor(0f, 1f, 1f, 0.5f); // cyan color
		gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
		gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad
		gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad
		gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad
		gl.glEnd(); // Done Drawing The Quad
		gl.glPopMatrix();
	}

}
