package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.util.StringFormat;

public class user_regs_struct extends MemStruct {

	public final StructField r15 = new StructField(this, Long.BYTES);
	public final StructField r14 = new StructField(this, Long.BYTES);
	public final StructField r13 = new StructField(this, Long.BYTES);
	public final StructField r12 = new StructField(this, Long.BYTES);
	public final StructField rbp = new StructField(this, Long.BYTES);
	public final StructField rbx = new StructField(this, Long.BYTES);
	public final StructField r11 = new StructField(this, Long.BYTES);
	public final StructField r10 = new StructField(this, Long.BYTES);
	public final StructField r9 = new StructField(this, Long.BYTES);
	public final StructField r8 = new StructField(this, Long.BYTES);
	public final StructField rax = new StructField(this, Long.BYTES);
	public final StructField rcx = new StructField(this, Long.BYTES);
	public final StructField rdx = new StructField(this, Long.BYTES);
	public final StructField rsi = new StructField(this, Long.BYTES);
	public final StructField rdi = new StructField(this, Long.BYTES);
	public final StructField orig_rax = new StructField(this, Long.BYTES);
	public final StructField rip = new StructField(this, Long.BYTES);
	public final StructField cs = new StructField(this, Long.BYTES);
	public final StructField eflags = new StructField(this, Long.BYTES);
	public final StructField rsp = new StructField(this, Long.BYTES);
	public final StructField ss = new StructField(this, Long.BYTES);
	public final StructField fs_base = new StructField(this, Long.BYTES);
	public final StructField gs_base = new StructField(this, Long.BYTES);
	public final StructField ds = new StructField(this, Long.BYTES);
	public final StructField es = new StructField(this, Long.BYTES);
	public final StructField fs = new StructField(this, Long.BYTES);
	public final StructField gs = new StructField(this, Long.BYTES);

	@Override
	public String toString() {
		return "user_regs_struct [" + "r15=" + r15.getLong() + ", r14=" + r14.getLong() + ", r13=" + r13.getLong() + ", r12=" + r12.getLong() + ", r11=" + r11.getLong() + ", r10=" + r10.getLong() + ", r9=" + r9.getLong() + ", r8=" + r8.getLong() + ", \nrbp=" + StringFormat.hex(rbp.getLong()) + ", rax=" + StringFormat.hex(rax.getLong()) + ", rbx=" + StringFormat.hex(rbx.getLong()) + ", rcx=" + StringFormat.hex(rcx.getLong()) + ", rdx=" + StringFormat.hex(rdx.getLong()) + ", rsi=" + StringFormat.hex(rsi.getLong()) + ", rdi=" + StringFormat.hex(rdi.getLong()) + ", \norig_rax=" + orig_rax.getLong() + ", rip=" + StringFormat.hex(rip.getLong()) + ", cs=" + cs.getLong() + ", eflags=" + eflags.getLong() + ", rsp=" + rsp.getLong() + ", ss=" + ss.getLong() + ", fs_base=" + fs_base.getLong() + ", gs_base=" + gs_base.getLong() + ", ds=" + ds.getLong() + ", es=" + es.getLong() + ", fs=" + fs.getLong() + ", gs=" + gs.getLong() + " ]";
	}
}
