package me.lixko.csgoexternals.elf;

import java.util.ArrayList;
import com.github.jonatino.process.Module;
import com.sun.jna.Pointer;

public class GameInterface {

	private Module module;
	private String ifname;
	private long ifregbase = 0;
	private long instantiateIfFn = 0;
	private long vftptr = 0;
	private long vftbase = 0;
	ArrayList<Long> funcs = new ArrayList<>();

	// https://github.com/ericek111/java-csgo-internals/blob/master/src/eu/lixko/csgoshared/offsets/GameInterface.java

	public GameInterface(Module module, long baseclass) {
		this.module = module;
		this.ifregbase = baseclass;
		this.init();
	}

	public void init() {
		long strptr = module.readLong(this.ifregbase + 8);
		this.ifname = module.readString(strptr, 256);
		this.instantiateIfFn = module.readLong(this.ifregbase);
		System.out.println(this.ifname + ": " + (module.readByte(this.instantiateIfFn) != 0x48));
		if (module.readByte(this.instantiateIfFn) != 0x48) {
			this.vftptr = this.instantiateIfFn + module.readInt(this.instantiateIfFn + 1 + 3) + 8;
		} else {
			this.vftptr = module.readLong(this.instantiateIfFn + module.readInt(this.instantiateIfFn + 3) + 7);
			//System.out.println(this.vftptr - module.GetAbsoluteAddress(this.instantiateIfFn, 3 ,7));
		}
		//this.vftptr = module.GetAbsoluteAddress(this.instantiateIfFn + (module.readByte(this.instantiateIfFn) != 0x48 ? 1 : 0), 3, 7);
		this.vftbase = module.readLong(this.vftptr);
		
		for (int i = 0;; i++) {
			long fptr = module.readLong(vftbase + Pointer.SIZE * i);
			if (fptr == 0)
				break;
			funcs.add(fptr);
		}
		System.out.println("Found " + funcs.size() + " fcs in " + this.ifname);
	}
	
	public String getName() {
		return this.ifname;
	}
	
	public Module getModule() {
		return this.module;
	}
	
	public long getInstantiateFn() {
		return this.instantiateIfFn;
	}
	
	public long getFunction(int index) {
		return this.funcs.get(index);
	}
	
	public long getVFTPointer() {
		return this.vftptr;
	}
	
	public long getVFTBase() {
		return this.vftbase;
	}
	
	public long getIfRegBase() {
		return this.ifregbase;
	}

}
