package me.lixko.csgoexternals;

import com.sun.jna.platform.unix.X11.KeySym;

public class KeyboardHandler {

	byte[] keys = new byte[32];
	byte[] lastkeys = new byte[32];

	Thread keyLoop = new Thread(new Runnable() {
		@Override
		public void run() {

			while (!Client.theClient.isInitialized) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (Engine.dpy == null)
					throw new Error("Can't open X Display");

				Engine.x11.XQueryKeymap(Engine.dpy.get(), keys);

				for (int i = 0; i < keys.length; ++i) {
					if (keys[i] != lastkeys[i]) {
						for (int j = 0, test = 1; j < 8; ++j, test *= 2) {
							if (((keys[i] & test) != (lastkeys[i] & test))) { // (keys[i] & test) > 0)
								int code = i * 8 + j;
								KeySym sym = Engine.x11.XKeycodeToKeysym(Engine.dpy.get(), (byte) code, 0);
								// System.out.println((keys[i] & test) + " Key: " + Engine.x11.XKeysymToString(sym) + " / " + StringFormat.hex(code) + " = " + code + " / " + sym.intValue() + ", i: " + i + " j: " + j);
								if ((keys[i] & test) > 0)
									Client.theClient.eventHandler.onKeyPress(sym);
								else
									Client.theClient.eventHandler.onKeyRelease(sym);
							}
						}
					}
					lastkeys[i] = keys[i];
				}
			}
			System.out.println("ENDING KEYLOOP");
			Engine.x11.XCloseDisplay(Engine.dpy.get());
		}
	});

	public KeyboardHandler() {
		keyLoop.start();
	}

	public boolean isPressed(int code) {
		int c = Engine.x11.XKeysymToKeycode(Engine.dpy.get(), new KeySym(code));
		return (keys[(c & 0xFF) / 8] & (1 << (c % 8))) > 0;
	}

}
