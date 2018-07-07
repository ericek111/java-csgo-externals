package me.lixko.csgoexternals.modules;

import java.nio.FloatBuffer;

import com.github.jonatino.misc.MemoryBuffer;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.BoneList;
import me.lixko.csgoexternals.structs.Matrix3x4Mem;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.GLGraph;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.PID;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;
import me.lixko.csgoexternals.util.XKeySym;

public class AimBotGhetto extends Module {

	Module thismod = this;
	AimBotMode mode = AimBotMode.CROSSHAIR;
	AimBotTarget target = AimBotTarget.ENEMY;
	
	VectorMem vecpos = new VectorMem();
	Matrix3x4Mem matrixstr = new Matrix3x4Mem();
	MemoryBuffer vectorbuf = new MemoryBuffer(vecpos.size());
	MemoryBuffer vectorbuf2 = new MemoryBuffer(vecpos.size());
	MemoryBuffer vectorbuf3 = new MemoryBuffer(vecpos.size());
	MemoryBuffer vectorbuf4 = new MemoryBuffer(vecpos.size());
	MemoryBuffer matrixbuf = new MemoryBuffer(matrixstr.size());
	MemoryBuffer entityBones = new MemoryBuffer(matrixstr.size());
	
	PID pidX = new PID(3.0f, 7f, 0f);
	PID pidY = new PID(3.7f, 24f, 0.11f);
	
	// 3 / 0 / 0.5
	GLGraph xAngleGraph, xErrGraph, yAngleGraph, yErrGraph, xOutGraph, yOutGraph;

	public static float[] GetBonePosition(long entityptr, int bone) {
		// https://www.unknowncheats.me/forum/1779179-post3.html
		Matrix3x4Mem matrixstr = new Matrix3x4Mem();
		MemoryBuffer entityBonesBfr = new MemoryBuffer(matrixstr.size());
		matrixstr.setSource(entityBonesBfr);
		long boneMatrixPtr = Engine.clientModule().readLong(entityptr + Offsets.m_dwBoneMatrix);
		Engine.clientModule().read(boneMatrixPtr + Offsets.m_dwBoneDistance * bone, entityBonesBfr);
		float[] x = new float[3];
		x[0] = matrixstr.f03.getFloat();
		x[1] = matrixstr.f13.getFloat();
		x[2] = matrixstr.f23.getFloat();
		return x;
	}
	
	@Override
	public void onEngineLoaded() {
		vecpos.setSource(vectorbuf);
		xAngleGraph = new GLGraph(DrawUtils.gl, 50f, 450f, 300f, 300f, 1f, 400);
		yAngleGraph = new GLGraph(DrawUtils.gl, 50f, 250f, 300f, 300f, 0.5f, 400);
		xErrGraph = new GLGraph(DrawUtils.gl, 50f, 450f, 300f, 300f, 1f, 400);
		yErrGraph = new GLGraph(DrawUtils.gl, 50f, 250f, 300f, 300f, 0.5f, 400);
		xOutGraph = new GLGraph(DrawUtils.gl, 50f, 450f, 300f, 300f, 1f, 400);
		yOutGraph = new GLGraph(DrawUtils.gl, 50f, 250f, 300f, 300f, 0.5f, 400);
		
		xAngleGraph.setColor(0f, 1f, 0f, 1f);
		yAngleGraph.setColor(0f, 1f, 0f, 1f);
		xErrGraph.setColor(1f, 0f, 0f, 1f);
		yErrGraph.setColor(1f, 0f, 0f, 1f);
		xOutGraph.setColor(0f, 1f, 1f, 1f);
		yOutGraph.setColor(0f, 1f, 1f, 1f);
		
		xOutGraph.setOscillationFinder(true);
		//yOutGraph.setOscillationFinder(true);
		
		xOutGraph.setOscFinderThreshold(0.2f);
		yOutGraph.setOscFinderThreshold(0.2f);
		
		for(int i = 0; i < xErrGraph.maxSamples(); i++) {
			
			//xErrGraph.putSample((float) Math.sin(((float) i / 100f) * Math.PI) * 100f);
			//xErrGraph.putSample((float)(i * 10));
		}
		
		/*for(int i = 0; i < color_data.capacity() / 3; i++) {
			color_data.put(new float[] { 1f, 0f, 0f });
		}*/
		
		/*vboIndices = temp[1];
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vboIndices);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.capacity() * BufferUtil.SIZEOF_SHORT,
                            indices, GL.GL_STATIC_DRAW);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);*/
	}
	
