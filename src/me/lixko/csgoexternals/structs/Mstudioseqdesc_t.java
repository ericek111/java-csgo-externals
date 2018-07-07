package me.lixko.csgoexternals.structs;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.util.BufferStruct;

public class Mstudioseqdesc_t extends BufferStruct {
	public int baseptr;

	// studiohdr_t*
	public long pStudiohdr() {
		return this.lastRead() + baseptr;
	}

	public int szlabelindex;

	// char*
	public long pszLabel() {
		return this.lastRead() + szlabelindex;
	}

	public int szactivitynameindex;

	// char*
	public long pszActivityName() {
		return this.lastRead() + szactivitynameindex;
	}

	public int flags; // looping/non-looping flags

	public int activity; // initialized at loadtime to game DLL values
	public int actweight;

	public int numevents;
	public int eventindex;

	// mstudioevent_t*
	public long pEvent(int i) {
		//Assert( i >= 0 && i < numevents);
		return this.lastRead() + eventindex + i;
	}

	float[] bbmin = new float[3]; // per sequence bounding box
	float[] bbmax = new float[3];

	public int numblends;

	// Index into array of shorts which is groupsize[0] x groupsize[1] in length
	public int animindexindex;

	public int anim(int x, int y) {
		if (x >= groupsize[0]) {
			x = groupsize[0] - 1;
		}

		if (y >= groupsize[1]) {
			y = groupsize[1] - 1;
		}

		int offset = y * groupsize[0] + x;
		// short*
		long blends = this.lastRead() + animindexindex;
		// int value = (int)blends[offset];
		int value = ((MemoryBuffer) this.source())._lastreadsrc.readShort(blends + offset * Short.BYTES);
		return value;
	}

	public int movementindex; // [blend] float array for blended movement
	public int[] groupsize = new int[2];
	public int[] paramindex = new int[2]; // X, Y, Z, XR, YR, ZR
	public float[] paramstart = new float[2]; // local (0..1) starting value
	public float[] paramend = new float[2]; // local (0..1) ending value
	public int paramparent;

	public float fadeintime; // ideal cross fate in time (0.2 default)
	public float fadeouttime; // ideal cross fade out time (0.2 default)

	public int localentrynode; // transition node at entry
	public int localexitnode; // transition node at exit
	public int nodeflags; // transition rules

	public float entryphase; // used to match entry gait
	public float exitphase; // used to match exit gait

	public float lastframe; // frame that should generation EndOfSequence

	public int nextseq; // auto advancing sequences
	public int pose; // index of delta animation between end and nextseq

	public int numikrules;

	public int numautolayers; //
	public int autolayerindex;

	// mstudioautolayer_t*
	public long pAutolayer(int i) {
		//Assert( i >= 0 && i < numautolayers);
		return (this.lastRead() + autolayerindex) + i;
	};

	public int weightlistindex;

	// float *
	public long pBoneweight(int i) {
		return this.lastRead() + weightlistindex + i;
	};

	public float weight(int i) {
		return ((MemoryBuffer) this.source())._lastreadsrc.readFloat(pBoneweight(i));
	};

	// FIXME: make this 2D instead of 2x1D arrays
	public int posekeyindex;

	// float*
	public long pPoseKey(int iParam, int iAnim) {
		return this.lastRead() + posekeyindex + iParam * groupsize[0] + iAnim;
	}

	public float poseKey(int iParam, int iAnim) {
		return ((MemoryBuffer) this.source())._lastreadsrc.readFloat(pPoseKey(iParam, iAnim));
	}

	public int numiklocks;
	public int iklockindex;

	// mstudioiklock_t *
	public long pIKLock(int i) {
		//Assert( i >= 0 && i < numiklocks);
		return this.lastRead() + iklockindex + i;
	};

	// Key values
	public int keyvalueindex;
	public int keyvaluesize;

	// char*
	public long KeyValueText() {
		return keyvaluesize != 0 ? this.lastRead() + keyvalueindex : 0;
	}

	public int cycleposeindex; // index of pose parameter to use as cycle index

	public int activitymodifierindex;
	public int numactivitymodifiers;

	// mstudioactivitymodifier_t*
	public long pActivityModifier(int i) {
		//Assert( i >= 0 && i < numactivitymodifiers); 
		return activitymodifierindex != 0 ? this.lastRead() + activitymodifierindex + i : 0;
	};

	public int[] unused = new int[5]; // remove/add as appropriate (grow back to 8 ints on version change!)
	
	public long pSeqdesc(int i) {
		return 0;
	}

}
