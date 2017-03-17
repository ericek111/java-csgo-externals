package me.lixko.csgoexternals;

import java.awt.DisplayMode;
import java.awt.Font;

import com.github.jonatino.misc.MemoryBuffer;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.DrawUtils;

public class JOGL2Renderer implements GLEventListener {

	TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 12));;

	public static DisplayMode dm, dm_old;
	private GLU glu = new GLU();
	private VectorMem lpvec = new VectorMem();
	private MemoryBuffer lpvecbuf = new MemoryBuffer(lpvec.size());
	private boolean needsDataUpdate = false;
	private float cx = 0f, cy = 0f, cz = 0f, pitch = 0f, yaw = 0f;

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (!Client.theClient.isRunning) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(1);
					if (!needsDataUpdate)
						continue;

					Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecOrigin, lpvec.size(), lpvecbuf);
					cx = lpvec.x.getFloat();
					cy = lpvec.y.getFloat();
					cz = -lpvec.z.getFloat();

					Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_angRotation, lpvecbuf.size(), lpvecbuf);
					yaw = 90 - lpvec.z.getFloat();
					pitch = lpvec.x.getFloat();
					needsDataUpdate = false;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	@Override
	public void display(GLAutoDrawable drawable) {
		if (!Client.theClient.isRunning)
			return;
		final GL2 gl = drawable.getGL().getGL2();

		init3D(drawable, gl);
		Client.theClient.eventHandler.onWorldRender();
		gl.glFlush();
		init2D(drawable, gl);
		Client.theClient.eventHandler.onUIRender();
		gl.glFlush();

		needsDataUpdate = true;
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		DrawUtils.gl = drawable.getGL().getGL2();
		DrawUtils.drawable = drawable;
		lpvec.setSource(lpvecbuf);
		DrawUtils.textRenderer.setSmoothing(true);
		needsDataUpdate = true;
		updateLoop.start();
	}

	private void init3D(GLAutoDrawable drawable, GL2 gl) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90.0f, (float) drawable.getSurfaceWidth() / (float) drawable.getSurfaceHeight(), 1.0, 8000.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(-cx / 100f, -cy / 100f, -cz / 100f);

		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		// gl.glDepthFunc(GL2.GL_LEQUAL);
		// gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

	}

	private void init2D(GLAutoDrawable drawable, GL2 gl) {
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		gl.glDisable(GL2.GL_CULL_FACE);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_LIGHTING);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, drawable.getSurfaceWidth(), 0, drawable.getSurfaceHeight(), -1, 1); // left,right,bottom,top,front,back

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
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

}