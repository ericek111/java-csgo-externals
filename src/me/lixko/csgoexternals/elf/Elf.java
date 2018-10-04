package me.lixko.csgoexternals.elf;

import me.lixko.csgoexternals.util.BufferStruct;

public class Elf {

	public static class Elf32_Dyn extends BufferStruct {
		public int d_tag; /* entry tag value */
		/*
		 * union {
		 * public long d_val;
		 * Elf64_ Addr d_ptr;
		 * } d_un;
		 */
		public int d_un;
	}

	public static class Elf64_Dyn extends BufferStruct {
		public long d_tag; /* entry tag value */
		/*
		 * union {
		 * public long d_val;
		 * Elf64_ Addr d_ptr;
		 * } d_un;
		 */
		public long d_un;
	}

	public static class Elf32_Rel extends BufferStruct {
		public int r_offset;
		public int r_info;
	}

	public static class Elf64_Rel extends BufferStruct {
		public long r_offset; /* Location at which to apply the action */
		public long r_info; /* index and type of relocation */
	}

	public static class Elf32_Rela extends BufferStruct {
		public int r_offset;
		public int r_info;
		public int r_addend;
	}

	public static class Elf64_Rela extends BufferStruct {
		public long r_offset; /* Location at which to apply the action */
		public long r_info; /* index and type of relocation */
		public long r_addend; /* Constant addend used to compute value */
	}

	public static class Elf32_Sym extends BufferStruct {
		public int st_name;
		public int st_value;
		public int st_size;
		public byte st_info;
		public byte st_other;
		public short st_shndx;
	}

	public static class Elf64_Sym extends BufferStruct {
		public int st_name; /* Symbol name, index in string tbl */
		public byte st_info; /* Type and binding attributes */
		public byte st_other; /* No defined meaning, 0 */
		public short st_shndx; /* Associated section index */
		public long st_value; /* Value of the symbol */
		public long st_size; /* Associated symbol size */
	}

	public static class Elf32_Ehdr extends BufferStruct {
		public byte[] e_ident = new byte[EI_NIDENT];
		public short e_type;
		public short e_machine;
		public int e_version;
		public int e_entry; /* Entry point */
		public int e_phoff;
		public int e_shoff;
		public int e_flags;
		public short e_ehsize;
		public short e_phentsize;
		public short e_phnum;
		public short e_shentsize;
		public short e_shnum;
		public short e_shstrndx;
	}

	public static class Elf64_Ehdr extends BufferStruct {
		public byte[] e_ident = new byte[EI_NIDENT]; /* ELF "magic number" */
		public short e_type;
		public short e_machine;
		public int e_version;
		public long e_entry; /* Entry point virtual address */
		public long e_phoff; /* Program header table file offset */
		public long e_shoff; /* Section header table file offset */
		public int e_flags;
		public short e_ehsize;
		public short e_phentsize;
		public short e_phnum;
		public short e_shentsize;
		public short e_shnum;
		public short e_shstrndx;
	}

	public static class Elf32_Phdr extends BufferStruct {
		public int p_type;
		public int p_offset;
		public int p_vaddr;
		public int p_paddr;
		public int p_filesz;
		public int p_memsz;
		public int p_flags;
		public int p_align;
	}

	public static class Elf64_Phdr extends BufferStruct {
		public int p_type;
		public int p_flags;
		public long p_offset; /* Segment file offset */
		public long p_vaddr; /* Segment virtual address */
		public long p_paddr; /* Segment physical address */
		public long p_filesz; /* Segment size in file */
		public long p_memsz; /* Segment size in memory */
		public long p_align; /* Segment alignment, file & memory */
	}

	public static class Elf32_Shdr extends BufferStruct {
		public int sh_name;
		public int sh_type;
		public int sh_flags;
		public int sh_addr;
		public int sh_offset;
		public int sh_size;
		public int sh_link;
		public int sh_info;
		public int sh_addralign;
		public int sh_entsize;
	}

