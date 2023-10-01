package me.lixko.csgoexternals.modules;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.sdk.Studio;
import me.lixko.csgoexternals.sdk.Studio.Studiohdr_t;
import me.lixko.csgoexternals.structs.GlowObjectDefinition;
import me.lixko.csgoexternals.structs.Matrix4x4Mem;
import me.lixko.csgoexternals.structs.Mstudiobone_t;
import me.lixko.csgoexternals.structs.Mstudioseqdesc_t;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.bsp.BSPParser;

public class TestModule extends Module {

	boolean needsDataUpdate = false;
	VectorMem lporigin = new VectorMem();
	MemoryBuffer lpvecbuf = new MemoryBuffer(lporigin.size());

	GlowObjectDefinition glowobj = new GlowObjectDefinition();
	MemoryBuffer g_glow = new MemoryBuffer(glowobj.size() * 64);
	NumberFormat f = new DecimalFormat("#0.000");
	int m_hObserverTarget;
	
	int toshowi = 0;
	int distmapsize = 0;
	
	long lastr = 0;
	@SuppressWarnings("static-access")
	@Override
	public void onUIRender() {
		if (true) return;

		VectorMem vavec = new VectorMem();
		MemoryBuffer vecbuf = new MemoryBuffer(lporigin.size());
		vavec.setSource(vecbuf);
				
		Engine.engineModule().read(Offsets.m_dwLocalPlayer, vecbuf);

		DrawUtils.setTextColor(DrawUtils.theme.textColor);
		DrawUtils.setStyle(ChatColor.MEDIUM, ChatColor.AQUA);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, StringFormat.dump(vavec.getVector()));
		
		
		if (true) return;
		int obstarget = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + m_hObserverTarget) & Const.ENT_ENTRY_MASK;
		if(obstarget != Const.ENT_ENTRY_MASK) {
			DrawUtils.setColor(0xFFFF00FF);
			DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, DrawUtils.getScreenHeight() - 50, "" + obstarget);
		}
		
		Matrix4x4Mem matrixstr = new Matrix4x4Mem();
		MemoryBuffer matrixbfr = new MemoryBuffer(matrixstr.size());
		matrixstr.setSource(matrixbfr);
		Engine.clientModule().read(Offsets.g_matCurrentCamInverse, matrixbfr);
		float[][] mat = matrixstr.getMatrix();
		
		DrawUtils.setTextColor(DrawUtils.theme.textColor);
		DrawUtils.setStyle(ChatColor.MEDIUM, ChatColor.AQUA);
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 4; y++) {
				DrawUtils.drawString(y * 100 + 50, DrawUtils.getScreenHeight() - x * 20 - 100, "" + ((double) Math.round(mat[x][y] * 1000d) / 1000d));
			}
		}
		
		
		
		if (true)
			return;
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, toshowi + " / " + distmapsize);

		// if (!Client.theClient.isRunning || true)
		// return;
		VectorMem toread = lporigin;

		/*DrawUtils.enableStringBackground();
		DrawUtils.setTextColor(DrawUtils.theme.textColor);
		DrawUtils.setStyle(ChatColor.MEDIUM, ChatColor.GOLD);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_vecOrigin, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		DrawUtils.setStyle(ChatColor.GRAY);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + 0x74, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 1 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_angRotation, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 2 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBasePlayer.veloc, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 3 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBasePlayer.localdata.m_Local.BASE_OFFSET + Netvars.CBasePlayer.localdata.m_Local.m_aimPunchAngle, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 4 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
	*/
	}

	@Override
	public void onWorldRender() {		
		if (!Client.theClient.isRunning)
			return;
		
		if (true) {
		int x = 0;
		DrawUtils.setTextColor(1.0f, 1.0f, 0.0f, 1.0f);
		TreeMap<Float, Integer> distmap = new TreeMap<>();

		for (int i = 0; i < Engine.bsp.pEntities.length; i++) {
			BSPParser.Entity e = Engine.bsp.pEntities[i];
			
			String originstr = e.properties.get("origin");
			if (originstr == null) continue;
			float[] origin = BSPParser.GetCoordsFromString(originstr);
			float distance = MathUtils.VecDist(origin, DrawUtils.lppos.getOrigin());
			if (distance > 300) continue;
			
			distmap.put(distance, i);			
		}
		distmapsize = distmap.size();
		if (toshowi >= distmap.size())
			toshowi = 0;
		
		//System.out.println(distmap.size());
		for (Entry<Float, Integer> entry : distmap.entrySet()) {
			if( Math.abs(x++ - toshowi) > 90) continue;
			BSPParser.Entity e = Engine.bsp.pEntities[entry.getValue()];
			
			float[] origin = BSPParser.GetCoordsFromString(e.properties.get("origin"));
			float distance = entry.getKey();
			float[] v = MathUtils.CalcAngle(DrawUtils.lppos.getOrigin(), origin);
			float pitch = 360f - (float) MathUtils.normalizeAngle(v[1] - 90f);
			
			float roll = v[0];
			float scale = Math.max(0.2f, distance / 1000f);
			float ex = origin[0];
			float ey = origin[2] + 5f * e.properties.size();
			float ez = -origin[1];
						
			for (Map.Entry<String, String> property : e.properties.entrySet()) {
			    DrawUtils.draw3DString(property.getKey() + ": " + property.getValue(), ex, ey + scale * 20 * 1, ez, pitch, roll, scale);
			    ey -= 5f;
			}
		}
		return;
		}
		
		MemoryBuffer vecbuf = new MemoryBuffer(lporigin.size());
		VectorMem entvec = new VectorMem();
		Mstudiobone_t studiobone = new Mstudiobone_t();
		Studiohdr_t studiohdr = new Studiohdr_t();
		MemoryBuffer studiobuf = new MemoryBuffer(studiohdr.size());
		MemoryBuffer studiobonebuf = new MemoryBuffer(studiobone.size());
		studiobone.setSource(studiobonebuf);
		//studiohdr.setSource(studiobuf);
		entvec.setSource(vecbuf);
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
			float scale = Math.max(0.3f, distance / 300f);

			Engine.clientModule().read(entityptr + Netvars.CBasePlayer.localdata.m_vecViewOffset_0, vecbuf);
			// System.out.println(entvec.z.f);
			ey += entvec.z.getFloat();

			// TODO: Tracers
			// DrawUtils.drawLine(entv, MathUtils.cadd(DrawUtils.lppos.getOrigin(), new float[]{ 0, 0, 64 }));

			if (isDormant)
				DrawUtils.setTextColor(ChatColor.RED.color);
			else
				DrawUtils.setTextColor(ChatColor.AQUA.color);

			long studioModelptr = Engine.clientModule().readLong(entityptr + 0x2FC0);
			long entityModelName = Engine.clientModule().readLong(entityptr + 0x370);
			long entityModelIndex = Engine.clientModule().readInt(entityptr + 0x28C);
			long studioModel = Engine.engineModule().readLong(studioModelptr);
			
			Engine.engineModule().read(studioModel, studiobuf);
			Engine.engineModule().readString(entityModelName, 64);
			studiohdr.readFrom(studiobuf);
			//System.out.println(studiohdr.numincludemodels);
			//System.out.println(StringFormat.hex(Engine.engineModule().readLong(studioModelptr + 8)));
			
			
			/*final int AIMSEQUENCE_LAYER = 1;	// Aim sequence uses layers 0 and 1 for the weapon idle animation (needs 2 layers so it can blend).
			final int ACT_CSGO_RELOAD = 976;
			long seqdesc = studioModel + Engine.engineModule().readInt(studioModel + 192) + 212 * AIMSEQUENCE_LAYER;
			float weight = Engine.engineModule().readFloat(seqdesc + 20);
			int result = Engine.engineModule().readInt(seqdesc + 16);
			Mstudioseqdesc_t seqdescs = new Mstudioseqdesc_t();
			;
			
			seqdescs.readFrom(Engine.engineModule(), seqdesc);
			System.out.println(StringFormat.hex(seqdescs.activity));
			System.out.println(StringFormat.hex(result) + " / " + weight);*/

			
			int numBones = studiohdr.numbones;
			int boneIndex = studiohdr.boneindex;
			DrawUtils.setColor(0x00FFFFFF);
			for (int bi = 0; bi < numBones; bi++) {
				float[] bone1 = AimBotGhetto.GetBonePosition(entityptr, bi);
				Engine.engineModule().read(studioModel + boneIndex + bi * studiobone.size(), studiobonebuf);
				int parentBone = studiobone.parent.getInt();
				if (parentBone == -1 || (studiobone.flags.getInt() & Studio.BONE_USED_BY_HITBOX) == 0)
					continue;

				DrawUtils.draw3DString(parentBone + "", bone1[0], bone1[2], -bone1[1], pitch, roll, scale*0.4f);
				float[] bone2 = AimBotGhetto.GetBonePosition(entityptr, parentBone);
				DrawUtils.drawLine(bone1, bone2);
			}

			DrawUtils.draw3DString(studiohdr.name + "", ex, ey + scale * 20 * 1, ez, pitch, roll, scale);

			// String name = studiohdr.name.getString();
			// DrawUtils.draw3DString(name, ex, ey + scale * 20 * 2, ez, pitch, roll, scale);

		}

		// DrawUtils.drawCube();

	}
	
	public boolean onCommand(String command) {
		if(command.equals("forceupdate")) {
			Engine.engineModule().writeInt(Offsets.m_dwClientState + Offsets.m_nDeltaTick, -1);
		}
		return true;
	}

	@Override
	public void onEngineLoaded() {
		lporigin.setSource(lpvecbuf);
		//m_hObserverTarget = Offsets.netvars.get("CBasePlayer", "m_hObserverTarget");
	}
	
	// https://github.com/ValveSoftware/source-sdk-2013/blob/0d8dceea4310fde5706b3ce1c70609d72a38efdf/sp/src/game/shared/animation.cpp#L888
	// returns Activity
	public static int GetSequenceActivity(long entity, int sequence) {
		return 0;
	}
}
