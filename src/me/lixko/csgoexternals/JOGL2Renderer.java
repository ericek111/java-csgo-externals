package me.lixko.csgoexternals;

import java.awt.Font;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

import me.lixko.csgoexternals.util.DrawUtils;


public class JOGL2Renderer implements GLEventListener {

	TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 12));;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		if (!Client.theClient.isRunning)
			return;

		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, drawable.getSurfaceWidth(), 0, drawable.getSurfaceHeight(), -1, 1); // left,right,bottom,top,front,back
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		DrawUtils.textRenderer.setSmoothing(true);

		// DrawUtils.setColor(0.1f, 0.2f, 0.3f, 0.8f);
		// DrawUtils.setColor(0x003030AA);
		// DrawUtils.drawFilledRectangle(0, 0, 100, 100);

		Client.theClient.eventHandler.onUIRender();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		Client.theClient.isRunning = false;

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		DrawUtils.gl = drawable.getGL().getGL2();
		DrawUtils.drawable = drawable;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

	}

}