	public static class Elf64_Shdr extends BufferStruct {
		public int sh_name; /* Section name, index in string tbl */
		public int sh_type; /* Type of section */
		public long sh_flags; /* Miscellaneous section attributes */
		public long sh_addr; /* Section virtual addr at execution */
		public long sh_offset; /* Section file offset */
		public long sh_size; /* Size of section in bytes */
		public int sh_link; /* Index of another section */
		public int sh_info; /* Additional section information */
		public long sh_addralign; /* Section alignment */
		public long sh_entsize; /* Entry size if section holds table */
	}

	public static class Elf32_Nhdr extends BufferStruct {
		public int n_namesz; /* Name size */
		public int n_descsz; /* Content size */
		public int n_type; /* Content type */
	}

	public static class Elf64_Nhdr extends BufferStruct {
		public int n_namesz; /* Name size */
		public int n_descsz; /* Content size */
		public int n_type; /* Content type */
	}

	/*
	 * Structure describing a loaded shared object. The `l_next' and `l_prev'
	 * members form a chain of all the shared objects loaded at startup.
	 * 
	 * These data structures exist in space used by the run-time dynamic linker;
	 * modifying them may have disastrous results.
	 */
	public static class link_map extends BufferStruct {
		public long l_addr; /* Difference between the address in the ELF file and the addresses in memory. */
		public long l_name; // *chat 	/* Absolute file name object was found in. */
		public long l_ld; // *Elf64_Dyn /* Dynamic section of the shared object. */
		public long l_next, l_prev; /* Chain of loaded objects. */
		// struct dynamic
	}

	/* These constants are for the segment types stored in the image headers */
	public static final int PT_NULL = 0;
	public static final int PT_LOAD = 1;
	public static final int PT_DYNAMIC = 2;
	public static final int PT_INTERP = 3;
	public static final int PT_NOTE = 4;
	public static final int PT_SHLIB = 5;
	public static final int PT_PHDR = 6;
	public static final int PT_TLS = 7 /* Thread local storage segment */;
	public static enum PT_TYPES {
		PT_NULL, PT_LOAD, PT_DYNAMIC, PT_INTERP, PT_NOTE, PT_SHLIB, PT_PHDR, PT_TLS
	}
	
	public static final int PT_LOOS = 0x60000000 /* OS-specific */;
	public static final int PT_HIOS = 0x6fffffff /* OS-specific */;
	public static final int PT_LOPROC = 0x70000000;
	public static final int PT_HIPROC = 0x7fffffff;
	public static final int PT_GNU_EH_FRAME = 0x6474e550;

	public static final int PT_GNU_STACK = (PT_LOOS + 0x474e551);

	/*
	 * Extended Numbering
	 *
	 * If the real number of program header table entries is larger than
	 * or equal to PN_XNUM(0xffff), it is set to sh_info field of the
	 * section header at index 0, and PN_XNUM is set to e_phnum
	 * field. Otherwise, the section header at index 0 is zero
	 * initialized, if it exists.
	 *
	 * Specifications are available in:
	 *
	 * - Oracle: Linker and Libraries.
	 * Part No: 817–1984–19, August 2011.
	 * http://docs.oracle.com/cd/E18752_01/pdf/817-1984.pdf
	 *
	 * - System V ABI AMD64 Architecture Processor Supplement
	 * Draft Version 0.99.4,
	 * January 13, 2010.
	 * http://www.cs.washington.edu/education/courses/cse351/12wi/supp-docs/abi.pdf
	 */
	public static final int PN_XNUM = 0xffff;

	/* These constants define the different elf file types */
	public static final int ET_NONE = 0;
	public static final int ET_REL = 1;
	public static final int ET_EXEC = 2;
	public static final int ET_DYN = 3;
	public static final int ET_CORE = 4;
	public static final int ET_LOPROC = 0xff00;
	public static final int ET_HIPROC = 0xffff;

