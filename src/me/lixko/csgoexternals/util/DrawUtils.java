package me.lixko.csgoexternals.util;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.awt.TextureRenderer;

import me.lixko.csgoexternals.offsets.ItemDefinitionIndex;
import me.lixko.csgoexternals.themes.BasicTheme;

@SuppressWarnings("static-access")
public class DrawUtils {

	public static GL2 gl;

	public static GLAutoDrawable drawable;
	public static BasicTheme theme = new BasicTheme();
	public static FontRenderer fontRenderer;
	public static TextAlign align = TextAlign.LEFT;
	public static LocalPlayerPosition lppos = new LocalPlayerPosition();

	/*
	 * public static TextureRenderer mTextureRenderer;
	 * private static Graphics2D tTextureGraphics2D;
	 */
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	public static HashMap<String, FontRenderer> fontRenderers = new HashMap<String, FontRenderer>();

	private static boolean textBackground = true;
	private static boolean textBackgroundDefaultColor = true;
	private static float[] color = new float[4];
	private static float[] texcolor = new float[4];
	private static float[] textcolor = new float[4];
	private static int textsize = 18;
	private static int textstyle = Font.PLAIN;
	private static String fontname = theme.normalFontName;

	static {
		String texturespath = FileUtil.mainpath + "/textures/";
		for (File texfile : textureSourceFile("ranks").listFiles()) {
			addTexture(texfile.getName().substring(0, texfile.getName().lastIndexOf(".")), texfile);
		}
		for (File texfile : textureSourceFile("weapons").listFiles()) {
			String name = texfile.getName().substring(0, texfile.getName().lastIndexOf("."));
			if (name.startsWith("weapon_"))
				addTexture("weapon_" + Enum.valueOf(ItemDefinitionIndex.class, name.toUpperCase()).id(), texfile);
			else
				addTexture(name, texfile);
		}
		textureSourceFile("weapons_outline");
		for (File texfile : textureSourceFile("weapons_outline").listFiles()) {
			String name = texfile.getName().substring(0, texfile.getName().lastIndexOf("."));
			if (name.startsWith("weapon_"))
				addTexture("weaponout_" + Enum.valueOf(ItemDefinitionIndex.class, name.toUpperCase()).id(), texfile);
			else
				addTexture("out_" + name, texfile);
		}
		addTexture("defuser", textureSourceFile("defuser.png"));
		addTexture("bomb", textureSourceFile("bomb.png"));
		loadTextures();
	}

	public static File textureSourceFile(String directory) {
		File folder = null;
		String path = FileUtil.mainpath + "/textures/" + directory;
		try {
			folder = new File(path);
		} catch (Exception ex) {
			System.out.println("Uknown error occured while accessing a texture file or folder: " + path);
			ex.printStackTrace();
			System.exit(1);
		}
		if (folder == null || !folder.exists()) {
			System.out.println("ERROR! Textures folder doesn't exist! Verify its location: " + path);
			System.exit(1);
		}
		if (folder.isDirectory() && folder.listFiles() == null) {
			System.out.println("ERROR! Cannot list a texture folder! Verify its location: " + path);
			System.exit(1);
		}
		return folder;
	}

