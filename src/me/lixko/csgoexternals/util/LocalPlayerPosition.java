package me.lixko.csgoexternals.util;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.VectorMem;

public class LocalPlayerPosition {
	private VectorMem lpvec = new VectorMem();
	private MemoryBuffer lpvecbuf = new MemoryBuffer(lpvec.size());
	private float cx = 0f, cy = 0f, cz = 0f, pitch = 0f, yaw = 0f;
	private float originangle = 0f;
	private float origindistance = 0f;
	
	public LocalPlayerPosition() {
		lpvec.setSource(lpvecbuf);
	}
	
	public void updateData() {
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecOrigin, lpvec.size(), lpvecbuf);
		cx = lpvec.x.getFloat();
		cy = lpvec.y.getFloat();
		cz = -lpvec.z.getFloat();
		
		float x1 = 0f, y1 = 0f, x2 = 999999f, y2 = 0f, x3 = 0f, y3 = 0f, x4 = cx, y4 = cz;
		
		originangle = (float) MathUtils.calculateAngleDeg(x1, y1, x2, y2, x3, y3, x4, y4);
		originangle += 90f;
		originangle = (float) MathUtils.normalizeAngle(originangle);
		origindistance = (float) MathUtils.calculateDistance(0, 0, cx, cz);
		
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_angRotation, lpvecbuf.size(), lpvecbuf);
		yaw = 90 - lpvec.z.getFloat();
		pitch = lpvec.x.getFloat();
	}
	
	public float getX() {
		return this.cx;
	}
	
	public float getY() {
		return this.cy;
	}
	
	public float getZ() {
		return this.cz;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getOriginAngle() {
		return this.originangle;
	}
	
	public float getOriginDistance() {
		return this.origindistance;
	}
}
