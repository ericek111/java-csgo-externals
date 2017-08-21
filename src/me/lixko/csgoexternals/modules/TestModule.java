package me.lixko.csgoexternals.modules;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.github.jonatino.misc.MemoryBuffer;
import com.sun.javafx.geom.Vec3f;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Studio;
import me.lixko.csgoexternals.structs.GlowObjectDefinition;
import me.lixko.csgoexternals.structs.Mstudiobone_t;
import me.lixko.csgoexternals.structs.Studiohdr_t;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;

public class TestModule extends Module {

	boolean needsDataUpdate = false;
	VectorMem lporigin = new VectorMem();
	MemoryBuffer lpvecbuf = new MemoryBuffer(lporigin.size());
	ArrayList<Vec3f> posarr = new ArrayList<Vec3f>();

	GlowObjectDefinition glowobj = new GlowObjectDefinition();
	MemoryBuffer g_glow = new MemoryBuffer(glowobj.size() * 64);
	NumberFormat f = new DecimalFormat("#0.000");

	@SuppressWarnings("static-access")
	@Override
	public void onUIRender() {
		if (true)
			return;
		// if (!Client.theClient.isRunning || true)
		// return;
		VectorMem toread = lporigin;

		DrawUtils.enableStringBackground();
		DrawUtils.setTextColor(DrawUtils.theme.textColor);
		DrawUtils.setStyle(ChatColor.MEDIUM, ChatColor.GOLD);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecOrigin, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		DrawUtils.setStyle(ChatColor.GRAY);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + 0x74, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 1 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_angRotation, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 2 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecVelocity, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 3 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + Offsets.m_aimPunchAngle, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 4 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
	}

	@Override
	public void onWorldRender() {
		if (!Client.theClient.isRunning)
			return;

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
			long entityptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + i * Offsets.m_dwEntityLoopDistance);
			if (entityptr == 0)
				continue;
			if (entityptr == Offsets.m_dwLocalPlayer)
				continue;

			boolean isDormant = Engine.clientModule().readBoolean(entityptr + Offsets.m_bDormant);

			Engine.clientModule().read(entityptr + Offsets.m_vecOrigin, vecbuf);

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
				float[] bone1 = AimBot.GetBonePosition(entityptr, bi);
				Engine.engineModule().read(studioModel + boneIndex + bi * studiobone.size(), studiobonebuf);
				int parentBone = studiobone.parent.getInt();
				if (parentBone == -1 || (studiobone.flags.getInt() & Studio.BONE_USED_BY_HITBOX) == 0)
					continue;
				// DrawUtils.draw3DString(parentBone + "", bone1[0], bone1[2], -bone1[1], pitch, roll, scale*0.4f);
				float[] bone2 = AimBot.GetBonePosition(entityptr, parentBone);
				DrawUtils.drawLine(bone1, bone2);
			}

			// DrawUtils.draw3DString(numBones + "", ex, ey + scale * 20 * 1, ez, pitch, roll, scale);

			// String name = studiohdr.name.getString();
			// DrawUtils.draw3DString(name, ex, ey + scale * 20 * 2, ez, pitch, roll, scale);

		}

		// DrawUtils.drawCube();

	}

	@Override
	public void onEngineLoaded() {
		lporigin.setSource(lpvecbuf);
	}
}
