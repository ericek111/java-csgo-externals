package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.BaseAttributableItem;

public class SkinChanger extends Module {

	BaseAttributableItem itemobj = new BaseAttributableItem();
	MemoryBuffer itemobjbuf = new MemoryBuffer(itemobj.size());

	Thread skinChangerLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(2);
					if (Offsets.m_dwLocalPlayer == 0 || Offsets.m_dwEntityList == 0)
						continue;

					for (int w = 0; w < 64; w++) {
						int weaponentindex = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + 0x3528 + 4 * (w - 1)) & 0xFFF;
						if (weaponentindex == 0)
							continue;
						long weaponptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + weaponentindex * Offsets.m_dwEntityLoopDistance);
						if (weaponptr == 0)
							continue;
						Engine.clientModule().read(weaponptr + 0x34c0, itemobjbuf);
						int m_iItemDefinitionIndex = itemobj.m_iItemDefinitionIndex.getInt();
						if (m_iItemDefinitionIndex != 9)
							continue;

						int m_nFallbackPaintKit = itemobj.m_nFallbackPaintKit.getInt();
						int m_OriginalOwnerXuidLow = itemobj.m_OriginalOwnerXuidLow.getInt();
						itemobj.m_nFallbackPaintKit.set(279);
						itemobj.m_nFallbackSeed.set(-1);
						itemobj.m_nFallbackStatTrak.set(6262);
						itemobj.m_iEntityQuality.set(4);
						itemobj.m_flFallbackWear.set(0.05f);
						itemobj.m_iItemIDHigh.set(-1); // When iItemIDHigh is set to non zero value, fallback values will be used.
						itemobj.m_iAccountID.set(m_OriginalOwnerXuidLow);
						// System.out.println(weaponentindex + " > " + m_iItemDefinitionIndex + " / " + m_nFallbackPaintKit + " / " + MemoryUtils.getEntityClassName(weaponptr));
						for (int i = 0; i < 100; i++) {
							Engine.clientModule().write(weaponptr + 0x34c0, itemobjbuf);
							Thread.sleep(1);
						}
					}
					// System.out.println("----------------");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		itemobj.setSource(itemobjbuf);
		// skinChangerLoop.start();
	}
}
