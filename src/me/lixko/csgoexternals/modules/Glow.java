package me.lixko.csgoexternals.modules;

import java.util.function.Supplier;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.unix.unixc;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.CGlowObjectManager;
import me.lixko.csgoexternals.structs.CUtlVector;
import me.lixko.csgoexternals.structs.GlowObjectDefinition;
import me.lixko.csgoexternals.util.DrawUtils;

public class Glow extends Module {

	GlowObjectDefinition glowobj = new GlowObjectDefinition();
	CGlowObjectManager glowman = new CGlowObjectManager();
	CUtlVector cvec = new CUtlVector();
	
	MemoryBuffer cglowobjmanbuf = new MemoryBuffer(glowman.size());
	MemoryBuffer g_glow = new MemoryBuffer(glowobj.size() * 1024);
	private long[] l_remote = new long[1024];
	private long[] l_local = new long[1024];
	private int[] l_length = new int[1024];
	long bytesToCutOffEnd = glowobj.size() - glowobj.writeEnd();
	long bytesToCutOffBegin = glowobj.writeStart();
	long totalWriteSize = (glowobj.size() - (bytesToCutOffBegin + bytesToCutOffEnd));
	
	/*unix.iovec g_remote[] = new unix.iovec[1024];
	unix.iovec g_local[] = new unix.iovec[1024];
	Pointer p_remote[] = fill(new Pointer[1024], () -> new Pointer(0));
	Pointer p_local[] = fill(new Pointer[1024], () -> new Pointer(0));*/
		
	int objcount;
	long data_ptr;
	
	boolean glowEnabled = true;
	boolean glowOthers = true;
	boolean glowJustDisabled = false;

	@Override
	public void onUIRender() {
		if(!glowEnabled) return;
		DrawUtils.textRenderer.setColor(0.0f, 1.0f, 1.0f, 0.8f);
		DrawUtils.setColor(0.1f, 0.2f, 0.3f, 0.8f);
		DrawUtils.drawCenteredStringWithBackground(DrawUtils.drawable.getSurfaceWidth()/2, 15, glowOthers ? "GLOW All" : "GLOW Players");
	}

	public void onLoop() {
		if (!(glowEnabled || glowJustDisabled)) return;
		
		Engine.clientModule().read(Offsets.m_dwGlowObject, glowman.size(), cglowobjmanbuf);
		
		// glowman.m_GlowObjectDefinitions.offset() + cvec.x.offset(), but m_GlowObjectDefinitions offset = 0
		objcount = cglowobjmanbuf.getInt(cvec.Count.offset());
		data_ptr = cglowobjmanbuf.getLong(cvec.DataPtr.offset());
		
		Engine.clientModule().read(data_ptr, objcount * glowobj.size(), g_glow);

		int writeCount = 0;
		for (int i = 0; i < objcount; i++) {
			glowobj.setSource(g_glow, i * glowobj.size());
			long entityaddr = glowobj.m_pEntity.getLong();
			if (entityaddr < 1)
				continue;
			int team = Engine.clientModule().readInt(entityaddr + 0x128);
			int health = Engine.clientModule().readInt(entityaddr + 0x134);

			// Radar
			Engine.clientModule().writeBoolean(entityaddr + 0xECD, true);
			
			glowobj.m_bRenderWhenOccluded.set(true);
			glowobj.m_bRenderWhenUnoccluded.set(false);
			glowobj.m_bFullBloomRender.set(false);
			
			if (team == 2) {
				glowobj.m_flGlowRed.set(1.0f);
				glowobj.m_flGlowGreen.set(health != 0 ? 1.0f - health / 100.0f : 0.0f);
				glowobj.m_flGlowBlue.set(0.0f);
				glowobj.m_flGlowAlpha.set(0.55f);
			} else if (team == 3) {
				glowobj.m_flGlowRed.set(0.0f);
				glowobj.m_flGlowGreen.set(health != 0 ? 1.0f - health / 100.0f : 0.0f);
				glowobj.m_flGlowBlue.set(1.0f);
				glowobj.m_flGlowAlpha.set(0.55f);
			} else {
				if(glowOthers) glowobj.m_bRenderWhenOccluded.set(true);
				else glowobj.m_bRenderWhenOccluded.set(false);
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
			if(glowJustDisabled && !glowOthers) {	
				glowEnabled = false;
				glowJustDisabled = false;
				glowobj.m_flGlowRed.set(0f);
				glowobj.m_flGlowGreen.set(0f);
				glowobj.m_flGlowBlue.set(0f);
				glowobj.m_flGlowAlpha.set(0f);
				glowobj.m_bRenderWhenOccluded.set(false);
				glowobj.m_bRenderWhenUnoccluded.set(false);
				glowobj.m_bFullBloomRender.set(false);
				System.out.println("Disabling glow!!");
			}
			
			if(glowJustDisabled && glowOthers) glowOthers = false;
			
			long glowptr = Pointer.nativeValue(g_glow);
			
			l_local[writeCount] = glowptr + bytesToCutOffBegin + glowobj.size() * i;
			l_remote[writeCount] = data_ptr + bytesToCutOffBegin + glowobj.size() * i;
			l_length[writeCount] = (int)totalWriteSize;
			
			/*Pointer bufptr = g_glow;
			Pointer.nativeValue(p_remote[writeCount], l_remote[writeCount]);
			Pointer.nativeValue(p_local[writeCount], l_local[writeCount]);
			
			if(g_remote[writeCount] == null) {
				g_remote[writeCount] = new unix.iovec();
				g_local[writeCount] = new unix.iovec();
			}
			g_remote[writeCount].iov_base = p_remote[writeCount];
			g_local[writeCount].iov_base = p_local[writeCount];
			g_remote[writeCount].iov_len = g_local[writeCount].iov_len = (int) totalWriteSize;*/
			
			writeCount++;
		}
		//unix.process_vm_writev(Engine.process().id(), g_local, writeCount, g_remote, writeCount, 0);
		unixc.mem_write(Engine.process().id(), l_local, l_remote, l_length );
	}

	@Override
	public void onKeyPress(KeySym sym) {
		if (sym.intValue() == X11.XK_Alt_L) {
			if (!glowEnabled)
				glowEnabled = true;
			else
				glowJustDisabled = true;
		} else if (sym.intValue() == X11.XK_Control_R) {
			glowOthers = !glowOthers;
		}
	}
	
	static <E> E[] fill(E[] arr, Supplier<? extends E> supp) {
	    for(int i = 0; i < arr.length; i++) {
	        arr[i] = supp.get();
	    }
	    return arr;
	}

}
