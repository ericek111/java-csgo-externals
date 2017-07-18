package me.lixko.csgoexternals.structs;

public class CGlobalVars extends MemStruct {

	public final StructField realtime = new StructField(this, Float.BYTES);
	public final StructField framecount = new StructField(this, Integer.BYTES);
	public final StructField absoluteframetime = new StructField(this, Float.BYTES);
	public final StructField absoluteframestarttimestddev = new StructField(this, Float.BYTES);
	public final StructField curtime = new StructField(this, Float.BYTES);
	public final StructField frametime = new StructField(this, Float.BYTES);
	public final StructField tickcount = new StructField(this, Integer.BYTES);
	public final StructField maxClients = new StructField(this, Integer.BYTES);
	public final StructField interval_per_tick = new StructField(this, Float.BYTES);
	public final StructField interpolation_amount = new StructField(this, Float.BYTES);
	public final StructField simTicksThisFrame = new StructField(this, Integer.BYTES);
	public final StructField network_protocol = new StructField(this, Integer.BYTES);
	public final StructField pSaveData = new StructField(this, Long.BYTES);
	public final StructField m_bClient = new StructField(this, 1);
	public final StructField m_bRemoteClient = new StructField(this, 1);

}