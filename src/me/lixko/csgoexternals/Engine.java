package me.lixko.csgoexternals;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Collections;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.unix.ptrace;
import com.github.jonatino.natives.unix.unix;
import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.github.jonatino.process.Processes;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.XTest;
import com.sun.jna.platform.unix.X11.XVisualInfo;
import com.sun.jna.platform.unix.X11.Xext;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.unix.X11.Atom;
import com.sun.jna.platform.unix.X11.Colormap;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.GC;
import com.sun.jna.platform.unix.X11.Pixmap;
import com.sun.jna.platform.unix.X11.Visual;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.XGCValues;
import com.sun.jna.platform.unix.X11.XSetWindowAttributes;

import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.offsets.PatternScanner;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.structs.ExampleBufStruct;
import me.lixko.csgoexternals.structs.user_regs_struct;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.Injector;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.ProfilerUtil;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.WMCtrl;
import me.lixko.csgoexternals.util.bsp.BSPParser;
import me.lixko.csgoexternals.util.bsp.BSPRenderer;
import me.lixko.csgoexternals.util.demo.DemoParser;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public final class Engine {

	private static Process process, localprocess = Processes.byId(MemoryUtils.getPID());
	private static Module clientModule, engineModule;

	private static final int TARGET_TPS = 200;
	private long tps_sleep = (long) ((1f / TARGET_TPS) * 1000);
	private long last_tick = 0;

	public static X11 x11 = X11.INSTANCE;
	public static XTest xtest = XTest.INSTANCE;
	public static Xext xext = Xext.INSTANCE;
	public static Window csgowin;

	public static ThreadLocal<Display> dpy = ThreadLocal.withInitial(() -> x11.XOpenDisplay(null));
	public static BSPRenderer bsp;
	
	public static MemoryBuffer entlistbuffer = new MemoryBuffer(Long.BYTES * 4 * 65);
	public static long tick = 0;
	public static int isInGame = 0;
	public static String[] cmdargs;

	public void init(String[] args) throws InterruptedException, IOException {
		this.cmdargs = args;
		/*localprocess = Processes.byName("csgo.exe");
		localprocess.initModules();
		Module clientdll = localprocess.findModule("client.dll");
		System.out.println(StringFormat.hex(clientdll.start()));
		long lpaddr = PatternScanner.getAddressForPattern(clientdll, "A3 ?? ?? ?? ?? C7 05 ?? ?? ?? ?? ?? ?? ?? ?? E8 ?? ?? ?? ?? 59 C3 6A ??");
		System.out.println(StringFormat.hex(lpaddr));
		System.out.println(StringFormat.hex(lpaddr - clientdll.start()));
		boolean xddd = true;
		while(xddd) {
			int health = clientdll.readInt(clientdll.start() + 0xA9ADEC + 0xFC);
			System.out.println(health);
			Thread.sleep(500);
		}*/
		
		boolean injector = false;
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-inj"))
				injector = true;
		}
		
		if (injector) {
			Injector inj = new Injector();
			inj.doStuff();
			System.exit(0);
		}
		if (injector) {

			ProfilerUtil.start();
			user_regs_struct cregs = new user_regs_struct();
			MemoryBuffer cregsbuf = new MemoryBuffer(cregs.size());
			cregs.setSource(cregsbuf);

			IntByReference status = new IntByReference();
			Process exampleproc = Processes.byName("example");
			if(exampleproc == null) System.exit(0);
			int pid = exampleproc.id();
			Module examplemod = exampleproc.findModule("example");

			File shellcodebin = new File("/home/erik/Dokumenty/Java/linux-csgo-externals/res/linux_thread_injection/thread_shellcode.bin");
			int shellcode_size = (int) shellcodebin.length();

			// Make it aligned on 8 bytes boundary
			shellcode_size &= ~0x07;
			shellcode_size += 0x08;
			MemoryBuffer shellcodebuf = new MemoryBuffer(shellcode_size);
			MemoryBuffer backupbuf = new MemoryBuffer(shellcode_size);
			shellcodebuf.setBytes(Files.readAllBytes(shellcodebin.toPath()));

			System.out.println("Loaded shellcode (" + shellcodebuf.size() + " B).\nAttaching...");

			ProfilerUtil.measure("Shellcode loaded.");

			ptrace.attach(pid);
			unix.waitpid(pid, status, 0);
			System.out.println("SIGTERM: " + StringFormat.hex(status.getValue()));

			ptrace.syscall(pid);

			// while(!unix.WIFSTOPPED(status.getValue())) { Thread.sleep(1); }
			// 89 7D FC 8B 05 F0 09 20 00 8B 55 FC 89 C6 BF 34
			// 07 40 00 B8 00 00 00 00 E8 48 FE FF FF 90 C9 C3
			long printcyka = PatternScanner.getAddressForPattern(examplemod, "89 7D FC 8B 05 F0 09 20 00 8B 55 FC 89 C6 BF 34 07 40 00 B8 00 00 00 00 E8 48 FE FF FF 90 C9 C3") + 32;
			System.out.println("Found printcyka: " + StringFormat.hex(printcyka));
			// byte callrax[] = new byte[] { (byte) 0x48, (byte)0xff, (byte)0xd0, (byte)0xCC }; // jmp rax; db 0xCC
			// byte callrax[] = new byte[] { (byte)0xff, (byte)0xe3, (byte)0xCC }; // jmp rax; db 0xCC

			byte callrax[] = new byte[] { (byte) 0x55, (byte) 0x48, (byte) 0x89, (byte) 0xE5, (byte) 0xBF, (byte) 0x46, (byte) 0x07, (byte) 0x40, (byte) 0x00, (byte) 0xE8, (byte) 0x17, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0x90, (byte) 0x5D, (byte) 0xC3, (byte) 0xCC };

			int buf_size = callrax.length;
			buf_size &= ~0x07;
			buf_size += 0x08;

			MemoryBuffer callraxbuf = new MemoryBuffer(buf_size);
			callraxbuf.setBytes(callrax);

			// 55 48 89 E5 BF 46 07 40 00 E8 17 FE FF FF 90 5D C3 = 17
			// 0x55, 0x48, 0x89, 0xE5, 0xBF, 0x46, 0x07, 0x40, 0x00, 0xE8, 0x17, 0xFE, 0xFF, 0xFF, 0x90, 0x5D, 0xC3 = 17 + 0xCC

			MemoryBuffer bkbuf = new MemoryBuffer(buf_size);
			// ptrace.getregs(pid, Pointer.nativeValue(cregsbuf));
			// System.out.println(cregs.toString());

			// System.out.println(StringFormat.hex(cregs.rip.getLong()) + " / " + StringFormat.hex(cregs.cs.getLong()) + " / " + StringFormat.hex(printcyka) + " / " + StringFormat.hex(examplemod.start()));
			// System.out.println(StringFormat.hex(ptrace.read_by_reg(pid, cregs.rip.offset())) + " ");
			// System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())));
			// ptrace.read(pid, cregs.rip.getLong(), bkbuf);
			// ptrace.write(pid, cregs.rip.getLong(), callrax);
			// ptrace.pokeuser(pid, cregs.rbx.offset(), printcyka);
			/*
			 * System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())));
			 * ptrace.detach(pid);
			 * System.exit(0);
			 */

			int step = 0;
			while (step < 1000) {
				if (-1 == unix.waitpid(pid, status, 0)) {
					System.out.println("An error has occured while waiting for pid " + pid + " (" + StringFormat.hex(status.getValue()) + ")");
				}
				if (unix.WIFEXITED(status.getValue())) {
					System.out.println("The victim program has exited..");
				}
				if (unix.WIFSTOPPED(status.getValue())) {
					if (0 == step && (1 == ptrace.peekuser(pid, cregs.orig_rax.offset()))) {
						ProfilerUtil.measure("Step 0");
						ptrace.syscall(pid);
						step++;
					} else if (step == 1) {
						System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())));
						ProfilerUtil.measure("Step 1");
						ptrace.getregs(pid, Pointer.nativeValue(cregsbuf));
						// System.out.println(cregs.toString());

						System.out.printf("Injecting %d bytes into the victim process.\n", shellcode_size);

						ptrace.read(pid, cregs.rip.getLong(), bkbuf);
						ptrace.write(pid, cregs.rip.getLong(), callraxbuf);
						// ptrace.pokeuser(pid, cregs.rbx.offset(), printcyka);
						System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())));
						ptrace.singlestep(pid);
						System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())));
						step = 3;
					} else if (step == 2) {
						if (0xCC == (ptrace.read_by_reg_R(pid, cregs.rip.offset()) & 0xFF)) {
							ProfilerUtil.measure("Step 2");
							System.out.printf("breakpoint: signal = %d\n", unix.WSTOPSIG(status.getValue()));
							// ptrace.pokeuser(pid, cregs.rip.offset(), ptrace.peekuser(pid, cregs.rip.offset()) + 1);
							System.out.println("Injected code has allocated memory at " + StringFormat.hex(ptrace.peekuser(pid, cregs.rax.offset())) + ".");
							System.out.println("Injected code is going to copy the remote thread procedure.");
							ptrace.singlestep(pid);
							step++;
						} else {
							ptrace.singlestep(pid);
							System.out.println("> RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())) + " = " + StringFormat.hex(ptrace.read_by_reg_R(pid, cregs.rip.offset())));

						}
					} else if (step == 3) {

						if (0xCC != (ptrace.read_by_reg_R(pid, cregs.rip.offset()) & 0xFF)) {
							System.out.print(".");
							ptrace.singlestep(pid);
							continue;
						}
						System.out.println();
						ProfilerUtil.measure("Step 3");

						ptrace.write(pid, cregs.rip.getLong(), bkbuf);
						ptrace.setregs(pid, Pointer.nativeValue(cregsbuf));
						ptrace.detach(pid);

						System.out.println("HOWGH!");

						System.exit(1);
					} else {
						ptrace.syscall(pid);
					}
				}
			}

			/*
			 * while(0xCC != (ptrace.read_by_reg(pid, cregs.rip.offset()) & 0xFF)) {
			 * //if(unix.WIFSTOPPED(status.getValue())) {
			 * System.out.println(StringFormat.hex((ptrace.read_by_reg(pid, cregs.rip.offset()) >> Long.BYTES*7) & 0xFF));
			 * System.out.println(">2 RIP: " + StringFormat.hex(ptrace.peekuser(pid, cregs.rip.offset())));
			 * ptrace.singlestep(pid);
			 * Thread.sleep(1000);
			 * //
			 * while(!unix.WIFSTOPPED(status.getValue())) Thread.sleep(10);
			 * }
			 * 
			 * ptrace.write(pid, cregs.rip.getLong(), bkbuf);
			 * ptrace.pokeuser(pid, cregs.rip.offset(), ptrace.peekuser(pid, cregs.rip.offset()) + 1);
			 * ptrace.detach(pid);
			 */

			// PatternScanner.getAddressForPattern(examplemod, "89 C7 E8 A6 FE FF FF 83 45 F8 01 8B 45 F8 89 C7 E8 55 00 00 00 84 C0 75 D4 B8 00 00 00 00 C9 C3");
			// examplemod.read(examplemod.start(), 4);

			ProfilerUtil.measure("Looping.");

			step = 0;
			while (step < 1000) {
				if (-1 == unix.waitpid(pid, status, 0)) {
					System.out.println("An error has occured while waiting for pid " + pid + " (" + StringFormat.hex(status.getValue()) + ")");
				}
				if (unix.WIFEXITED(status.getValue())) {
					System.out.println("The victim program has exited..");
				}
				if (unix.WIFSTOPPED(status.getValue())) {
					if (0 == step && (1 == ptrace.peekuser(pid, cregs.orig_rax.offset()))) {
						ProfilerUtil.measure("Step 0");
						ptrace.syscall(pid);
						step++;
					} else if (step == 1) {
						ProfilerUtil.measure("Step 1");
						ptrace.getregs(pid, Pointer.nativeValue(cregsbuf));
						// System.out.println(cregs.toString());

						System.out.printf("Injecting %d bytes into the victim process.", shellcode_size);

						ptrace.read(pid, cregs.rip.getLong(), backupbuf);
						ptrace.write(pid, cregs.rip.getLong(), shellcodebuf);

						ptrace.singlestep(pid);
						step++;
					} else if (step == 2) {
						if (0xCC == (ptrace.read_by_reg(pid, cregs.rip.offset()) & 0xFF)) {
							ProfilerUtil.measure("Step 2");
							System.out.printf("breakpoint: signal = %d\n", unix.WSTOPSIG(status.getValue()));
							// ptrace.pokeuser(pid, cregs.rip.offset(), ptrace.peekuser(pid, cregs.rip.offset()) + 1);
							System.out.println("Injected code has allocated memory at " + StringFormat.hex(ptrace.peekuser(pid, cregs.rax.offset())) + ".");
							System.out.println("Injected code is going to copy the remote thread procedure.");
							ptrace.singlestep(pid);
							step++;
						} else {
							ptrace.singlestep(pid);
						}
					} else if (step == 3) {
						if (0xCC == (ptrace.read_by_reg(pid, cregs.rip.offset()) & 0xFF)) {
							ProfilerUtil.measure("Step 3");
							System.out.println("Remote thread has been created.");
							// ptrace.pokeuser(pid, cregs.rip.offset(), ptrace.peekuser(pid, cregs.rip.offset()) + 1);
							ptrace.singlestep(pid);
							step++;
						} else {
							ptrace.singlestep(pid);
						}
					} else if (step == 4) {

						if (0xCC != (ptrace.read_by_reg(pid, cregs.rip.offset()) & 0xFF)) {
							ptrace.singlestep(pid);
							continue;
						}
						ProfilerUtil.measure("Step 4");

						ptrace.write(pid, cregs.rip.getLong(), backupbuf);
						ptrace.setregs(pid, Pointer.nativeValue(cregsbuf));
						ptrace.detach(pid);

						System.out.println("HOWGH!");
						System.exit(0);
					} else {
						ptrace.syscall(pid);
					}
				}
			}

		}

		//bsp = new BSPRenderer(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/maps/jb_citrus_v2_d.bsp"));
		//bsp.parse();
		/*
		 * setupWindow(bsp);
		 * boolean cyka = true;
		 * while(cyka) {
		 * Thread.sleep(500);
		 * }
		 */
		//System.exit(0);

		/*
		 * ExampleBufStruct bufstr1 = new ExampleBufStruct();
		 * ExampleBufStruct bufstr2 = new ExampleBufStruct();
		 * System.out.println("size: " + bufstr1.size());
		 * int[] contents = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160 };
		 * ByteBuffer buf = ByteBuffer.allocate(contents.length * Integer.BYTES);
		 * for (int x : contents) {
		 * System.out.print(x + ", ");
		 * buf.putInt(x);
		 * }
		 * buf.rewind();
		 * bufstr1.readFrom(buf);
		 * bufstr2.readFrom(buf);
		 * 
		 * System.out.println(StringFormat.dumpObj(bufstr1));
		 * System.out.println(StringFormat.dumpObj(bufstr2));
		 */

		if (DrawUtils.enableOverlay)
			setupWindow(new JOGL2Renderer());

		String processName = "csgo_linux64";
		String clientName = "client_client.so";
		String engineName = "engine_client.so";

		waitUntilFound("process", () -> (process = Processes.byName(processName)) != null);
		waitUntilFound("client module", () -> (clientModule = process.findModule(clientName)) != null);
		waitUntilFound("engine module", () -> (engineModule = process.findModule(engineName)) != null);
		System.out.println("process: " + processName);
		System.out.println("client: " + StringFormat.hex(clientModule.start()) + " - " + StringFormat.hex(clientModule.end()));
		System.out.println("engine: " + StringFormat.hex(engineModule.start()) + " - " + StringFormat.hex(clientModule.end()));

		loadOffsets();

		System.out.println("Engine initialization complete! Starting client...");
		Client.theClient.startClient();

		Client.theClient.commandManager.executeCommand("exec autoexec.txt");
		Client.theClient.eventHandler.onEngineLoaded();
		Client.theClient.commandManager.executeCommand("recoilcross toggle");
		Client.theClient.commandManager.executeCommand("crosshairdot toggle");
		Client.theClient.commandManager.executeCommand("bunnyhop toggle");
		Client.theClient.commandManager.executeCommand("namehud toggle");
		Client.theClient.commandManager.executeCommand("spectators toggle");
		Client.theClient.commandManager.executeCommand("bind KP_DELETE boneesp toggle");
		Client.theClient.commandManager.executeCommand("bind Alt_L glow toggle");
		Client.theClient.commandManager.executeCommand("bind kp_end disablepp toggle");
		Client.theClient.commandManager.executeCommand("bind END autojoinct toggle");
		Client.theClient.commandManager.executeCommand("bind HOME testmodule forceupdate");
		//Client.theClient.commandManager.executeCommand("bind END testmodule toshowinc");
		
		while (Client.theClient.isRunning) {
			try {
				Client.theClient.eventHandler.onPreLoop();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.sleep(100);
			}
			
			last_tick = System.nanoTime();
			//System.out.println(last_tick);
			/*isInGame = engineModule.readInt(Offsets.m_dwClientState + Offsets.m_bIsInGame);
			if(isInGame != 6) {
				Thread.sleep(1000);
				continue;
			}*/
			
			Offsets.m_dwLocalPlayer = clientModule.readLong(Offsets.m_dwLocalPlayerPointer);
			if (Offsets.m_dwLocalPlayer < 1) {
				Thread.sleep(1000);
				continue;
			}
			Offsets.m_dwPlayerResources = Engine.clientModule().readLong(Offsets.m_dwPlayerResourcesPointer);

			try {
				Client.theClient.eventHandler.onLoop();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.sleep(100);
			}
			DrawUtils.lppos.fov = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + 0x3998);
			if (DrawUtils.lppos.fov == 0)
				DrawUtils.lppos.defaultfov = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + 0x3AF4);
			
			if (tick % 1000 == 0)
				Engine.clientModule.read(Offsets.m_dwEntityList, entlistbuffer);

			if (tps_sleep > 0)
				Thread.sleep(tps_sleep);

			double adjust = ((1f / TARGET_TPS) * 1e9) / (System.nanoTime() - last_tick);
			tps_sleep *= adjust;
			if (tps_sleep > ((1f / TARGET_TPS) * 1000))
				tps_sleep = (long) ((1f / TARGET_TPS) * 1000l);
			if (tps_sleep < 1)
				tps_sleep = 1;

			// System.out.println("Looping! " + Math.floor(adjust*1e5)/1e5 + " /
			// " + tps_sleep + " - " + (System.nanoTime() - last_tick) + " > " +
			// ((System.nanoTime() - last_tick) / 1e9));
			tick++;
		}

		Client.theClient.shutdownClient();
	}

	private void setupWindow(GLEventListener renderer) {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setBackgroundOpaque(false);

		
        Xext xext = Xext.INSTANCE;
        
        long lBASIC_EVENT_MASK = X11.StructureNotifyMask|X11.ExposureMask|X11.PropertyChangeMask|X11.EnterWindowMask|X11.LeaveWindowMask|X11.KeyPressMask|X11.KeyReleaseMask|X11.KeymapStateMask;
        long lNOT_PROPAGATE_MASK = X11.KeyPressMask|X11.KeyReleaseMask|X11.ButtonPressMask|X11.ButtonReleaseMask|X11.PointerMotionMask|X11.ButtonMotionMask;
        NativeLong BASIC_EVENT_MASK = new NativeLong(lBASIC_EVENT_MASK);
        NativeLong NOT_PROPAGATE_MASK = new NativeLong(lNOT_PROPAGATE_MASK);
        NativeLong attrmask = new NativeLong(X11.CWColormap | X11.CWBorderPixel | X11.CWBackPixel | X11.CWEventMask | X11.CWWinGravity | X11.CWBitGravity | X11.CWSaveUnder | X11.CWDontPropagate | X11.CWOverrideRedirect);
        
        long lmask = X11.CWEventMask | X11.CWWinGravity | X11.CWBitGravity | X11.CWSaveUnder | X11.CWDontPropagate | X11.CWOverrideRedirect;
        
        Window root = x11.XDefaultRootWindow(dpy.get());
        int screen= x11.XDefaultScreen(dpy.get());
        Visual visual = x11.XDefaultVisual(dpy.get(), screen);
        XVisualInfo vinfo  = new XVisualInfo();
        vinfo.visual = visual;
        vinfo.c_class = X11.TrueColor;
        vinfo.depth = 32;
        
        Colormap colormap = x11.XCreateColormap(dpy.get(), root, visual, X11.AllocNone);

        XSetWindowAttributes attr = new XSetWindowAttributes();
        attr.background_pixmap = null;
        attr.background_pixel = new NativeLong(0);
        attr.border_pixel = new NativeLong(0);        
        attr.win_gravity = X11.NorthWestGravity;
        attr.bit_gravity = X11.ForgetGravity;
        attr.save_under = true;
        attr.event_mask = BASIC_EVENT_MASK;
        attr.do_not_propagate_mask=NOT_PROPAGATE_MASK;
        attr.override_redirect = true; // OpenGL > 0
        attr.colormap = colormap;
        
        /*Window cwin = x11.XCreateSimpleWindow(dpy.get(), root, 0, 0, 1920, 1080, 0, 0, 0);
        
        GC gc = x11.XCreateGC(dpy.get(), pmap, new NativeLong(0), shape_xgcv);*/
		GLWindow window = GLWindow.create(caps);
		//window.getDelegatedWindow().getNativeSurface().getDisplayHandle();
		Window xwin = new Window(window.getWindowHandle());
		// void XShapeCombineMask(Display display, Window window, int dest_kind, int x_off, int y_off, Pixmap src, int op);
		
		window.setAlwaysOnTop(true);
		window.setUndecorated(true);

		final FPSAnimator animator = new FPSAnimator(window, 60, true);

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted())
							animator.stop();
						System.exit(0);
					}
				}.start();
			}
		});

		window.addGLEventListener(renderer);
		window.setSize(1920, 1080);
		window.setPosition(1920, 0);
		//window.setPosition(0, 0);
		window.setTitle("Java CS:GO externals");
		
		x11.XChangeWindowAttributes(dpy.get(), xwin, new NativeLong(lmask), attr);
		window.setVisible(true);
		// window.setAlwaysOnTop(true);
		animator.start();
		
		System.out.println("started animator");
		
		
		/*Pixmap pmap = x11.XCreatePixmap(dpy.get(), xwin, window.getWidth(), window.getHeight(), 1);
		XGCValues shape_xgcv = new XGCValues();
		GC shape_gc = x11.XCreateGC(dpy.get(), pmap, new NativeLong(0), shape_xgcv);
		x11.XSetForeground(dpy.get(), shape_gc, new NativeLong(0)); // 1 to paint
		x11.XFillRectangle(dpy.get(), pmap, shape_gc, 0,  0, window.getWidth(), window.getHeight());
		//xext.XShapeCombineMask(dpy.get(), xwin, Xext.ShapeInput, 0, 0, pmap, Xext.ShapeSet);
		
		x11.XFreePixmap(dpy.get(), pmap);
		x11.XFreeGC(dpy.get(), shape_gc);*/
		
		
		
		/*XGCValues shape_xgcv = new XGCValues();
		Pixmap imap = x11.XCreatePixmap(dpy.get(), xwin, 100, 100, 0);
		GC igc = x11.XCreateGC(dpy.get(), imap, new NativeLong(0), shape_xgcv);
		x11.XSetForeground(dpy.get(), igc, new NativeLong(1)); // 1 to paint
		x11.XFillRectangle(dpy.get(), imap, igc, 0, 0, 100, 100);

		xext.XShapeCombineMask(dpy.get(), xwin, Xext.ShapeInput, 0, 0, imap, Xext.ShapeSet);*/

		
		 // XAllowEvents(g_display, AsyncBoth, CurrentTime);
		//x11.XSelectInput(dpy.get(), xwin, new NativeLong(lib.PointerMotionMask | lib.ButtonPressMask | lib.ButtonReleaseMask));
		
	}

	public static void initAll() {
		loadOffsets();
	}

	public static void loadOffsets() {
		Offsets.load();
		System.out.println();
		System.out.println("m_dwGlowObject: " + StringFormat.hex(Offsets.m_dwGlowObject));
		System.out.println("m_iAlt1: " + StringFormat.hex(Offsets.input.alt1));
		System.out.println("m_iAlt2: " + StringFormat.hex(Offsets.input.alt2));
		System.out.println("m_dwForceJump: " + StringFormat.hex(Offsets.input.jump));
		System.out.println("m_dw_bOverridePostProcessingDisable: " + StringFormat.hex(Offsets.m_dw_bOverridePostProcessingDisable));
		System.out.println("m_dwPlayerResources: " + StringFormat.hex(Offsets.m_dwPlayerResourcesPointer));
		System.out.println("m_dwForceAttack: " + StringFormat.hex(Offsets.input.attack));
		System.out.println("m_dwEntityList: " + StringFormat.hex(Offsets.m_dwEntityList));
		System.out.println("m_dwLocalPlayerPointer: " + StringFormat.hex(Offsets.m_dwLocalPlayerPointer) + " / " + StringFormat.hex(Offsets.m_dwLocalPlayerPointer - clientModule().start()));
		System.out.println("m_dwGlobalVars: " + StringFormat.hex(Offsets.m_dwGlobalVars));
		System.out.println();
	}

	public static Process process() {
		return process;
	}

	public static Module clientModule() {
		return clientModule;
	}

	public static Module engineModule() {
		return engineModule;
	}
	
	public static boolean IsInGame() {
		//return true;
		return isInGame == 6;
	}

	private static void waitUntilFound(String message, Clause clause) {
		System.out.print("Looking for " + message + ". Please wait.");
		while (!clause.get())
			try {
				Thread.sleep(1000);
				System.out.print(".");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("\nFound " + message + "!");
	}

	@FunctionalInterface
	private interface Clause {
		boolean get();
	}

}