	public static void addTexture(String key, File imgfile) {
		try {
			textures.put(key, new Texture(imgfile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadTextures() {
		/*
		 * int totalwidth = 0;
		 * int maxheight = 0;
		 * for (Map.Entry<String, Texture> entry : textures.entrySet()) {
		 * Texture tex = entry.getValue();
		 * maxheight = Math.max(maxheight, tex.height);
		 * tex.texx = totalwidth;
		 * totalwidth += tex.width;
		 * }
		 * System.out.println("Allocating texture buffer " + totalwidth + "x" + maxheight + "px.");
		 * System.out.println("Maximum texture size: " + GL.GL_MAX_TEXTURE_SIZE);
		 * mTextureRenderer = new TextureRenderer(totalwidth, maxheight, true);
		 * tTextureGraphics2D = mTextureRenderer.createGraphics();
		 * for (Map.Entry<String, Texture> entry : textures.entrySet()) {
		 * Texture tex = entry.getValue();
		 * tTextureGraphics2D.drawImage(tex.img, tex.texx, 0, null);
		 * }
		 * tTextureGraphics2D.dispose();
		 * mTextureRenderer.markDirty(0, 0, totalwidth, maxheight);
		 */
	}

	public static void drawTexture(String key, int x, int y) {
		if (!textures.containsKey(key))
			return;
		Texture tex = textures.get(key);
		if (tex == null)
			return;

		tex.mTextureRenderer.setColor(texcolor[0], texcolor[1], texcolor[2], texcolor[3]);
		tex.mTextureRenderer.beginOrthoRendering(getScreenWidth() * 1, getScreenHeight() * 1);
		tex.mTextureRenderer.drawOrthoRect(x, y, tex.texx, tex.mTextureRenderer.getHeight() - tex.height, tex.width, tex.height);
		tex.mTextureRenderer.endOrthoRendering();
	}

	public static void drawTexture(String key, int x, int y, int width, int height) {
		if (!textures.containsKey(key))
			return;
		Texture tex = textures.get(key);
		if (tex == null)
			return;

		float ratx = 1f;
		float raty = 1f;
		if (width < 0 && height < 0) {
			ratx = (tex.width / width);
			if (height > 0)
				raty = (tex.height / height);
			else
				raty = ratx;

		} else if (width > 0) {
			ratx = (tex.width / width);
			if (height > 0)
				raty = (tex.height / height);
			else
				raty = ratx;
		} else {
			if (height > 0) {
				raty = (tex.height / height);
				ratx = raty;
			} else
				ratx = 1f;
		}

		int xoffset = 0;
		switch (DrawUtils.align) {
		case LEFT:
			xoffset = 0;
			break;
		case CENTER:
			xoffset = (int) ((float) tex.width / ratx / 2);
			break;
		case RIGHT:
			xoffset = (int) ((float) tex.width / ratx);
			break;
		}

		tex.mTextureRenderer.setColor(texcolor[0], texcolor[1], texcolor[2], texcolor[3]);
		tex.mTextureRenderer.beginOrthoRendering((int) (getScreenWidth() * ratx), (int) (getScreenHeight() * raty));
		tex.mTextureRenderer.drawOrthoRect((int) ((x - xoffset) * ratx), (int) (y * raty), tex.texx, tex.mTextureRenderer.getHeight() - tex.height, tex.width, tex.height);
		tex.mTextureRenderer.endOrthoRendering();
	}

	public static FontRenderer getFont(String name, int style, int size) {
		if (fontRenderers.containsKey(name + "/" + style + "/" + size)) {
			return fontRenderer = fontRenderers.get(name + "/" + style + "/" + size);
		} else {
			FontRenderer newfr = new FontRenderer(new TextRenderer(new Font(name, style, size)));
			fontRenderers.put(name + "/" + style + "/" + size, newfr);
			System.out.println("Loading new Font: " + name + "/" + style + "/" + size);
			return fontRenderer = newfr;
		}
	}

	public static void setStyle(ChatColor... styles) {
		for (ChatColor e : styles) {
			if (e.color[0] != -1)
				DrawUtils.setTextColor(e.color);
			else if (e == ChatColor.PLAIN)
				textstyle = Font.PLAIN;
			else if (e == ChatColor.BOLD)
				textstyle |= Font.BOLD;
			else if (e == ChatColor.ITALIC)
				textstyle |= Font.ITALIC;
			else if (e == ChatColor.SMALL) {
				textsize = theme.smallFontSize;
				fontname = theme.smallFontName;
			} else if (e == ChatColor.MEDIUM) {
				textsize = theme.normalFontSize;
				fontname = theme.normalFontName;
			} else if (e == ChatColor.LARGE) {
				textsize = theme.largeFontSize;
				fontname = theme.largeFontName;
			}
		}
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

	public static void setColorAlpha(float a) {
		gl.glColor4f(color[0], color[1], color[2], a);
		color[3] = a;
	}

	public static void setColor(int ir, int ig, int ib) {
		setColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f);
	}

	public static void setColor(int ir, int ig, int ib, int ia) {
		setColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f, (float) ia / 255f);
	}

	public static void setTextureColor(int color) {
		setTextureColor((float) ((color >> 24) & 0xFF) / 255, (float) ((color >> 16) & 0xFF) / 255, (float) ((color >> 8) & 0xFF) / 255, (float) ((color >> 0) & 0xFF) / 255);
	}

	public static void setTextureColor(float r, float g, float b) {
		texcolor[0] = r;
		texcolor[1] = g;
		texcolor[2] = b;
		texcolor[3] = 1f;
	}

	public static void setTextureColor(float r, float g, float b, float a) {
		texcolor[0] = r;
		texcolor[1] = g;
		texcolor[2] = b;
		texcolor[3] = a;
	}

	public static void setTextureAlpha(float a) {
		texcolor[3] = a;
	}

	public static void setTextColor(int ir, int ig, int ib) {
		setTextColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f);
	}

	public static void setTextColor(int ir, int ig, int ib, int ia) {
		setTextColor((float) ir / 255f, (float) ig / 255f, (float) ib / 255f, (float) ia / 255f);
	}

	public static void setTextColor(int color) {
		setTextColor((float) ((color >> 24) & 0xFF) / 255f, (float) ((color >> 16) & 0xFF) / 255f, (float) ((color >> 8) & 0xFF) / 255f, (float) ((color >> 0) & 0xFF) / 255f);
	}

	public static void setTextColor(float r, float g, float b) {
		textcolor[0] = r;
		textcolor[1] = g;
		textcolor[2] = b;
		textcolor[3] = 1f;
	}

	public static void setTextColor(float r, float g, float b, float a) {
		textcolor[0] = r;
		textcolor[1] = g;
		textcolor[2] = b;
		textcolor[3] = a;
	}

	public static void setTextColor(float[] color) {
		textcolor = color;
	}

	public static void setTextAlpha(float a) {
		textcolor[3] = a;
	}

	public static float getDefaultTextBGAlpha() {
		return (float) ((theme.stringBackgroundColor >> 0) & 0xFF) / 255;
	}

	// TODO: Rework
	public static void setTextBGColorToDefault(float a) {
		setColor((float) ((theme.stringBackgroundColor >> 24) & 0xFF) / 255, (float) ((theme.stringBackgroundColor >> 16) & 0xFF) / 255, (float) ((theme.stringBackgroundColor >> 8) & 0xFF) / 255, a);
	}

	public static void enableStringBackground() {
		textBackground = true;
		setColor(theme.stringBackgroundColor);
	}

	public static void disableStringBackground() {
		textBackground = false;
		setColor(0);
	}

	public static void enableTextBackgroundColor() {
		textBackgroundDefaultColor = true;
	}

	public static void disableTextBackgroundColor() {
		textBackgroundDefaultColor = false;
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
		if (str.length() < 1)
			return;
		if (fontRenderer == null)
			fontRenderer = getFont(fontname, textstyle, textsize);

		float txtw = 0, txth = 0;
		String[] strparts = str.split(ChatColor.colorChar + "");
		String todraw = "";
		int xoffset = 0;

		if (textBackground || align != TextAlign.LEFT) {
			for (int i = 0; i < strparts.length; i++) {
				if (strparts[i].length() == 0)
					continue;
				if (i == 0 && str.charAt(0) != ChatColor.colorChar) {
					todraw = strparts[i].substring(0);
				} else {
					ChatColor foundenum = ChatColor.getChatColor(strparts[i].charAt(0));
					if (foundenum == null) {
						todraw = strparts[i];
					} else {
						todraw = strparts[i].substring(1);
						setStyle(foundenum);
					}
				}

				getFont(fontname, textstyle, textsize);
				txtw += fontRenderer.getStringWidth(todraw);
				txth = Math.max(fontRenderer.getStringHeight(todraw), txth);
			}

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
			todraw = "";

			if (textBackgroundDefaultColor)
				setColor(theme.stringBackgroundColor);
			fillRectangle(x - xoffset - theme.stringBackgroundPadding[3 % theme.stringBackgroundPadding.length], y - theme.stringBackgroundPadding[0 % theme.stringBackgroundPadding.length] - fontRenderer.getStringMinDescend(str), txtw + x - xoffset + theme.stringBackgroundPadding[1 % theme.stringBackgroundPadding.length], y + txth - theme.stringBackgroundPadding[2 % theme.stringBackgroundPadding.length]);

		}

		for (int i = 0; i < strparts.length; i++) {
			if (strparts[i].length() == 0)
				continue;
			if (i == 0 && str.charAt(0) != ChatColor.colorChar) {
				todraw = strparts[i].substring(0);
			} else {
				ChatColor foundenum = ChatColor.getChatColor(strparts[i].charAt(0));
				if (foundenum == null) {
					todraw = strparts[i];
				} else {
					todraw = strparts[i].substring(1);
					setStyle(foundenum);
				}
			}

			getFont(fontname, textstyle, textsize);
			fontRenderer.textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
			fontRenderer.textRenderer.setColor(textcolor[0], textcolor[1], textcolor[2], textcolor[3]);
			fontRenderer.textRenderer.draw(todraw, x - xoffset, y);
			fontRenderer.textRenderer.endRendering();
			x += fontRenderer.getStringWidth(todraw);
		}

		if (textsize == theme.smallFontSize)
			setStyle(theme.smallFontStyle);
		else if (textsize == theme.normalFontSize)
			setStyle(theme.normalFontStyle);
		else if (textsize == theme.largeFontSize)
			setStyle(theme.largeFontStyle);
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
