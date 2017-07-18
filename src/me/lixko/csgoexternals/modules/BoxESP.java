package me.lixko.csgoexternals.modules;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.github.jonatino.misc.MemoryBuffer;
import com.sun.javafx.geom.Vec3f;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.GlowObjectDefinition;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.DrawUtils;

public class BoxESP extends Module {

	boolean needsDataUpdate = false;
	VectorMem lporigin = new VectorMem();
	MemoryBuffer lpvecbuf = new MemoryBuffer(lporigin.size());
	ArrayList<Vec3f> posarr = new ArrayList<Vec3f>();

	GlowObjectDefinition glowobj = new GlowObjectDefinition();
	MemoryBuffer g_glow = new MemoryBuffer(glowobj.size() * 64);
	NumberFormat f = new DecimalFormat("#.000");

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(1);
					if (Offsets.m_dwLocalPlayer == 0 || true)
						continue;

					long data_ptr = Engine.clientModule().readLong(Offsets.m_dwGlowObject);
					Engine.clientModule().read(data_ptr, 64 * glowobj.size(), g_glow);

					posarr.clear();

					// System.out.println("size: " + glowobj.size() + " red: " + glowobj.m_flGlowRed.offset() + " alpha: " + glowobj.m_flGlowAlpha.offset() + " occ: " + glowobj.m_bRenderWhenOccluded.offset() + " unocc: " + glowobj.m_bRenderWhenUnoccluded.offset());

					for (int i = 0; i < 64; i++) {
						glowobj.setSource(g_glow, i * glowobj.size());
						long entityaddr = glowobj.m_pEntity.getLong();
						if (entityaddr < 1 || entityaddr > Engine.clientModule().end())
							continue;

						Engine.clientModule().writeFloat(data_ptr + i * glowobj.size() + glowobj.m_flGlowRed.offset(), 1f);
						Engine.clientModule().writeFloat(data_ptr + i * glowobj.size() + glowobj.m_flGlowAlpha.offset(), 1f);
						Engine.clientModule().writeBoolean(data_ptr + i * glowobj.size() + glowobj.m_bRenderWhenUnoccluded.offset(), false);

						Engine.clientModule().writeBoolean(data_ptr + i * glowobj.size() + glowobj.m_bRenderWhenOccluded.offset(), true);

						int health = Engine.clientModule().readInt(entityaddr + Offsets.m_iHealth);
						int team = Engine.clientModule().readInt(entityaddr + Offsets.m_iTeamNum);

						if (health < 1 || team < 1)
							continue;

						// posarr.add(new Vec3f(lporigin.x.getFloat(), lporigin.y.getFloat(), lporigin.z.getFloat()));
					}

					// needsDataUpdate = false;
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	});

	@SuppressWarnings("static-access")
	@Override
	public void onUIRender() {
		if (!Client.theClient.isRunning || true)
			return;
		VectorMem toread = lporigin;

		/*
		 * DrawUtils.enableStringBackground();
		 * DrawUtils.setTextColor(DrawUtils.theme.textColor);
		 * Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecOrigin, lpvecbuf.size(), lpvecbuf);
		 * DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
		 * 
		 * Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecViewOffset, lpvecbuf.size(), lpvecbuf);
		 * DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 1 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
		 * 
		 * Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_angRotation, lpvecbuf.size(), lpvecbuf);
		 * DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 2 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
		 * 
		 * Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecVelocity, lpvecbuf.size(), lpvecbuf);
		 * DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 3 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
		 * 
		 * Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + Offsets.m_aimPunchAngle, lpvecbuf.size(), lpvecbuf);
		 * DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 4 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));
		 */
		//
		int classid = 0;
		int crossid = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Offsets.m_iCrosshairIndex);

		long data_ptr = Engine.clientModule().readLong(Offsets.m_dwGlowObject);
		Engine.clientModule().read(data_ptr, 64 * glowobj.size(), g_glow);
		for (int i = 0; i < 64; i++) {
			glowobj.setSource(g_glow, i * glowobj.size());
			long entityaddr = glowobj.m_pEntity.getLong();
			if (entityaddr < 1)
				continue;

			int health = Engine.clientModule().readInt(entityaddr + Offsets.m_iHealth);
			int team = Engine.clientModule().readInt(entityaddr + Offsets.m_iTeamNum);

			if (health < 1 || team < 1)
				continue;

			int entitylistid = Engine.clientModule().readInt(entityaddr + Offsets.m_iEntityIndex);

			long vtable = Engine.clientModule().readLong(entityaddr + 8);

			long fn = Engine.clientModule().readLong(vtable + 8 * 2);

			// long cls = Engine.clientModule().readLong(fn + 1);

			// classid = Engine.clientModule().readInt(cls + 0x14);

		}
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 4 * 18, "" + classid);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 3 * 18, "" + crossid);
	}

	@Override
	public void onEngineLoaded() {
		lporigin.setSource(lpvecbuf);
		updateLoop.start();
	}

	@Override
	public void onWorldRender() {
		if (!Client.theClient.isRunning || this.needsDataUpdate)
			return;

		DrawUtils.setTextColor(1.0f, 1.0f, 0.0f, 1.0f);
		DrawUtils.draw3DString("Hello, World!", 5f, 0f, 0f, DrawUtils.lppos.getOriginAngle(), DrawUtils.lppos.getOriginDistance() / 100000);

		/*
		 * DrawUtils.setColor(0x00FFFF80);
		 * DrawUtils.drawCube();
		 */
		this.needsDataUpdate = true;

	}

	@Override
	public void onLoop() {
	}
}
