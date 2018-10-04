package me.lixko.csgoexternals.elf;

import me.lixko.csgoexternals.elf.Elf;
import me.lixko.csgoexternals.elf.Elf.*;
import me.lixko.csgoexternals.util.StringFormat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.process.Module;

public class ElfModule extends Module {

	Elf64_Ehdr ehdr = new Elf64_Ehdr();
	Elf64_Phdr[] phdrs = new Elf64_Phdr[PT_TYPES.values().length];
	HashMap<Long, Long> dyns = new HashMap<>();
	HashMap<String, Elf64_Sym> syms = new HashMap<>();
	MemoryBuffer strbuf;
	HashMap<String, GameInterface> interfaces = new HashMap<>();

	public ElfModule(Module module) {
		// Process process, String name, Pointer pointer, long size, String permissions
		super(module.process(), module.name(), module.pointer(), module.size(), module.permissions());
		this.init();
	}

	private void parseEhdr() {
		ehdr.readFrom(this, this.start());
	}

	private void parsePhdrs() {
		for (int i = 0; i < ehdr.e_phnum; i++) {
			Elf64_Phdr phdr = new Elf64_Phdr();
			if (phdr.size() != ehdr.e_phentsize)
				throw new IllegalStateException("Invalid Elf64_Phdr size [" + i + "]: " + phdr.size() + " instead of " + ehdr.e_phentsize);

			phdr.readFrom(this, this.start() + ehdr.e_phoff + i * ehdr.e_phentsize);
			if (phdr.p_type >= phdrs.length)
				continue;
			phdrs[phdr.p_type] = phdr;
		}
	}

	private void parseDyns() {
		Elf64_Dyn inst = new Elf64_Dyn();
		int count = (int) (phdrs[Elf.PT_DYNAMIC].p_memsz / (inst.size()));
		MemoryBuffer buf = new MemoryBuffer((int) phdrs[Elf.PT_DYNAMIC].p_memsz);

		this.read(this.start() + phdrs[Elf.PT_DYNAMIC].p_vaddr, buf);
		for (int i = 0; i < count; i++) {
			inst.readFrom(buf, inst.size() * i);
			if (inst.d_tag == 0)
				break;
			dyns.put(inst.d_tag, inst.d_un);
		}
		buf.free();
	}

	private void parseStringBuf(boolean arr) {
		strbuf = new MemoryBuffer(dyns.get((long) Elf.DT_STRSZ).intValue());
		this.read(dyns.get((long) Elf.DT_STRTAB).longValue(), strbuf);
		
		if (!arr)
			return;
		
		ArrayList<String> strings = new ArrayList<>();
		ByteBuffer bytebuf = strbuf.getByteBuffer(0, strbuf.size());
		int lastnull = 0;
		while (bytebuf.hasRemaining()) {
			if (bytebuf.get() != '\0')
				continue;
			byte[] sbuf = new byte[bytebuf.position() - lastnull];
			bytebuf.position(lastnull);
			bytebuf.get(sbuf);
			lastnull = bytebuf.position();
			String str = new String(sbuf);
			strings.add(str);
		}
	}

	private void parseSyms() {
		Elf64_Sym inst = new Elf64_Sym();

		for (int i = 2; true; i++) {
			Elf64_Sym sym = new Elf64_Sym();
			sym.readFrom(this, dyns.get((long) Elf.DT_SYMTAB).longValue() + inst.size() * i);
			if (sym.st_name > strbuf.size())
				break;
			syms.put(strbuf.getString(sym.st_name), sym);
		}
	}
	
	private void parseInterfaces() {
		long cur_interface = this.getSymbol("s_pInterfaceRegs").st_value + this.start();
		cur_interface = this.readLong(cur_interface);
		do {
			try {
				GameInterface gif = new GameInterface(this, cur_interface);
				this.interfaces.put(gif.getName(), gif);
			} catch (Exception ex) {
				String ifname = "";
				try {
					ifname = this.readString(this.readLong(cur_interface + 8), 256);
				} catch (Exception e) {
					System.err.println("[UNKNOWN]");
				}
				System.err.println("Failed to initialize interface " + ifname + " @ " + StringFormat.hex(cur_interface) + " (" + this.name() + "+" + StringFormat.hex(cur_interface - this.start()) + ")");
			}
			
			cur_interface = this.readLong(cur_interface + 0x10);
		} while (cur_interface != 0);
	}
	
	public void parseAll() {
		this.parseEhdr();
		this.parsePhdrs();
		this.parseDyns();
		this.parseStringBuf(false);
		this.parseSyms();
		this.parseInterfaces();
	}
	
	public void init() {
		this.parseAll();
	}

	public Elf64_Ehdr getEhdr() {
		return this.ehdr;
	}

	public Elf64_Phdr[] getPhdrs() {
		return this.phdrs;
	}
	
	public HashMap<Long, Long> getDyns() {
		return this.dyns;
	}
	
	public HashMap<String, Elf64_Sym> getSyms() {
		return this.syms;
	}
	
	public HashMap<String, GameInterface> getInterfaces() {
		return this.interfaces;
	}
	
	public GameInterface getInterface(String name) {
		return this.interfaces.get(name);
	}
	
	public GameInterface getInterfacePartially(String prefix) {
		return this.getInterfacePartially(prefix, false);
	}
	
	public GameInterface getInterfacePartially(String prefix, boolean numberAfterPrefix) {
		for (Map.Entry<String, GameInterface> entry : this.interfaces.entrySet()) {
			if (entry.getKey().startsWith(prefix)) {
				if (numberAfterPrefix) {
					char afterchar = entry.getKey().charAt(prefix.length());
					if (afterchar >= '0' && afterchar <= '9')
						return entry.getValue();
				} else {
					return entry.getValue();
				}
			}
		}
		return null;
	}
		
	public MemoryBuffer getStringBuffer() {
		return this.strbuf;
	}
	
	public Elf64_Sym getSymbol(String symname) {
		return this.syms.get(symname);
	}

}
