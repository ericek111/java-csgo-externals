package me.lixko.csgoexternals;

import java.io.IOException;

import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.github.jonatino.process.Processes;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.XTest;
import com.sun.jna.platform.unix.X11.Xext;
import com.sun.jna.platform.unix.X11.Display;

import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
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

	public static ThreadLocal<Display> dpy = ThreadLocal.withInitial(() -> x11.XOpenDisplay(null));

	public void init() throws InterruptedException, IOException {

		if (DrawUtils.enableOverlay)
			setupWindow();

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
		// Client.theClient.commandManager.executeCommand("namehud toggle");
		// Client.theClient.commandManager.executeCommand("spectators toggle");
		// Client.theClient.commandManager.executeCommand("boneesp toggle");
		// Client.theClient.commandManager.executeCommand("bind Alt_L glow toggle");
		// Client.theClient.commandManager.executeCommand("bind kp_end disablepp toggle");
		// Client.theClient.commandManager.executeCommand("bind END autojoinct toggle");

		while (Client.theClient.isRunning) {
			last_tick = System.nanoTime();

			Offsets.m_dwLocalPlayer = clientModule.readLong(Offsets.m_dwLocalPlayerPointer);
			Offsets.m_dwPlayerResources = Engine.clientModule().readLong(Offsets.m_dwPlayerResourcesPointer);

			if (Offsets.m_dwLocalPlayer < 1) {
				Thread.sleep(1000);
				continue;
			}

			try {
				Client.theClient.eventHandler.onLoop();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.sleep(100);
			}
			DrawUtils.lppos.fov = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + 0x3998);
			if (DrawUtils.lppos.fov == 0)
				DrawUtils.lppos.defaultfov = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + 0x3AF4);

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

		}

		Client.theClient.shutdownClient();
	}

	private void setupWindow() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setBackgroundOpaque(false);
		GLWindow window = GLWindow.create(caps);

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

		window.addGLEventListener(new JOGL2Renderer());
		window.setSize(1920, 1080);
		window.setTitle("Java CS:GO externals");
		window.setVisible(true);
		// window.setAlwaysOnTop(true);
		animator.start();

		// void XShapeCombineMask(Display display, Window window, int dest_kind, int x_off, int y_off, Pixmap src, int op);
		/*
		 * Window xwin = new Window(window.getWindowHandle());
		 * Pixmap pmap = lib.XCreatePixmap(dpy.get(), xwin, 150, 150, 1);
		 * xext.XShapeCombineMask(dpy.get(), xwin, xext.ShapeInput, 100, 100, pmap, xext.ShapeSet);
		 * // XAllowEvents(g_display, AsyncBoth, CurrentTime);
		 * lib.XSelectInput(dpy.get(), xwin, new NativeLong(lib.PointerMotionMask | lib.ButtonPressMask | lib.ButtonReleaseMask));
		 */
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
