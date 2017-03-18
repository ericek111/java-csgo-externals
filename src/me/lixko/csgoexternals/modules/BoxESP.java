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
					if (!needsDataUpdate)
						continue;

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

						posarr.add(new Vec3f(lporigin.x.getFloat(), lporigin.y.getFloat(), lporigin.z.getFloat()));
					}

					needsDataUpdate = false;
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
		if (true || !Client.theClient.isRunning)
			return;
		VectorMem toread = lporigin;

		DrawUtils.enableStringBackground();
		DrawUtils.setTextColor(DrawUtils.theme.textColor);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecOrigin, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecViewOffset, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 1 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_angRotation, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 2 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_vecVelocity, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 3 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Offsets.m_Local + Offsets.m_aimPunchAngle, lpvecbuf.size(), lpvecbuf);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, 50 + 4 * 18, f.format(toread.x.getFloat()) + ", " + f.format(toread.y.getFloat()) + ", " + f.format(toread.z.getFloat()));

		// this.needsDataUpdate = true;

	}

	@Override
	public void onEngineLoaded() {
		lporigin.setSource(lpvecbuf);
		updateLoop.start();
	}

	@Override
	public void onWorldRender() {
		/*
		 * if (!Client.theClient.isRunning || this.needsDataUpdate) return;
		 * DrawUtils.setColor(0x00FFFF80); DrawUtils.drawCube();
		 * this.needsDataUpdate = true;
		 */
	}

	@Override
	public void onLoop() {
	}
}
