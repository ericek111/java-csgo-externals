package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.BoneList;
import me.lixko.csgoexternals.structs.Matrix3x4Mem;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.XKeySym;

public class AimBot extends Module {
	
	MemoryBuffer viewanglesbuf = new MemoryBuffer(Float.BYTES * 3);
	VectorMem viewangles = new VectorMem(viewanglesbuf);
	float[] va = new float[3], targetAngles = new float[3];
	
	AimBotMode mode = AimBotMode.CROSSHAIR;
	AimBotTarget target = AimBotTarget.ENEMY;
	public static float AIMSTEP = 1f;
	
	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {}
				}
			}
		}
	});
	
	public void onUIRender() {
		DrawUtils.setTextColor(DrawUtils.theme.textColor);
		DrawUtils.setStyle(ChatColor.MEDIUM, ChatColor.AQUA);
		
		if (!Client.theClient.keyboardHandler.isPressed(XKeySym.XK_F))
			return;
		
		Engine.engineModule().read(Offsets.m_dwClientState + Offsets.m_vecViewAngles, viewanglesbuf);
		viewangles.copyTo(va);
		
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 90, StringFormat.dump(va));
		
		// MathUtils.subtract(va, MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), 2f));
		targetAngles = getTargetAngles();
		if(targetAngles == null) return;
		float[] anglesDiff = MathUtils.csubtract(va, targetAngles);
		
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, StringFormat.dump(anglesDiff));
		// DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 , StringFormat.dump(DrawUtils.lppos.getAimPunch()));
		//DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 10 , StringFormat.dump(MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), new float[] {2, 2, 2})));
		//MathUtils.subtract(va, MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), 2f));
		va = targetAngles;
		// MathUtils.subtract(va, MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), 2f));
		
		MathUtils.ClampAngle(va);
		viewangles.readFrom(va);
		Engine.engineModule().write(Offsets.m_dwClientState + Offsets.m_vecViewAngles, viewanglesbuf);
		//Thread.sleep(5);
	}
	
	public float[] getTargetAngles() {
		float bestval = Float.MAX_VALUE;
		float[] bestenthitbox = null;
		int localteam = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_iTeamNum);
		
		int printyi = 0;
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
			boolean immunity = Engine.clientModule().readBoolean(entityptr + Netvars.CCSPlayer.m_bGunGameImmunity);
			if(immunity) continue;

			float[] hitbox = AimBotGhetto.GetBonePosition(entityptr, BoneList.BONE_HEAD.index);
			if (mode == AimBotMode.NEAREST) {
				float distance = MathUtils.VecDist(DrawUtils.lppos.getViewOrigin(), hitbox);
				DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 90 + printyi++ * 20 , Math.round(distance) + " > " + StringFormat.dump(hitbox) );
				if (distance < bestval) {
					bestval = distance;
					bestenthitbox = hitbox;
				} else
					continue;
			} else if (mode == AimBotMode.CROSSHAIR) {
				float fov = Math.abs(MathUtils.GetFov(va, DrawUtils.lppos.getViewOrigin(), hitbox));
				if (fov < bestval) {
					bestval = fov;
					bestenthitbox = hitbox;
				} else
					continue;
			}

		}
		
		if (mode == AimBotMode.NEAREST) {

		} else if (mode == AimBotMode.CROSSHAIR) {
		}
		if (bestenthitbox == null)
			return null;
		
		float[] dir = MathUtils.CalcAngle(DrawUtils.lppos.getViewOrigin(), bestenthitbox);
		MathUtils.ClampAngle(dir);
		
		return dir;
	}
	
	@Override
	public void onLoop() {

	}
	
	@Override
	public void onEngineLoaded() {
		//updateLoop.start();
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
