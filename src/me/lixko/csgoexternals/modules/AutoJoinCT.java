package me.lixko.csgoexternals.modules;

import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.CSPlayerResource;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.TextAlign;
import me.lixko.csgoexternals.util.XKeySym;

public class AutoJoinCT extends Module {

	RankReveal rankreveal;
	Module thismod = this;
	CSPlayerResource pres = new CSPlayerResource();
	int loopc = 0;

	Thread autoJoin = new Thread(new Runnable() {
		@Override
		public void run() {
			int keycode = Engine.x11.XKeysymToKeycode(Engine.dpy.get(), new KeySym(XKeySym.XK_KP_9));
			
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(5);
					if (Offsets.m_dwLocalPlayer == 0 || !thismod.isToggled())
						continue;
					int myteam = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_iTeamNum);
					if (myteam == 3)
						continue;
					
					//Engine.clientModule().read(Offsets.m_dwPlayerResources + pres.m_iTeam.offset(), Integer.SIZE * 64, buf);
					
					int tCount = 0, ctCount = 0;
					
					for(int i = 1; i < 64; i++) {
						long entityptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + i * Offsets.m_dwEntityLoopDistance);
						if(entityptr == 0 || Offsets.m_dwLocalPlayer == entityptr) continue;
						
						int t = Engine.clientModule().readInt(entityptr + Netvars.CBaseEntity.m_iTeamNum);
						if(t == 2) {
							tCount++;
						} else if(t == 3) {
							ctCount++;
						}
					}
					
					boolean canJoinCT = false;
					
					if (ctCount < 2)
						canJoinCT = true;
					if (ctCount == 2 && tCount > 8)
						canJoinCT = true;
					if (ctCount == 3 && tCount > 11)
						canJoinCT = true;
					if (ctCount == 4 && tCount > 14)
						canJoinCT = true;
					if (ctCount == 5 && tCount > 17)
						canJoinCT = true;
					
					//System.out.println(tCount + " / " + ctCount);
					
					if(!canJoinCT) continue;

					Engine.x11.XFlush(Engine.dpy.get());
					Engine.xtest.XTestFakeKeyEvent(Engine.dpy.get(), keycode, true, new NativeLong(0));
					Thread.sleep(10);
					Engine.xtest.XTestFakeKeyEvent(Engine.dpy.get(), keycode, false, new NativeLong(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		rankreveal = (RankReveal) Client.theClient.moduleManager.getModule("RankReveal");
		autoJoin.start();
	}

	@Override
	public void onUIRender() {
		if (!this.isToggled() || Offsets.m_dwLocalPlayer == 0)
			return;
		loopc++;
		if (loopc == 90) {
			loopc = 0;
		}
		if (rankreveal.canJoinCT) {
			DrawUtils.setStyle(ChatColor.LARGE);
			DrawUtils.setTextColor(0.54f, 0.9f, 1.0f);
			DrawUtils.setAlign(TextAlign.CENTER);
			DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, DrawUtils.getScreenHeight() - 70, ">>> JOIN CT <<<");
		}
		if (this.isToggled()) {
			DrawUtils.setLineWidth(3f);
			DrawUtils.setColor(0.54f, 0.72f, 1.0f, (float) Math.sin((loopc / 90f) * Math.PI));
			DrawUtils.drawRectangle(DrawUtils.getScreenWidth() / 2 - 83, DrawUtils.getScreenHeight(), DrawUtils.getScreenWidth() / 2 - 35, DrawUtils.getScreenHeight() - 50);
		}
	}

	@Override
	public void onEnable() {
		loopc = 0;
	}

}
