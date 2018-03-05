package me.lixko.csgoexternals.modules;

import java.util.ArrayList;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Studio;
import me.lixko.csgoexternals.structs.Mstudiobone_t;
import me.lixko.csgoexternals.structs.Studiohdr_t;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.bsp.BSPParser;
import me.lixko.csgoexternals.util.bsp.BSPParser.Entity;

public class VisibleTest extends Module {

	boolean needsDataUpdate = false;
	VectorMem lporigin = new VectorMem();
	MemoryBuffer lpvecbuf = new MemoryBuffer(lporigin.size());
	
	@Override
	public void onWorldRender() {
		if(true) return;
		if (!Client.theClient.isRunning)
			return;
		
		for (Entity e : Engine.bsp.pEntities) {
			if (e.properties.get("classname").equals("info_player_terrorist")) {
				float[] origin = BSPParser.GetCoordsFromString(e.properties.get("origin"));
				float[] angles = BSPParser.GetCoordsFromString(e.properties.get("angles"));
				float[] end = new float[3];
				end[0] = origin[0] +  (float) Math.cos(angles[0]) * 30;
				end[1] = origin[1] + (float) Math.sin(angles[0]) * 30;
				end[2] = origin[2];
				DrawUtils.setColor(0xff0000ff);
				DrawUtils.drawLine(origin, end);
				
			}
		}
				
		if(true) return;
		int xd = 0;
		for(Entity ent : Engine.bsp.pEntities) {
			xd++;
			String orstr = ent.properties.get("origin");
			if(orstr == null) continue;
			//System.out.println(xd + " / " + Engine.bsp.pEntities.length + " " + orstr);

			float[] origin = BSPParser.GetCoordsFromString(orstr);

			String classname = ent.properties.get("classname");
			String mdlname = ent.properties.get("model");
			//System.out.println(xd + " > " + StringFormat.dump(origin) + " / " + classname);
			
			float distance = MathUtils.VecDist(DrawUtils.lppos.getOrigin(), origin);
			
			float[] v = MathUtils.CalcAngle(DrawUtils.lppos.getViewOrigin(), origin);
			float pitch = 360f - (float) MathUtils.normalizeAngle(v[1] - 90f);
			float roll = v[0];
			float scale = Math.max(0.3f, distance / 1000f);
			
			//System.out.println(xd + " " + scale);
			if(mdlname == null) {
				//if(true) continue;
				DrawUtils.setTextColor(0x00FFFFFF);
				DrawUtils.draw3DString(xd + " / " + classname, origin[0], origin[2], -origin[1], pitch, roll, scale*0.4f);
				continue;
			} else {
				DrawUtils.setTextColor(0xFF0000FF);
				DrawUtils.draw3DString(xd + " / " + mdlname, origin[0], origin[2], -origin[1], pitch, roll, scale*0.4f);
			}			
		}
		if(true) return;

		MemoryBuffer vecbuf = new MemoryBuffer(lporigin.size());
		VectorMem entvec = new VectorMem();
		Mstudiobone_t studiobone = new Mstudiobone_t();
		Studiohdr_t studiohdr = new Studiohdr_t();
		MemoryBuffer studiobuf = new MemoryBuffer(studiohdr.size());
		MemoryBuffer studiobonebuf = new MemoryBuffer(studiobone.size());
		studiobone.setSource(studiobonebuf);
		studiohdr.setSource(studiobuf);
		entvec.setSource(vecbuf);
		DrawUtils.setTextColor(1.0f, 1.0f, 0.0f, 1.0f);
		for (int i = 1; i < 64; i++) {
			long entityptr = MemoryUtils.getEntity(i);
			if (entityptr == 0)
				continue;
			if (entityptr == Offsets.m_dwLocalPlayer)
				continue;

			boolean isDormant = Engine.clientModule().readBoolean(entityptr + Offsets.m_bDormant);

			Engine.clientModule().read(entityptr + Netvars.CBaseEntity.m_vecOrigin, vecbuf);

			float ex = entvec.x.getFloat();
			float ey = entvec.z.getFloat() + 10f;
			float ez = -entvec.y.getFloat();
			float[] entv = entvec.getVector();
			float distance = MathUtils.VecDist(entvec.getVector(), DrawUtils.lppos.getOrigin());
			float[] v = MathUtils.CalcAngle(DrawUtils.lppos.getOrigin(), entvec.getVector());
			float pitch = 360f - (float) MathUtils.normalizeAngle(v[1] - 90f);
			float roll = v[0];
			float scale = Math.max(0.3f, distance / 1000f);

			Engine.clientModule().read(entityptr + Offsets.m_vecViewOffset, vecbuf);
			// System.out.println(entvec.z.f);
			ey += entvec.z.getFloat();

			// TODO: Tracers
			// DrawUtils.drawLine(entv, MathUtils.cadd(DrawUtils.lppos.getOrigin(), new float[]{ 0, 0, 64 }));

			if (isDormant)
				DrawUtils.setTextColor(ChatColor.RED.color);
			else
				DrawUtils.setTextColor(ChatColor.AQUA.color);

			long studioModelptr = Engine.engineModule().readLong(entityptr + 0x2FC0);
			long studioModel = Engine.engineModule().readLong(studioModelptr);

			Engine.engineModule().read(studioModel, studiobuf);
			int numBones = studiohdr.numbones.getInt();
			int boneIndex = studiohdr.boneindex.getInt();
			DrawUtils.setColor(0x00FFFFFF);
			for (int bi = 0; bi < numBones; bi++) {
				float[] bone1 = AimBotGhetto.GetBonePosition(entityptr, bi);
				Engine.engineModule().read(studioModel + boneIndex + bi * studiobone.size(), studiobonebuf);
				int parentBone = studiobone.parent.getInt();
				if (parentBone == -1 || (studiobone.flags.getInt() & Studio.BONE_USED_BY_HITBOX) == 0)
					continue;
				if(Engine.bsp.isVisible(DrawUtils.lppos.getViewOrigin(), bone1)) {
					DrawUtils.setTextColor(0x00FF00FF);
				} else {
					DrawUtils.setTextColor(0xFF0000FF);
				}
				DrawUtils.draw3DString(parentBone + "", bone1[0], bone1[2], -bone1[1], pitch, roll, scale*0.4f);
				float[] bone2 = AimBotGhetto.GetBonePosition(entityptr, parentBone);
				DrawUtils.drawLine(bone1, bone2);
			}
			/*boolean isEntityVisible = Engine.bsp.isVisible(DrawUtils.lppos.getViewOrigin(), entvec.getVector());
			if(isEntityVisible) {
				DrawUtils.setTextColor(0x00FF00FF);
			} else {
				DrawUtils.setTextColor(0xFF0000FF);
			}
			DrawUtils.draw3DString(numBones + "", ex, ey + scale * 20 * 1, ez, pitch, roll, scale);*/

			// String name = studiohdr.name.getString();
			// DrawUtils.draw3DString(name, ex, ey + scale * 20 * 2, ez, pitch, roll, scale);

		}

		// DrawUtils.drawCube();

	}
}
