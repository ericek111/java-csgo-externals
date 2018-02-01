package me.lixko.csgoexternals.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.unix.ptrace;
import com.github.jonatino.natives.unix.unix;
import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.github.jonatino.process.Processes;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import me.lixko.csgoexternals.offsets.PatternScanner;
import me.lixko.csgoexternals.structs.user_regs_struct;

public class Injector {

	String processname = "dummy";
	String mmapPayload = "/home/erik/Dokumenty/Java/linux-csgo-externals/res/docs/linux-injector/mmap64.bin";
	String clonePayload = "/home/erik/Dokumenty/Java/linux-csgo-externals/res/docs/linux-injector/clone64.bin";
	String injectedPayload = "/home/erik/Dokumenty/Java/linux-csgo-externals/res/docs/linux-injector/mine4.bin";
	int archWidth = 64;
	int MMAP_PROTS = UnixDefs.mmap.PROT_READ | UnixDefs.mmap.PROT_WRITE | UnixDefs.mmap.PROT_EXEC;
	int MMAP_FLAGS = UnixDefs.mmap.MAP_PRIVATE | UnixDefs.mmap.MAP_ANONYMOUS;
	int CLONE_FLAGS = UnixDefs.sched.CLONE_THREAD | UnixDefs.sched.CLONE_SIGHAND | UnixDefs.sched.CLONE_UNTRACED | UnixDefs.sched.CLONE_VM;
	int STACK_SIZE = 0x1000;
	
	user_regs_struct regsobj = new user_regs_struct();
	
	public void doStuff() throws IOException {

		ProfilerUtil.start();
		user_regs_struct cregs = new user_regs_struct();
		MemoryBuffer cregsbuf = new MemoryBuffer(cregs.size());
		cregs.setSource(cregsbuf);

		user_regs_struct bakregs = new user_regs_struct();
		MemoryBuffer bakregsbuf = new MemoryBuffer(bakregs.size());
		bakregs.setSource(bakregsbuf);

		IntByReference status = new IntByReference();
		Process exampleproc = Processes.byName(processname);
		int pid = exampleproc.id();
		Module examplemod = exampleproc.findModule(processname);

		// load our shellcode
		File shellcodebin = new File("/home/erik/Dokumenty/Java/linux-csgo-externals/res/docs/linux-injector/print64.bin");
		int shellcode_size = (int) shellcodebin.length();

		// Make it aligned on 8 bytes boundary
		shellcode_size &= ~0x07;
		shellcode_size += 0x08;
		MemoryBuffer shellcodebuf = new MemoryBuffer(shellcode_size);
		MemoryBuffer backupbuf = new MemoryBuffer(128);
		shellcodebuf.setBytes(Files.readAllBytes(shellcodebin.toPath()));
		ProfilerUtil.measure("Loaded shellcode (" + shellcodebuf.size() + " B).");

		// attach to the process
		ptrace.attach(pid);
		ProfilerUtil.measure("Attached.");

		// wait for the process to stop, otherwise we may kill the process
		if (wait_stopped(pid) == 0) {
			ProfilerUtil.measure("Failed to wait until target process stopped!");
			return;
		}
		ProfilerUtil.measure("Process stopped is in stopped state!");

		//  Wait until process has just returned from a system call before proceeding
		ptrace_next_syscall(pid);
		ProfilerUtil.measure("Process exited from syscall!");

		// save state
		//System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, bakregs.rip.offset())));
		ptrace.getregs(pid, Pointer.nativeValue(bakregsbuf));
		ProfilerUtil.measure("Saved registers.");
		ptrace.read(pid, bakregs.rip.getLong(), backupbuf);
		ProfilerUtil.measure("Saved " + backupbuf.size() + " bytes from RIP " + StringFormat.hex(bakregs.rip.getLong()));

		// allocate space and copy the payload
		long payload_addr = mmap(pid, shellcode_size, 0, 0, 0);
		ProfilerUtil.measure("Allocated " + shellcode_size + " B at " + StringFormat.hex(payload_addr));
		//ptrace.cont(pid);
		//ptrace.detach(pid);
		//System.exit(0);
		ptrace.write(pid, payload_addr, shellcodebuf);

		System.out.println("Allocating new stack...");
		// allocate new stack
		long stack = mmap(pid, STACK_SIZE, 0, 0, 0);
		stack += STACK_SIZE; // stack grows downwards, use the top address

		// allocate space for code cave
		long code_cave = mmap(pid, 128, 0, 0, 0);

		// launch the payload
		launch(pid, code_cave, 128, stack, payload_addr, shellcode_size, 0, 0);

		// restores process state
		ptrace.setregs(pid, Pointer.nativeValue(bakregsbuf));
		ptrace.write(pid, payload_addr, backupbuf);
		ptrace.cont(pid);
	}