	/* This is the info that is needed to parse the dynamic section of the file */
	public static final int DT_NULL = 0;
	public static final int DT_NEEDED = 1;
	public static final int DT_PLTRELSZ = 2;
	public static final int DT_PLTGOT = 3;
	public static final int DT_HASH = 4;
	public static final int DT_STRTAB = 5;
	public static final int DT_SYMTAB = 6;
	public static final int DT_RELA = 7;
	public static final int DT_RELASZ = 8;
	public static final int DT_RELAENT = 9;
	public static final int DT_STRSZ = 10;
	public static final int DT_SYMENT = 11;
	public static final int DT_INIT = 12;
	public static final int DT_FINI = 13;
	public static final int DT_SONAME = 14;
	public static final int DT_RPATH = 15;
	public static final int DT_SYMBOLIC = 16;
	public static final int DT_REL = 17;
	public static final int DT_RELSZ = 18;
	public static final int DT_RELENT = 19;
	public static final int DT_PLTREL = 20;
	public static final int DT_DEBUG = 21;
	public static final int DT_TEXTREL = 22;
	public static final int DT_JMPREL = 23;
	public static final int DT_ENCODING = 32;
	public static final int OLD_DT_LOOS = 0x60000000;
	public static final int DT_LOOS = 0x6000000d;
	public static final int DT_HIOS = 0x6ffff000;
	public static final int DT_VALRNGLO = 0x6ffffd00;
	public static final int DT_VALRNGHI = 0x6ffffdff;
	public static final int DT_ADDRRNGLO = 0x6ffffe00;
	public static final int DT_ADDRRNGHI = 0x6ffffeff;
	public static final int DT_VERSYM = 0x6ffffff0;
	public static final int DT_RELACOUNT = 0x6ffffff9;
	public static final int DT_RELCOUNT = 0x6ffffffa;
	public static final int DT_FLAGS_1 = 0x6ffffffb;
	public static final int DT_VERDEF = 0x6ffffffc;
	public static final int DT_VERDEFNUM = 0x6ffffffd;
	public static final int DT_VERNEED = 0x6ffffffe;
	public static final int DT_VERNEEDNUM = 0x6fffffff;
	public static final int OLD_DT_HIOS = 0x6fffffff;
	public static final int DT_LOPROC = 0x70000000;
	public static final int DT_HIPROC = 0x7fffffff;

	/* This info is needed when parsing the symbol table */
	public static final int STB_LOCAL = 0;
	public static final int STB_GLOBAL = 1;
	public static final int STB_WEAK = 2;

	public static final int STT_NOTYPE = 0;
	public static final int STT_OBJECT = 1;
	public static final int STT_FUNC = 2;
	public static final int STT_SECTION = 3;
	public static final int STT_FILE = 4;
	public static final int STT_COMMON = 5;
	public static final int STT_TLS = 6;

	public static long ELF_ST_BIND(long x) {
		return x >> 4;
	}

	public static long ELF_ST_TYPE(long x) {
		return ((int) x) & 0xf;
	}

	public static int ELF32_ST_BIND(int x) {
		return (int) ELF_ST_BIND(x);
	}

	public static int ELF32_ST_TYPE(int x) {
		return (int) ELF_ST_TYPE(x);
	}

	public static long ELF64_ST_BIND(long x) {
		return ELF_ST_BIND(x);
	}

	public static long ELF64_ST_TYPE(long x) {
		return ELF_ST_TYPE(x);
	}

	/* The following are used with relocations */
	public static int ELF32_R_SYM(int x) {
		return x >> 8;
	}

	public static int ELF32_R_TYPE(int x) {
		return x & 0xff;
	}

	public static long ELF64_R_SYM(long x) {
		return x >> 32;
	}

	public static long ELF64_R_TYPE(long x) {
		return x & 0xffffffff;
	}

	public static final int EI_NIDENT = 16;

	/*
	 * These constants define the permissions on sections in the program
	 * header, p_flags.
	 */
	public static final int PF_R = 0x4;
	public static final int PF_W = 0x2;
	public static final int PF_X = 0x1;

