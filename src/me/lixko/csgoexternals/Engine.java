package me.lixko.csgoexternals;

import java.io.File;
import java.io.IOException;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.github.jonatino.process.Processes;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.XTest;
import com.sun.jna.platform.unix.X11.XVisualInfo;
import com.sun.jna.platform.unix.X11.Xext;
import com.sun.jna.platform.unix.X11.Colormap;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Visual;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.XSetWindowAttributes;

import me.lixko.csgoexternals.elf.ElfModule;
import me.lixko.csgoexternals.offsets.Convars;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.CGlobalVars;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.bsp.BSPParser;
import me.lixko.csgoexternals.util.bsp.BSPRenderer;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public final class Engine {

	private static Process process, localprocess = Processes.byId(MemoryUtils.getPID());
	private static ElfModule clientModule, engineModule, materialModule, launcherModule;

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

	public static CGlobalVars globalVars = new CGlobalVars();
	private MemoryBuffer globalVarsBuf = new MemoryBuffer(globalVars.size());
	
	public void init(String[] args) throws InterruptedException, IOException {
		this.cmdargs = args;

		if (DrawUtils.enableOverlay)
			setupWindow(new JOGL2Renderer());
		
		globalVars.setSource(globalVarsBuf);

		String processName = "csgo_linux64";
		String clientName = "client_client.so";
		String engineName = "engine_client.so";

		waitUntilFound("process", () -> (process = Processes.byName(processName)) != null);
		waitUntilFound("client module", () -> (clientModule = new ElfModule(process.findModule(clientName))) != null);
		waitUntilFound("engine module", () -> (engineModule = new ElfModule(process.findModule(engineName))) != null);
		waitUntilFound("materialsystem module", () -> (materialModule = new ElfModule(process.findModule("materialsystem_client.so"))) != null);
		waitUntilFound("launcher module", () -> (launcherModule = new ElfModule(process.findModule("launcher_client.so"))) != null);
		
		//System.out.println("process: " + processName);
		//System.out.println("client: " + StringFormat.hex(clientModule.start()) + " - " + StringFormat.hex(clientModule.end()));
		//System.out.println("engine: " + StringFormat.hex(engineModule.start()) + " - " + StringFormat.hex(clientModule.end()));

		loadOffsets(false);
		// Convars.init(materialModule); // used to work
		
		//bsp = new BSPRenderer(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/maps/de_nuke.bsp"));
		//bsp.parse();
		
		Client.theClient.startClient();
		
		Client.theClient.commandManager.executeCommand("exec autoexec.txt");
		Client.theClient.eventHandler.onEngineLoaded();
		Client.theClient.commandManager.executeCommand("recoilcross toggle");
		Client.theClient.commandManager.executeCommand("crosshairdot toggle");
		Client.theClient.commandManager.executeCommand("bunnyhop toggle");
		Client.theClient.commandManager.executeCommand("namehud toggle");
		Client.theClient.commandManager.executeCommand("spectators toggle");
		Client.theClient.commandManager.executeCommand("bind KP_DELETE boneesp toggle");
		Client.theClient.commandManager.executeCommand("bind KP_Insert fovchanger toggle");
		Client.theClient.commandManager.executeCommand("bind Alt_L glow toggle");
		Client.theClient.commandManager.executeCommand("bind kp_end disablepp toggle");
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

			//isInGame = engineModule.readInt(Offsets.m_dwClientState + Offsets.m_bIsInGame);
			if(!IsInGame()) {
				Thread.sleep(1000);
				continue;
			}
			
			Offsets.m_dwLocalPlayer = clientModule.readLong(Offsets.m_dwLocalPlayerPointer);
			if (Offsets.m_dwLocalPlayer < 1) {
				Thread.sleep(1000);
				continue;
			}
			Offsets.m_dwPlayerResources = Engine.clientModule().readLong(Offsets.m_dwPlayerResourcesPointer);
					
			
			Engine.clientModule().read(Offsets.m_dwGlobalVars, globalVarsBuf);

			try {
				Client.theClient.eventHandler.onLoop();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.sleep(100);
			}
			
			if (tick % 1000 == 0)
				Engine.clientModule.read(Offsets.m_dwEntityList, entlistbuffer);

			long entityptr = MemoryUtils.getLocalOrSpectated();			
			DrawUtils.lppos.fov = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iFOV);
			if (DrawUtils.lppos.fov == 0)
				DrawUtils.lppos.defaultfov = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iDefaultFOV);			
			
			
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

		final FPSAnimator animator = new FPSAnimator(window, 75, true);

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

	public static void loadOffsets(boolean dump) {
		Offsets.load();
		System.out.println();
		System.out.println("m_dwGlowObject: " + StringFormat.hex(Offsets.m_dwGlowObject));
		System.out.println("m_dw_bOverridePostProcessingDisable: " + StringFormat.hex(Offsets.m_dw_bOverridePostProcessingDisable));
		System.out.println("m_dwPlayerResources: " + StringFormat.hex(Offsets.m_dwPlayerResourcesPointer));
		System.out.println("m_dwForceAttack: " + StringFormat.hex(Offsets.input.attack));
		System.out.println("m_dwEntityList: " + StringFormat.hex(Offsets.m_dwEntityList));
		System.out.println("m_dwLocalPlayer: " + StringFormat.hex(Offsets.m_dwLocalPlayer));
		System.out.println("m_dwLocalPlayerPointer: " + StringFormat.hex(Offsets.m_dwLocalPlayerPointer) + " / " + StringFormat.hex(Offsets.m_dwLocalPlayerPointer - clientModule().start()));
		System.out.println("m_dwGlobalVars: " + StringFormat.hex(Offsets.m_dwGlobalVars));
		System.out.println();
	}

	public static Process process() {
		return process;
	}

	public static ElfModule clientModule() {
		return clientModule;
	}

	public static ElfModule engineModule() {
		return engineModule;
	}
	
	public static ElfModule materialModule() {
		return materialModule;
	}
	
	public static ElfModule launcherModule() {
		return launcherModule;
	}
	
	public static boolean IsInGame() {
		return true;
		//return isInGame == 6;
	}

	private static void waitUntilFound(String message, Clause clause) {
		while (!clause.get())
			try {
				Thread.sleep(1000);
				System.out.print(".");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	@FunctionalInterface
	private interface Clause {
		boolean get();
	}

}
