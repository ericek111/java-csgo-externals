package me.lixko.csgoexternals.structs;

import com.github.jonatino.misc.MemoryBuffer;

public class GlowObjectDefinition extends MemStruct {

	public final StructField m_pEntity = new StructField(this, Long.BYTES);
	public final StructField m_flGlowRed = new StructField(this, Float.BYTES);
	public final StructField m_flGlowGreen = new StructField(this, Float.BYTES);
	public final StructField m_flGlowBlue = new StructField(this, Float.BYTES);
	public final StructField m_flGlowAlpha = new StructField(this, Float.BYTES);
	public final StructField unk0 = new StructField(this, 8);
	public final StructField m_flBloomAmount = new StructField(this, Float.BYTES);
	public final StructField localplayeriszeropoint3 = new StructField(this, Float.BYTES);
	public final StructField m_bRenderWhenOccluded = new StructField(this, 1);
	public final StructField m_bRenderWhenUnoccluded = new StructField(this, 1);
	public final StructField m_bFullBloomRender = new StructField(this, 1);
	public final StructField unk2 = new StructField(this, 1);
	public final StructField m_nGlowStyle = new StructField(this, Integer.BYTES);
	public final StructField iUnk = new StructField(this, Integer.BYTES);
	public final StructField m_nSplitScreenSlot = new StructField(this, Integer.BYTES);
	public final StructField m_nNextFreeSlot = new StructField(this, Integer.BYTES);
	public final StructField unk3 = new StructField(this, 4);

	static final int END_OF_FREE_LIST = -1;
	static final int ENTRY_IN_USE = -2;

	public GlowObjectDefinition(MemoryBuffer membuf, long offset) {
		super(membuf, offset);
	}

	public GlowObjectDefinition(MemoryBuffer membuf, int offset) {
		super(membuf, offset);
	}

	public GlowObjectDefinition() {
		super();
	}

	boolean ShouldDraw(int nSlot) {
		return m_pEntity.get().getLong() > 0 && (m_nSplitScreenSlot.get().getInt() == -1 || m_nSplitScreenSlot.get().getInt() == nSlot) && (m_bRenderWhenOccluded.get().get() > 0 || m_bRenderWhenUnoccluded.get().get() > 0);
	}

	boolean IsUnused() {
		return m_nNextFreeSlot.get().getInt() != ENTRY_IN_USE;
	}

	public long writeStart() {
		return m_flGlowRed.offset();
	}

	public long writeEnd() {
		return unk2.offset();
	}

}
