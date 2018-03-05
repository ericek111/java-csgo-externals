package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.XKeySym;

public class RCS extends Module {
	
	MemoryBuffer viewanglesbuf = new MemoryBuffer(Float.BYTES * 3);
	VectorMem viewangles = new VectorMem(viewanglesbuf);
	float[] va = new float[3];
	float[] old = new float[3];
	
	@Override
	public void onLoop() {
		if(!this.isToggled()) return;
		boolean work = Client.theClient.keyboardHandler.isPressed(XKeySym.XK_R);
		if(!work) {
			old[0] = 0;
			old[1] = 0;
			old[2] = 0;
			return;
		}
		
		Engine.engineModule().read(Offsets.m_dwClientState + Offsets.m_vecViewAngles, viewanglesbuf);
		viewangles.copyTo(va);
		MathUtils.add(va, old);
		MathUtils.subtract(va, MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), 2f));
		//MathUtils.subtract(va, MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), new float[] {2, 2, 2}));
		old = MathUtils.cmultiply(DrawUtils.lppos.getAimPunch(), 2f);
		MathUtils.ClampAngle(va);
		viewangles.readFrom(va);
		Engine.engineModule().write(Offsets.m_dwClientState + Offsets.m_vecViewAngles, viewanglesbuf);

	}
	
}
