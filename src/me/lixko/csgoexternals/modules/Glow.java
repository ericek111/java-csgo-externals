package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.unix.unixc;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.CEntInfo;
import me.lixko.csgoexternals.structs.CGlobalVars;
import me.lixko.csgoexternals.structs.CGlowObjectManager;
import me.lixko.csgoexternals.structs.CUtlVector;
import me.lixko.csgoexternals.structs.GlowObjectDefinition;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MemoryUtils;

public class Glow extends Module {

	GlowObjectDefinition glowobj = new GlowObjectDefinition();
	CGlowObjectManager glowman = new CGlowObjectManager();
	CUtlVector cvec = new CUtlVector();
	AutoDefuse autodefusemod;

	CEntInfo centinfo = new CEntInfo();
	MemoryBuffer centinfobuf = new MemoryBuffer(centinfo.size());

	MemoryBuffer cglowobjmanbuf = new MemoryBuffer(glowman.size());
	MemoryBuffer g_glow = new MemoryBuffer(glowobj.size() * 1024);
	private long[] l_remote = new long[1024];
	private long[] l_local = new long[1024];
	private int[] l_length = new int[1024];
	long bytesToCutOffEnd = glowobj.size() - glowobj.writeEnd();
	long bytesToCutOffBegin = glowobj.writeStart();
	long totalWriteSize = (glowobj.size() - (bytesToCutOffBegin + bytesToCutOffEnd));

	private long lastbombglow = 0;
	int objcount;
	long data_ptr;

	boolean glowEnabled = true;
	boolean glowOthers = true;
	boolean glowJustDisabled = false;

	CGlobalVars globalvars = new CGlobalVars();
	
	String classname = ""; // TODO: Optimize

	public void onLoop() {
		if (!(glowEnabled || glowJustDisabled))
			return;
		Engine.clientModule().read(Offsets.m_dwGlowObject, glowman.size(), cglowobjmanbuf);

		objcount = cglowobjmanbuf.getInt(cvec.Count.offset());
		data_ptr = cglowobjmanbuf.getLong(cvec.DataPtr.offset());

		Engine.clientModule().read(data_ptr, objcount * glowobj.size(), g_glow);
		int writeCount = 0;
		for (int i = 0; i < objcount; i++) {
			glowobj.setSource(g_glow, i * glowobj.size());
			long entityaddr = glowobj.m_pEntity.getLong();
			if (entityaddr < 1)
				continue;
			int team = Engine.clientModule().readInt(entityaddr + Netvars.CBaseEntity.m_iTeamNum);
			int health = Engine.clientModule().readInt(entityaddr + Netvars.CBasePlayer.m_iHealth);

			boolean issabomb = false;
			//String classname = MemoryUtils.getEntityClassName(entityaddr);
			int rendercolor = Engine.clientModule().readInt(entityaddr + 0xa8);

			// if(i < 5)System.out.println(i + ": " + StringFormat.hex(g_glow.lastReadAddress() + i * glowobj.size()));

			// Radar
			Engine.clientModule().writeBoolean(entityaddr + Offsets.m_bSpotted, true);

			glowobj.m_bRenderWhenOccluded.set(true);
			glowobj.m_bRenderWhenUnoccluded.set(false);
			glowobj.m_bFullBloomRender.set(false);

			// Engine.clientModule().writeInt(entityaddr + 0xa8 , 0xFF0000FF);
			health = Math.min(health, 100);

			/*if (classname.startsWith("4C_C4") || classname.startsWith("11C_PlantedC4")) {
				glowobj.m_flGlowRed.set(1.0f);
				glowobj.m_flGlowGreen.set(0.4f);
				glowobj.m_flGlowBlue.set(0.0f);
				glowobj.m_flGlowAlpha.set(1.0f);
				glowobj.m_bFullBloomRender.set(true);
				if (classname.startsWith("11C_PlantedC4"))
					issabomb = true;
			} else*/ if (team == 2) {
				glowobj.m_flGlowRed.set(1.0f);
				glowobj.m_flGlowGreen.set(health != 0 ? 1.0f - health / 100.0f : 0.0f);
				glowobj.m_flGlowBlue.set(0.0f);
				glowobj.m_flGlowAlpha.set(0.55f);
			} else if (team == 3) {
				glowobj.m_flGlowRed.set(0.0f);
				glowobj.m_flGlowGreen.set(health != 0 ? 1.0f - health / 100.0f : 0.0f);
				glowobj.m_flGlowBlue.set(1.0f);
				glowobj.m_flGlowAlpha.set(0.55f);

				if (rendercolor == 0xff00ff00 || rendercolor == 0xffff0000 || rendercolor == 0xff0000ff) {
					glowobj.m_flGlowRed.set((health / 100.0f) * 0.4f);
					glowobj.m_flGlowGreen.set(health != 0 ? 1.0f - health / 100.0f : 0.0f);
					glowobj.m_flGlowBlue.set(1.0f);
					glowobj.m_flGlowAlpha.set(1.00f);
				}
			} else {
				if (glowOthers)
					glowobj.m_bRenderWhenOccluded.set(true);
				else
					glowobj.m_bRenderWhenOccluded.set(false);
				glowobj.m_flGlowRed.set(0.0f);
				glowobj.m_flGlowGreen.set(1.0f);
				glowobj.m_flGlowBlue.set(1.0f);
				glowobj.m_flGlowAlpha.set(0.4f);
			}
			if (health < 3 && health > 0) {
				glowobj.m_flGlowRed.set(0.0f);
				glowobj.m_flGlowGreen.set(1.0f);
				glowobj.m_flGlowBlue.set(0.0f);
				glowobj.m_flGlowAlpha.set(0.7f);
			}

			if (glowJustDisabled || !glowEnabled) {
				glowEnabled = false;
				glowJustDisabled = false;
				glowobj.m_flGlowRed.set(0f);
				glowobj.m_flGlowGreen.set(0f);
				glowobj.m_flGlowBlue.set(0f);
				glowobj.m_flGlowAlpha.set(0f);
				glowobj.m_bRenderWhenOccluded.set(false);
				glowobj.m_bRenderWhenUnoccluded.set(false);
				glowobj.m_bFullBloomRender.set(false);
			}

			if (issabomb) {
				autodefusemod.bombentityaddr = entityaddr;
				autodefusemod.autodefuset = true;
				lastbombglow = System.currentTimeMillis();
			} else if (System.currentTimeMillis() > lastbombglow + 500) {
				autodefusemod.autodefuset = false;
			}

			// glowobj.m_bFullBloomRender.set(true);
			// glowobj.m_flGlowAlpha.set(0.5f);
			// glowobj.m_nGlowStyle.set(3);

			long glowptr = Pointer.nativeValue(g_glow);

			l_local[writeCount] = glowptr + bytesToCutOffBegin + glowobj.size() * i;
			l_remote[writeCount] = data_ptr + bytesToCutOffBegin + glowobj.size() * i;
			l_length[writeCount] = (int) totalWriteSize;

			writeCount++;
		}
		unixc.mem_write(Engine.process().id(), l_local, l_remote, l_length);
	}

	@Override
	public void onKeyPress(KeySym sym) {
		if (sym.intValue() == X11.XK_Control_R) {
			glowOthers = !glowOthers;
		}
	}

	@Override
	public void onEnable() {
		glowEnabled = true;
	}

	@Override
	public void onDisable() {
		glowJustDisabled = true;
	}

	@Override
	public void onEngineLoaded() {
		autodefusemod = (AutoDefuse) Client.theClient.moduleManager.getModule("AutoDefuse");
	}

}
