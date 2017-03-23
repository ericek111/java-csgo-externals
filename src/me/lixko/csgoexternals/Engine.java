package me.lixko.csgoexternals;

import java.io.IOException;

import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.github.jonatino.process.Processes;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.StringFormat;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public final class Engine {

	private static Process process;
	private static Module clientModule, engineModule;

	private static final int TARGET_TPS = 200;
	private long tps_sleep = (long) ((1f / TARGET_TPS) * 1000);
	private long last_tick = 0;

	public static X11 lib = X11.INSTANCE;
	public static ThreadLocal<Display> dpy = ThreadLocal.withInitial(() -> lib.XOpenDisplay(null));

	public void init() throws InterruptedException, IOException {
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

		Thread keyLoop = new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] keys = new byte[32];
				byte[] lastkeys = new byte[32];

				while (Client.theClient.isRunning) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (dpy == null)
						throw new Error("Can't open X Display");

					lib.XQueryKeymap(dpy.get(), keys);

					for (int i = 0; i < keys.length; ++i) {
						if (keys[i] != lastkeys[i]) {
							for (int j = 0, test = 1; j < 8; ++j, test *= 2) {
								if (((keys[i] & test) > 0) && ((keys[i] & test) != (lastkeys[i] & test))) {
									int code = i * 8 + j;
									KeySym sym = lib.XKeycodeToKeysym(dpy.get(), (byte) code, 0);
									// System.out.println("Key: " +
									// lib.XKeysymToString(sym) + " / " +
									// PatternScanner.hex(lib.XKeysymToKeycode(dpy,
									// sym)) + " / " + sym.intValue());
									Client.theClient.eventHandler.onKeyPress(sym);
								}
							}
						}
						lastkeys[i] = keys[i];
					}
				}
				lib.XCloseDisplay(dpy.get());
			}
		});

		String processName = "csgo_linux64";
		String clientName = "client_client.so";
		String engineName = "engine_client.so";

		waitUntilFound("process", () -> (process = Processes.byName(processName)) != null);
		waitUntilFound("client module", () -> (clientModule = process.findModule(clientName)) != null);
		waitUntilFound("engine module", () -> (engineModule = process.findModule(engineName)) != null);
		System.out.println("process: " + processName);
		System.out.println("client: " + StringFormat.hex(clientModule.start()));
		System.out.println("engine: " + StringFormat.hex(engineModule.start()));
		loadOffsets();

		System.out.println("Engine initialization complete! Starting client...");
		Client.theClient.startClient();
		keyLoop.start();

		Client.theClient.commandManager.executeCommand("exec autoexec.txt");
		Client.theClient.eventHandler.onEngineLoaded();

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

		keyLoop.join();
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
		System.out.println("m_dwLocalPlayerPointer: " + StringFormat.hex(Offsets.m_dwLocalPlayerPointer));
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