	public long mmap(int pid, int len, long base_address, int protections, int flags) throws IOException {
		ProfilerUtil.measure("mmap: ");

		IntByReference status = new IntByReference();

		user_regs_struct cregs = new user_regs_struct();
		MemoryBuffer cregsbuf = new MemoryBuffer(cregs.size());
		cregs.setSource(cregsbuf);

		user_regs_struct origregs = new user_regs_struct();
		MemoryBuffer origregsbuf = new MemoryBuffer(cregs.size());
		origregs.setSource(origregsbuf);
		
		ptrace.getregs(pid, Pointer.nativeValue(origregsbuf));
		cregsbuf.setBytes(origregsbuf);
		
		System.out.println(StringFormat.dumpObj(cregs));
		
		// put arguments in the proper registers
		if (archWidth == 32) {
		} else {
			cregs.rdi.set(base_address);
			cregs.rsi.set(len);
			cregs.rdx.set(protections > 0 ? protections : MMAP_PROTS);
			cregs.r10.set(flags > 0 ? flags : MMAP_FLAGS);
		}

		File mmapbin = new File(mmapPayload);
		int mmap_size = (int) mmapbin.length();

		// Make it aligned on 8 bytes boundary
		mmap_size &= ~0x07;
		mmap_size += 0x08;
		MemoryBuffer mmapbuf = new MemoryBuffer(mmap_size);

		for (int i = 0; i < mmapbuf.size(); i++) {
			mmapbuf.setByte(i, (byte) 0x90);
		}

		mmapbuf.setBytes(Files.readAllBytes(mmapbin.toPath()));
		System.out.println("Loaded shellcode (" + mmapbuf.size() + " B) @ " + StringFormat.hex(cregs.rip.getLong()));
		System.out.println(StringFormat.hex(Files.readAllBytes(mmapbin.toPath())));
		System.out.println(StringFormat.hex(mmapbuf.array()));
		
		// write mmap code to target process instruction pointer
		ptrace.setregs(pid, Pointer.nativeValue(cregsbuf));
		ptrace.write(pid, cregs.rip.getLong(), mmapbuf);
		ptrace.cont(pid);

		// run mmap code and check return value
		wait_trap(pid);
		//unix.waitpid(pid, status, 0);

		// get return value from mmap()
		ptrace.getregs(pid, Pointer.nativeValue(cregsbuf));
		long out = cregs.rax.getLong();
		if (out == -1) {
			System.err.println("Failed to mmap()!");
			return 0;
		}
		System.out.println("mmap() returned " + out);
		
		System.out.println(StringFormat.dumpObj(cregs));

		// restore registers
		ptrace.setregs(pid, Pointer.nativeValue(origregsbuf));

		return out;
	}

