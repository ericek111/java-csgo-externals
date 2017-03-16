package me.lixko.csgoexternals.util;

import java.awt.geom.Rectangle2D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

import me.lixko.csgoexternals.themes.BasicTheme;

public class DrawUtils {

	public static GL2 gl;

	public static GLAutoDrawable drawable;
	public static BasicTheme theme = new BasicTheme();
	public static TextRenderer textRenderer = theme.textRenderer; //theme.textRenderer;

	public static void setColor(int color) {
		gl.glColor4f((float) ((color >> 24) & 0xFF) / 255, (float) ((color >> 16) & 0xFF) / 255, (float) ((color >> 8) & 0xFF) / 255, (float) ((color >> 0) & 0xFF) / 255);
	}

	public static void setColor(float r, float g, float b) {
		gl.glColor3f(r, g, b);
	}

	public static void setColor(float r, float g, float b, float a) {
		gl.glColor4f(r, g, b, a);
	}
	
	public static void setColor(int ir, int ig, int ib) {
		gl.glColor3f((float)ir / 255f, (float)ig / 255f, (float)ib / 255f);
	}

	public static void setColor(int ir, int ig, int ib, int ia) {
		gl.glColor4f( (float)ir / 255f, (float)ig / 255f, (float)ib / 255f, (float)ia / 255f);
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
		gl.glVertex2d(x+w, y);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x, y+h);
		gl.glVertex2d(x+w, y+h);
		gl.glEnd();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

	public static void fillRectanglew(float x, float y, float w, float h) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glEnable(GL2.GL_BLEND); // Enable blending.
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(x+w, y);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x, y+h);
		gl.glVertex2d(x+w, y+h);
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
	    gl.glVertex2f(x+w, y+h);
	    gl.glEnd();
	}
	
	public static void drawString(int x, int y, String str) {
		textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		textRenderer.draw(str, x, y);
		textRenderer.endRendering();
	}

	public static void drawString(int x, int y, String str, int color) {
		textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		textRenderer.setColor((color >> 24) & 0xFF / 255, (color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF);
		textRenderer.draw(str, x, y);
		textRenderer.endRendering();
	}

	public static void drawStringWithBackground(int x, int y, String str, int textcolor, int bgcolor) {
		Rectangle2D txtbounds = textRenderer.getBounds(str);
		setColor(bgcolor);
		fillRectangle((float) txtbounds.getMinX() + x - theme.stringBackgroundPadding[3 % theme.stringBackgroundPadding.length], y - (float) txtbounds.getMinY() + theme.stringBackgroundPadding[0 % theme.stringBackgroundPadding.length], (float) txtbounds.getMaxX() + x + theme.stringBackgroundPadding[1 % theme.stringBackgroundPadding.length], y - (float) txtbounds.getMaxY() - theme.stringBackgroundPadding[2 % theme.stringBackgroundPadding.length]);
		txtbounds = null;
		textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		if (textcolor > -1)
			textRenderer.setColor((textcolor >> 24) & 0xFF / 255, (textcolor >> 16) & 0xFF, (textcolor >> 8) & 0xFF, (textcolor >> 0) & 0xFF);
		textRenderer.draw(str, x, y);
		textRenderer.endRendering();
	}

	public static void drawStringWithBackground(int x, int y, String str, int textcolor) {
		drawStringWithBackground(x, y, str, textcolor, theme.stringBackgroundColor);
	}

	public static void drawStringWithBackground(int x, int y, String str) {
		drawStringWithBackground(x, y, str, -1, theme.stringBackgroundColor);
	}
	

	public static void drawCenteredString(int x, int y, String str) {
		Rectangle2D txtbounds = textRenderer.getBounds(str);
		textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		textRenderer.draw(str, x - (int)(txtbounds.getMinX() + txtbounds.getMaxX()) / 2, y);
		textRenderer.endRendering();
		txtbounds = null;
	}

	public static void drawCenteredString(int x, int y, String str, int color) {
		Rectangle2D txtbounds = textRenderer.getBounds(str);
		textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		textRenderer.setColor((color >> 24) & 0xFF / 255, (color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF);
		textRenderer.draw(str, x - (int)(txtbounds.getMinX() + txtbounds.getMaxX()) / 2, y);
		textRenderer.endRendering();
		txtbounds = null;
	}
	public static void drawCenteredStringWithBackground(int x, int y, String str, int textcolor, int bgcolor) {
		Rectangle2D txtbounds = textRenderer.getBounds(str);
		int xoffset = (int)(txtbounds.getMinX() + txtbounds.getMaxX()) / 2;
		setColor(bgcolor);
		fillRectangle((float) txtbounds.getMinX() + x - xoffset - theme.stringBackgroundPadding[3 % theme.stringBackgroundPadding.length], y - (float) txtbounds.getMinY() + theme.stringBackgroundPadding[0 % theme.stringBackgroundPadding.length], (float) txtbounds.getMaxX() + x - xoffset + theme.stringBackgroundPadding[1 % theme.stringBackgroundPadding.length], y - (float) txtbounds.getMaxY() - theme.stringBackgroundPadding[2 % theme.stringBackgroundPadding.length]);
		txtbounds = null;
		textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		if (textcolor > -1)
			textRenderer.setColor((textcolor >> 24) & 0xFF / 255, (textcolor >> 16) & 0xFF, (textcolor >> 8) & 0xFF, (textcolor >> 0) & 0xFF);
		textRenderer.draw(str, x - xoffset, y);
		textRenderer.endRendering();
	}

	public static void drawCenteredStringWithBackground(int x, int y, String str, int textcolor) {
		drawCenteredStringWithBackground(x, y, str, textcolor, theme.stringBackgroundColor);
	}

	public static void drawCenteredStringWithBackground(int x, int y, String str) {
		drawCenteredStringWithBackground(x, y, str, -1, theme.stringBackgroundColor);
	}
	
	public static int getWidth() {
		return drawable.getSurfaceWidth();
	}
	
	public static int getHeight() {
		return drawable.getSurfaceHeight();
	}

}
