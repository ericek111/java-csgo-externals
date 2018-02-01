package me.lixko.csgoexternals.util;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.structs.VectorMem;

public class LocalPlayerPosition {
	private VectorMem lpvec = new VectorMem();
	private MemoryBuffer lpvecbuf = new MemoryBuffer(lpvec.size());
	private float pos[] = new float[3], viewoffset[] = new float[3], vieworigin[] = new float[3];
	private float pitch, yaw;
	public int fov, defaultfov;

	public LocalPlayerPosition() {
		lpvec.setSource(lpvecbuf);
	}

	public void updateData() {
		int lpobstarget = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBasePlayer.m_hObserverTarget) & Const.ENT_ENTRY_MASK;
		long lpaddr = 0;
		if(lpobstarget == Const.ENT_ENTRY_MASK) {
			lpaddr = Offsets.m_dwLocalPlayer;
		} else {
			lpaddr = Engine.clientModule().readLong(Offsets.m_dwEntityList + Offsets.m_dwEntityLoopDistance * lpobstarget);
		}
		
		if(true) {
			Engine.clientModule().read(lpaddr + Netvars.CBaseEntity.m_vecOrigin, lpvec.size(), lpvecbuf);
			pos = lpvec.getVector();
			Engine.clientModule().read(lpaddr + Offsets.m_vecViewOffset, lpvec.size(), lpvecbuf);
			viewoffset = lpvec.getVector();
			vieworigin = MathUtils.cadd(pos, viewoffset);

			Engine.clientModule().read(lpaddr + Netvars.CBaseEntity.m_angRotation, lpvecbuf.size(), lpvecbuf);
			yaw = 90 - lpvec.y.getFloat();
			pitch = lpvec.x.getFloat();
		} else {
			Engine.clientModule().read(Offsets.g_vecCurrentRenderOrigin, lpvec.size(), lpvecbuf);
			vieworigin = lpvec.getVector();
			pos = vieworigin;
			
			Engine.clientModule().read(Offsets.g_vecCurrentRenderAngles, lpvecbuf.size(), lpvecbuf);
			yaw = 90 - lpvec.y.getFloat();
			pitch = lpvec.x.getFloat();
		}

	}

	public float getX() {
		return this.pos[0];
	}

	public float getY() {
		return this.pos[1];
	}

	public float getZ() {
		return this.pos[2];
	}

	public int getFOV() {
		if (fov == 0)
			return defaultfov;
		return fov;
	}

	public float[] getViewOrigin() {
		return this.vieworigin;
	}

	public float[] getViewOffset() {
		return this.viewoffset;
	}

	public float[] getOrigin() {
		return this.pos;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

}