	public void launch(int pid, long code_cave, int code_cave_size, long stack_address, long payload_address, int payload_length, long payload_param, int flags) throws IOException {
		ProfilerUtil.measure("mmap: ");

		IntByReference status = new IntByReference();
		user_regs_struct cregs = new user_regs_struct();
		MemoryBuffer cregsbuf = new MemoryBuffer(cregs.size());
		cregs.setSource(cregsbuf);
		user_regs_struct origregs = new user_regs_struct();
		MemoryBuffer origregsbuf = new MemoryBuffer(cregs.size());
		origregs.setSource(origregsbuf);

		ptrace.getregs(pid, Pointer.nativeValue(origregsbuf));
		cregsbuf.setBytes(origregsbuf);

		// put arguments in the proper registers
		if (archWidth == 32) {
		} else {
			cregs.rax.set(code_cave_size);
			cregs.rdi.set(flags > 0 ? flags : CLONE_FLAGS);
			cregs.rsi.set(stack_address);
			cregs.rdx.set(STACK_SIZE);
			cregs.rcx.set(payload_address);
			cregs.r8.set(payload_length);
			cregs.r9.set(payload_param);
			cregs.rip.set(code_cave);
		}

		File clonebin = new File(clonePayload);
		int clone_size = (int) clonebin.length();

		// Make it aligned on 8 bytes boundary
		clone_size &= ~0x07;
		clone_size += 0x08;
		MemoryBuffer clonebuf = new MemoryBuffer(clone_size);

		for (int i = 0; i < clonebuf.size(); i++) {
			clonebuf.setByte(i, (byte) 0x90);
		}

		clonebuf.setBytes(Files.readAllBytes(clonebin.toPath()));
		ProfilerUtil.measure("Loaded clone() shellcode (" + clonebuf.size() + " B).");

		// write mmap code to target process instruction pointer
		ptrace.setregs(pid, Pointer.nativeValue(cregsbuf));
		ptrace.write(pid, code_cave, clonebuf);
		ptrace.cont(pid);

		// run mmap code and check return value
		unix.waitpid(pid, status, 0);

		// get return value from mmap()
		ptrace.getregs(pid, Pointer.nativeValue(cregsbuf));
		long out = cregs.rax.getLong();
		if (out == -1) {
			System.err.println("Failed to clone()!");
			return;
		}
		System.out.println("mmap() returned " + out);

		// restore registers
		ptrace.setregs(pid, Pointer.nativeValue(origregsbuf));
	}
	
	public void ptrace_next_syscall(int pid) {
		long rax;
		do {
			ptrace.syscall(pid);
			wait_stopped(pid);
			rax = ptrace.peekuser(pid, regsobj.rax.offset());
			ProfilerUtil.measure("RAX after syscall: " + StringFormat.hex(rax) + " / " + rax);
		} while(rax == -UnixDefs.errno.ENOSYS);
	}

	public int wait_trap(int pid) {
		IntByReference statusref = new IntByReference(0);
		while (true) {
			if (unix.waitpid(pid, statusref, 0) == -1) {
				System.err.println("waitpid error!");
				return 0;
			}
			int status = statusref.getValue();

			pstateinfo(status);

			// SIGTRAP = 5 - Trace/breakpoint trap
			if (unix.WIFSTOPPED(status) && unix.WSTOPSIG(status) == 5)
				return 1;
		}
	}

	public int wait_stopped(int pid) {
		IntByReference statusref = new IntByReference(1);
		while (true) {
			if (unix.waitpid(pid, statusref, 0) == -1) {
				System.err.println("waitpid error!");
				return 0;
			}
			int status = statusref.getValue();
			pstateinfo(status);

			if (unix.WIFSTOPPED(status))
				return 1;
		}
	}

	public void pstateinfo(int status) {
		if (unix.WIFSTOPPED(status))
			ProfilerUtil.measure("Process stopped with signal " + unix.WSTOPSIG(status));
		if (unix.WIFEXITED(status))
			ProfilerUtil.measure("Process exited with signal " + unix.WEXITSTATUS(status));
		if (unix.WIFSIGNALED(status)) {
			ProfilerUtil.measure("Process terminated with signal " + unix.WTERMSIG(status));
			if (unix.WCOREDUMP(status))
				ProfilerUtil.measure("Process core dumped!");
		}
		if (unix.WIFCONTINUED(status))
			ProfilerUtil.measure("Process was resumed by delivery of SIGCONT");
		if (unix.WIFEXITED(status))
			ProfilerUtil.measure("Target process has exited!");
	}
}