	/* sh_type */
	public static final int SHT_NULL = 0;
	public static final int SHT_PROGBITS = 1;
	public static final int SHT_SYMTAB = 2;
	public static final int SHT_STRTAB = 3;
	public static final int SHT_RELA = 4;
	public static final int SHT_HASH = 5;
	public static final int SHT_DYNAMIC = 6;
	public static final int SHT_NOTE = 7;
	public static final int SHT_NOBITS = 8;
	public static final int SHT_REL = 9;
	public static final int SHT_SHLIB = 10;
	public static final int SHT_DYNSYM = 11;
	public static final int SHT_NUM = 12;
	public static final int SHT_LOPROC = 0x70000000;
	public static final int SHT_HIPROC = 0x7fffffff;
	public static final int SHT_LOUSER = 0x80000000;
	public static final int SHT_HIUSER = 0xffffffff;

	/* sh_flags */
	public static final int SHF_WRITE = 0x1;
	public static final int SHF_ALLOC = 0x2;
	public static final int SHF_EXECINSTR = 0x4;
	public static final int SHF_RELA_LIVEPATCH = 0x00100000;
	public static final int SHF_RO_AFTER_INIT = 0x00200000;
	public static final int SHF_MASKPROC = 0xf0000000;

	/* special section indexes */
	public static final int SHN_UNDEF = 0;
	public static final int SHN_LORESERVE = 0xff00;
	public static final int SHN_LOPROC = 0xff00;
	public static final int SHN_HIPROC = 0xff1f;
	public static final int SHN_LIVEPATCH = 0xff20;
	public static final int SHN_ABS = 0xfff1;
	public static final int SHN_COMMON = 0xfff2;
	public static final int SHN_HIRESERVE = 0xffff;

	public static final int EI_MAG0 = 0; /* e_ident[] indexes */
	public static final int EI_MAG1 = 1;
	public static final int EI_MAG2 = 2;
	public static final int EI_MAG3 = 3;
	public static final int EI_CLASS = 4;
	public static final int EI_DATA = 5;
	public static final int EI_VERSION = 6;
	public static final int EI_OSABI = 7;
	public static final int EI_PAD = 8;

	public static final int ELFMAG0 = 0x7f; /* EI_MAG */
	public static final int ELFMAG1 = 'E';
	public static final int ELFMAG2 = 'L';
	public static final int ELFMAG3 = 'F';
	public static final String ELFMAG = "\177ELF";
	public static final int SELFMAG = 4;

	public static final int ELFCLASSNONE = 0; /* EI_CLASS */
	public static final int ELFCLASS32 = 1;
	public static final int ELFCLASS64 = 2;
	public static final int ELFCLASSNUM = 3;

	public static final int ELFDATANONE = 0; /* e_ident[EI_DATA] */
	public static final int ELFDATA2LSB = 1;
	public static final int ELFDATA2MSB = 2;

	public static final int EV_NONE = 0; /* e_version, EI_VERSION */
	public static final int EV_CURRENT = 1;
	public static final int EV_NUM = 2;

	public static final int ELFOSABI_NONE = 0;
	public static final int ELFOSABI_LINUX = 3;

