package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;
import com.sun.jna.NativeLong;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.BoneList;
import me.lixko.csgoexternals.structs.Matrix3x4Mem;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.XKeySym;

public class AimBot extends Module {

	Module thismod = this;
	AimBotMode mode = AimBotMode.NEAREST;
	AimBotTarget target = AimBotTarget.BOTH;

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
	public void onLoop() {
		if (true)
			return;
		VectorMem vecpos = new VectorMem();
		Matrix3x4Mem matrixstr = new Matrix3x4Mem();
		MemoryBuffer vectorbuf = new MemoryBuffer(vecpos.size());
		MemoryBuffer vectorbuf2 = new MemoryBuffer(vecpos.size());
		MemoryBuffer vectorbuf3 = new MemoryBuffer(vecpos.size());
		MemoryBuffer vectorbuf4 = new MemoryBuffer(vecpos.size());
		MemoryBuffer matrixbuf = new MemoryBuffer(matrixstr.size());
		MemoryBuffer entityBones = new MemoryBuffer(matrixstr.size());
		vecpos.setSource(vectorbuf);

		if (!Client.theClient.keyboardHandler.isPressed(XKeySym.XK_F))
			return;

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_vecOrigin, vectorbuf);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecViewOffset, vectorbuf2);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_angRotation, vectorbuf3);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + Offsets.m_aimPunchAngle, vectorbuf4);
		float[] posOffset = MathUtils.add(vectorbuf.getFloatArray(0, 3), vectorbuf2.getFloatArray(0, 3));
		float[] viewAngle = vectorbuf3.getFloatArray(0, 2);
		float[] punchAngle = vectorbuf4.getFloatArray(0, 2);

		System.out.println(StringFormat.dump(punchAngle));
		float bestval = Float.MAX_VALUE;
		float[] bestenthitbox = null;
		int localteam = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_iTeamNum);

		for (int i = 1; i < 64; i++) {
			long entityptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + i * Offsets.m_dwEntityLoopDistance);
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
		Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), (int) diffAngles[1], -(int) diffAngles[0], new NativeLong(1));
		Engine.x11.XFlush(Engine.dpy.get());
		// System.out.println(StringFormat.dump(diffAngles) + " / " + StringFormat.dump(viewAngle) + " " + StringFormat.dump(clampedDir));

		// Engine.xtest.XTestFakeRelativeMotionEvent(Engine.dpy.get(), (int) diffAngles[1], -(int) diffAngles[0], new NativeLong(1));
		// Engine.xtest.XTestFakeMotionEvent(Engine.dpy.get(), 30, 0, 0, new NativeLong(0));

		// System.out.println(x);
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