	@Override
	public void onUIRender() {
		DrawUtils.setColor(0f, 0f, 0f, 0.5f);
		DrawUtils.fillRectanglew(30f, 50f, 340f, 600f);
		xAngleGraph.render();
		yAngleGraph.render();
		xOutGraph.render();
		yOutGraph.render();
		xErrGraph.render();
		yErrGraph.render();
		
		DrawUtils.setAlign(TextAlign.RIGHT);
		DrawUtils.setStyle(ChatColor.LARGE);
		
		DrawUtils.setTextColor(0xffaaa5FF);
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 210, DrawUtils.getScreenHeight() - 25, "P");
		DrawUtils.setTextColor(0xffd3b6FF);
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 140, DrawUtils.getScreenHeight() - 25, "I");
		DrawUtils.setTextColor(0xfdffabFF);
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 70, DrawUtils.getScreenHeight() - 25, "D");
		DrawUtils.setTextColor(0xFFFFFFFF);
		
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 300, DrawUtils.getScreenHeight() - 50, "" + (Math.round(1f / xOutGraph.getLastOscillationPeriod() * 100f) / 100f));
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 260, DrawUtils.getScreenHeight() - 50, "X:");
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 300, DrawUtils.getScreenHeight() - 80, "" + (Math.round(1f / yOutGraph.getLastOscillationPeriod() * 100f) / 100f));
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 260, DrawUtils.getScreenHeight() - 80, "Y:");
		DrawUtils.setTextColor(0xFF0000FF);
		
		DrawUtils.setTextColor(0xffaaa5FF);
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 200, DrawUtils.getScreenHeight() - 50, "" + (Math.round(pidX.getP() * 100f) / 100f));
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 200, DrawUtils.getScreenHeight() - 80, "" + (Math.round(pidY.getP() * 100f) / 100f));
		DrawUtils.setTextColor(0xffd3b6FF);
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 130, DrawUtils.getScreenHeight() - 50, "" + (Math.round(pidX.getI() * 100f) / 100f));
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 130, DrawUtils.getScreenHeight() - 80, "" + (Math.round(pidY.getI() * 100f) / 100f));
		DrawUtils.setTextColor(0xfdffabFF);
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 60, DrawUtils.getScreenHeight() - 50, "" + (Math.round(pidX.getD() * 100f) / 100f));
		DrawUtils.drawString(DrawUtils.getScreenWidth() - 60, DrawUtils.getScreenHeight() - 80, "" + (Math.round(pidY.getD() * 100f) / 100f));
	}
	
	@Override
	public void onLoop() {
		//xErrGraph.putSample((float) Math.sin(((float) (loopi % 500) / 100f) * Math.PI) * 100f);
		//xErrGraph.putSample(loopi % 100);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_vecOrigin, vectorbuf);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecViewOffset, vectorbuf2);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_angRotation, vectorbuf3);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + Offsets.m_aimPunchAngle, vectorbuf4);
		float[] posOffset = MathUtils.add(vectorbuf.getFloatArray(0, 3), vectorbuf2.getFloatArray(0, 3));
		float[] viewAngle = vectorbuf3.getFloatArray(0, 3);
		float[] punchAngle = vectorbuf4.getFloatArray(0, 3);
		
		xAngleGraph.putSample(viewAngle[0]);
		yAngleGraph.putSample(viewAngle[1]);	
		
		if (!Client.theClient.keyboardHandler.isPressed(XKeySym.XK_F))
			return;
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		float bestval = Float.MAX_VALUE;
		float[] bestenthitbox = null;
		int localteam = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_iTeamNum);

		for (int i = 1; i < 64; i++) {
			long entityptr = MemoryUtils.getEntity(i);
			if (entityptr == 0)
				continue;
			if (entityptr == Offsets.m_dwLocalPlayer)
				continue;
			int team = Engine.clientModule().readInt(entityptr + Netvars.CBaseEntity.m_iTeamNum);
			if (team != 2 && team != 3)
				continue;
			if (target == AimBotTarget.ENEMY && team == localteam)
				continue;
			if (target == AimBotTarget.TEAM && team != localteam)
				continue;
			int health = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iHealth);
			if (health < 1)
				continue;

			float[] hitbox = GetBonePosition(entityptr, BoneList.BONE_HEAD.index);
			// System.out.println(StringFormat.dump(hitbox));
			if (mode == AimBotMode.NEAREST) {
				float distance = MathUtils.VecDist(posOffset, hitbox);
				if (distance < bestval) {
					bestval = distance;
					bestenthitbox = hitbox;
				} else
					continue;
			} else if (mode == AimBotMode.CROSSHAIR) {
				float fov = MathUtils.GetFov(viewAngle, posOffset, hitbox);
				if (fov < bestval) {
					bestval = fov;
					bestenthitbox = hitbox;
				} else
					continue;
			}

			/*
			 * if(diffAngles[1] > 0) {
			 * Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), 1, 0, new NativeLong(1));
			 * } else {
			 * Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), -1, 0, new NativeLong(1));
			 * }
			 * 
			 * if(diffAngles[0] > 0) {
			 * Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), 0, -1, new NativeLong(1));
			 * } else {
			 * Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), 0, 1, new NativeLong(1));
			 * }
			 */

			/*
			 * Engine.clientModule().read(entityptr + Offsets.m_vecOrigin, vectorbuf);
			 * for(int x = BoneList.BONE_PELVIS.index; x < BoneList.BONE_MAX.index; x++) {
			 * long boneMatrixPtr = Engine.clientModule().readLong(entityptr + Offsets.m_dwBoneDistance * x);
			 * Engine.clientModule().read(boneMatrixPtr, entityBones);
			 * // entityBones[i] = memoryManager->read<Matrix3x4>(GetBoneMatrixPointer() + (offsetManager->client.m_dwBoneDistance * i));
			 * }
			 */
		}

		if (mode == AimBotMode.NEAREST) {

		} else if (mode == AimBotMode.CROSSHAIR) {
		}
		if (bestenthitbox == null)
			return;

		float[] dir = MathUtils.subtract(posOffset, bestenthitbox);
		float[] normalizedDir = MathUtils.normalizeVector(dir);
		float[] anglesDir = MathUtils.VectorAngles(normalizedDir);
		float[] clampedDir = MathUtils.ClampAngle(anglesDir);
		float[] diffAngles = MathUtils.ClampAngle(MathUtils.add(MathUtils.subtract(viewAngle, clampedDir), punchAngle));
		//System.out.println(StringFormat.dump(diffAngles));
		xErrGraph.putSample(diffAngles[0]);
		yErrGraph.putSample(diffAngles[1]);
		//float outX = pidX.step(diffAngles[0]);
		//float outY = pidX.step(diffAngles[1]);
		
		float outX = MathUtils.clamp(pidX.step(diffAngles[0]), -50f, 50f);
		float outY = MathUtils.clamp(pidY.step(diffAngles[1]), -50f, 50f);
		xOutGraph.putSample(outX);
		yOutGraph.putSample(outY);
		Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), (int) outY, -(int) outX, new NativeLong(1));
		Engine.x11.XFlush(Engine.dpy.get());
		// System.out.println(StringFormat.dump(diffAngles) + " / " + StringFormat.dump(viewAngle) + " " + StringFormat.dump(clampedDir));

		// Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), (int) diffAngles[1], -(int) diffAngles[0], new NativeLong(1));
		// Engine.xtest.XTestFakeMotionEvent(Engine.dpy.get(), 30, 0, 0, new NativeLong(0));

		// System.out.println(x);
	}
	
	@Override
	public void onKeyPress(KeySym sym) {
		if (sym.intValue() == XKeySym.XK_KP_Add) {
			if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_KP_7)) {
				if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L))
					pidY.changePID(0.1f, 0f, 0f);
				else
					pidX.changePID(0.1f, 0f, 0f);
			} 
			if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_KP_8)) {
				if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L))
					pidY.changePID(0f, 1f, 0f);
				else
					pidX.changePID(0f, 1f, 0f);
			} 
			if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_KP_9)) {
				if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L))
					pidY.changePID(0f, 0f, 0.1f);
				else
					pidX.changePID(0f, 0f, 0.1f);
			}
		} else if (sym.intValue() == XKeySym.XK_KP_Subtract) {
			if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_KP_7)) {
				if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L))
					pidY.changePID(-0.1f, 0f, 0f);
				else
					pidX.changePID(-0.1f, 0f, 0f);
			} 
			if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_KP_8)) {
				if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L))
					pidY.changePID(0f, -1f, 0f);
				else
					pidX.changePID(0f, -1f, 0f);
			} 
			if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_KP_9)) {
				if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L))
					pidY.changePID(0f, 0f, -0.1f);
				else
					pidX.changePID(0f, 0f, -0.1f);
			}
		} else if (sym.intValue() == XKeySym.XK_f) {
			xAngleGraph.empty();
			yAngleGraph.empty();
			xOutGraph.empty();
			yOutGraph.empty();
			xErrGraph.empty();
			yErrGraph.empty();
		}
	}

	public enum AimBotMode {
		NEAREST,
		CROSSHAIR
	}

	public enum AimBotTarget {
		TEAM,
		ENEMY,
		BOTH
	}

}
