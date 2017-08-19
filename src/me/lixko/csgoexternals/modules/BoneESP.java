package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.Mstudiobone_t;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.Studio;

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
					Thread.sleep(20);
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
			long entityptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + i * Offsets.m_dwEntityLoopDistance);
			if (entityptr == 0)
				continue;
			if (entityptr == Offsets.m_dwLocalPlayer)
				continue;

			boolean isDormant = Engine.clientModule().readBoolean(entityptr + Offsets.m_bDormant);
			if (isDormant)
				continue;

			int health = Engine.clientModule().readInt(entityptr + Offsets.m_iHealth);
			if (health < 1)
				continue;

			if (health < 3 && health > 0) {
				colorBuf[i] = new float[] { 0f, 1f, 0f, 1f };
			} else {
				int team = Engine.clientModule().readInt(entityptr + Offsets.m_iTeamNum);
				if (team == 2) {
					colorBuf[i] = new float[] { 1f, health != 0 ? 1.0f - health / 100.0f : 0.0f, 0f, 1f };
				} else if (team == 3) {
					colorBuf[i] = new float[] { 0f, health != 0 ? 1.0f - health / 100.0f : 0.0f, 1f, 1f };
				}
			}

			long studioModelptr = Engine.engineModule().readLong(entityptr + 0x2FC0);
			long studioModel = Engine.engineModule().readLong(studioModelptr);
			// 0x9C = offsetof(studiohdr_t, numbones) > numBones, boneIndex
			int[] studioHdrData = Engine.engineModule().read(studioModel + 0x9C, 2 * Integer.BYTES).getIntArray(0, 2);

			for (int bi = 0; bi < studioHdrData[0]; bi++) {
				bonesBuf[procMutex][i][bi][0] = AimBot.GetBonePosition(entityptr, bi);
				int parentBone = Engine.engineModule().readInt(studioModel + studioHdrData[1] + bi * studiobone.size() + 4);
				int flags = Engine.engineModule().readInt(studioModel + studioHdrData[1] + bi * studiobone.size() + 0xA0);

				if (parentBone == -1 || (flags & Studio.BONE_USED_BY_HITBOX) == 0 || parentBone > Studio.MAXSTUDIOBONES)
					continue;
				bonesBuf[procMutex][i][bi][1] = AimBot.GetBonePosition(entityptr, parentBone);
			}
		}
		bonesBufMutex = procMutex;
	}

}