	/*
	 * Notes used in ET_CORE. Architectures export some of the arch register sets
	 * using the corresponding note types via the PTRACE_GETREGSET and
	 * PTRACE_SETREGSET requests.
	 */
	public static final int NT_PRSTATUS = 1;
	public static final int NT_PRFPREG = 2;
	public static final int NT_PRPSINFO = 3;
	public static final int NT_TASKSTRUCT = 4;
	public static final int NT_AUXV = 6;
	/*
	 * Note to userspace developers: size of NT_SIGINFO note may increase
	 * in the future to accomodate more fields, don't assume it is fixed!
	 */
	public static final int NT_SIGINFO = 0x53494749;
	public static final int NT_FILE = 0x46494c45;
	public static final int NT_PRXFPREG = 0x46e62b7f; /* copied from gdb5;.1/include/elf/common.h */
	public static final int NT_PPC_VMX = 0x100; /* PowerPC Altivec/VMX registers */
	public static final int NT_PPC_SPE = 0x101; /* PowerPC SPE/EVR registers */
	public static final int NT_PPC_VSX = 0x102; /* PowerPC VSX registers */
	public static final int NT_PPC_TAR = 0x103; /* Target Address Register */
	public static final int NT_PPC_PPR = 0x104; /* Program Priority Register */
	public static final int NT_PPC_DSCR = 0x105; /* Data Stream Control Register */
	public static final int NT_PPC_EBB = 0x106; /* Event Based Branch Registers */
	public static final int NT_PPC_PMU = 0x107; /* Performance Monitor Registers */
	public static final int NT_PPC_TM_CGPR = 0x108; /* TM checkpointed GPR Registers */
	public static final int NT_PPC_TM_CFPR = 0x109; /* TM checkpointed FPR Registers */
	public static final int NT_PPC_TM_CVMX = 0x10a; /* TM checkpointed VMX Registers */
	public static final int NT_PPC_TM_CVSX = 0x10b; /* TM checkpointed VSX Registers */
	public static final int NT_PPC_TM_SPR = 0x10c; /* TM Special Purpose Registers */
	public static final int NT_PPC_TM_CTAR = 0x10d; /* TM checkpointed Target Address Register */
	public static final int NT_PPC_TM_CPPR = 0x10e; /* TM checkpointed Program Priority Register */
	public static final int NT_PPC_TM_CDSCR = 0x10f; /* TM checkpointed Data Stream Control Register */
	public static final int NT_386_TLS = 0x200; /* i386 TLS slots (struct user_desc) */
	public static final int NT_386_IOPERM = 0x201; /* x86 io permission bitmap (1=deny) */
	public static final int NT_X86_XSTATE = 0x202; /* x86 extended state using xsave */
	public static final int NT_S390_HIGH_GPRS = 0x300; /* s390 upper register halves */
	public static final int NT_S390_TIMER = 0x301; /* s390 timer register */
	public static final int NT_S390_TODCMP = 0x302; /* s390 TOD clock comparator register */
	public static final int NT_S390_TODPREG = 0x303; /* s390 TOD programmable register */
	public static final int NT_S390_CTRS = 0x304; /* s390 control registers */
	public static final int NT_S390_PREFIX = 0x305; /* s390 prefix register */
	public static final int NT_S390_LAST_BREAK = 0x306; /* s390 breaking event address */
	public static final int NT_S390_SYSTEM_CALL = 0x307; /* s390 system call restart data */
	public static final int NT_S390_TDB = 0x308; /* s390 transaction diagnostic block */
	public static final int NT_S390_VXRS_LOW = 0x309; /* s390 vector registers 0-15 upper half */
	public static final int NT_S390_VXRS_HIGH = 0x30a; /* s390 vector registers 16-31 */
	public static final int NT_S390_GS_CB = 0x30b; /* s390 guarded storage registers */
	public static final int NT_S390_GS_BC = 0x30c; /* s390 guarded storage broadcast control block */
	public static final int NT_S390_RI_CB = 0x30d; /* s390 runtime instrumentation */
	public static final int NT_ARM_VFP = 0x400; /* ARM VFP/NEON registers */
	public static final int NT_ARM_TLS = 0x401; /* ARM TLS register */
	public static final int NT_ARM_HW_BREAK = 0x402; /* ARM hardware breakpoint registers */
	public static final int NT_ARM_HW_WATCH = 0x403; /* ARM hardware watchpoint registers */
	public static final int NT_ARM_SYSTEM_CALL = 0x404; /* ARM system call number */
	public static final int NT_ARM_SVE = 0x405; /* ARM Scalable Vector Extension registers */
	public static final int NT_METAG_CBUF = 0x500; /* Metag catch buffer registers */
	public static final int NT_METAG_RPIPE = 0x501; /* Metag read pipeline state */
	public static final int NT_METAG_TLS = 0x502; /* Metag TLS pointer */
	public static final int NT_ARC_V2 = 0x600; /* ARCv2 accumulator/extra registers */

}
