package me.lixko.csgoexternals.util;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;

public class InputUtils {

	public static boolean isPressed(int keysym) {
		X11 lib = X11.INSTANCE;
		Display dpy = lib.XOpenDisplay(null);
		if (dpy == null) {
			throw new Error("Can't open X Display");
		}
		try {
			byte[] keys = new byte[32];
			// Ignore the return value
			lib.XQueryKeymap(dpy, keys);
			for (int code = 5; code < 256; code++) {
				int idx = code / 8;
				int shift = code % 8;
				if ((keys[idx] & (1 << shift)) != 0) {
					int sym = lib.XKeycodeToKeysym(dpy, (byte) code, 0).intValue();
					if (sym == keysym)
						return true;
				}
			}
		} finally {
			lib.XCloseDisplay(dpy);
		}
		return false;
	}

	public static int getPressedKey() {
		X11 lib = X11.INSTANCE;
		Display dpy = lib.XOpenDisplay(null);
		if (dpy == null) {
			throw new Error("Can't open X Display");
		}
		try {
			byte[] keys = new byte[32];
			// Ignore the return value
			lib.XQueryKeymap(dpy, keys);
			for (int code = 5; code < 256; code++) {
				int idx = code / 8;
				int shift = code % 8;
				if ((keys[idx] & (1 << shift)) != 0) {
					int sym = lib.XKeycodeToKeysym(dpy, (byte) code, 0).intValue();
					return sym;
				}
			}
		} finally {
			lib.XCloseDisplay(dpy);
		}
		return 0;
	}
}
