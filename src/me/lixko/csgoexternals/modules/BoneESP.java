package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Studio;
import me.lixko.csgoexternals.structs.Mstudiobone_t;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MemoryUtils;

public class BoneESP extends Module {

	boolean needsDataUpdate = false;
	Module thismodule = this;
	Mstudiobone_t studiobone = new Mstudiobone_t();
	MemoryBuffer studiobonebuf = new MemoryBuffer(studiobone.size());
	float[][][][][] bonesBuf = new float[2][64][Studio.MAXSTUDIOBONES][2][3];
	float[][] colorBuf = new float[64][4];
	int bonesBufMutex = 0;

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(15);
					if (!thismodule.isToggled() || Offsets.m_dwLocalPlayer == 0)
						continue;

					updateArray();

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

	@Override
	public void onEngineLoaded() {
		studiobone.setSource(studiobonebuf);
		updateLoop.start();
	}

	@Override
	public void onWorldRender() {
		if (!Client.theClient.isRunning || !thismodule.isToggled())
			return;

		DrawUtils.setColor(0x00FFFFFF);
		int i = 0;
		for (float[][][] entBones : bonesBuf[bonesBufMutex]) {
			for (float[][] bone : entBones) {
				if (bone[1][0] == 0f || bone[1][1] == 0f || bone[1][2] == 0f || bone[0][0] == 0f || bone[0][1] == 0f || bone[0][2] == 0f)
					continue;
				DrawUtils.setColor(colorBuf[i]);
				DrawUtils.drawLine(bone[0], bone[1]);
			}
			i++;
		}
	}

	public void updateArray() {
		int procMutex = (bonesBufMutex + 1) % bonesBuf.length;
		bonesBuf[procMutex] = new float[64][Studio.MAXSTUDIOBONES][2][3];
		for (int i = 1; i < 64; i++) {
			long entityptr = MemoryUtils.getEntity(i);
			if (entityptr == 0)
				continue;
			if (entityptr == Offsets.m_dwLocalPlayer)
				continue;

			boolean isDormant = Engine.clientModule().readBoolean(entityptr + Offsets.m_bDormant);
			if (isDormant)
				continue;

			int health = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iHealth);
			if (health < 1)
				continue;

			if (health < 3 && health > 0) {
				colorBuf[i] = new float[] { 0f, 1f, 0f, 1f };
			} else {
				int team = Engine.clientModule().readInt(entityptr + Netvars.CBaseEntity.m_iTeamNum);
				if (team == 2) {
					colorBuf[i] = new float[] { 1f, health != 0 ? 1.0f - health / 100.0f : 0.0f, 0f, 1f };
				} else if (team == 3) {
					colorBuf[i] = new float[] { 0f, health != 0 ? 1.0f - health / 100.0f : 0.0f, 1f, 1f };
				}
			}
			
			// C_LocalTempEntity::DrawStudioModel(int)
			long studioModelptr = Engine.engineModule().readLong(entityptr + 0x2FC0);
			long studioModel = Engine.engineModule().readLong(studioModelptr);
			// 0x9C = offsetof(studiohdr_t, numbones) > numBones, boneIndex
			int[] studioHdrData = Engine.engineModule().read(studioModel + 0x9C, 2 * Integer.BYTES).getIntArray(0, 2);

			for (int bi = 0; bi < studioHdrData[0]; bi++) {
				bonesBuf[procMutex][i][bi][0] = AimBotGhetto.GetBonePosition(entityptr, bi);
				int parentBone = Engine.engineModule().readInt(studioModel + studioHdrData[1] + bi * studiobone.size() + 4);
				int flags = Engine.engineModule().readInt(studioModel + studioHdrData[1] + bi * studiobone.size() + 0xA0);

				if (parentBone == -1 || (flags & Studio.BONE_USED_BY_HITBOX) == 0 || parentBone > Studio.MAXSTUDIOBONES)
					continue;
				bonesBuf[procMutex][i][bi][1] = AimBotGhetto.GetBonePosition(entityptr, parentBone);
			}
		}
		bonesBufMutex = procMutex;
	}

}
