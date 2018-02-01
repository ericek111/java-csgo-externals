package me.lixko.csgoexternals.util;

public class ProfilerUtil {
	private static final ThreadLocal<Long> firstnanos = ThreadLocal.withInitial(() -> new Long(0));
	private static final ThreadLocal<Long> lastnanos = ThreadLocal.withInitial(() -> new Long(0));
	
	public static void start() {
		firstnanos.set(System.nanoTime());
	}

	public static void measure(String text) {
		System.out.println(text);
		if(true) return;
		if (firstnanos.get() == 0) {
			firstnanos.set(System.nanoTime());
			lastnanos.set(System.nanoTime());
			System.out.println("[PROFILER] Started measurement.");
			return;
		}

		long elapsed = System.nanoTime() - firstnanos.get();
		long diff = System.nanoTime() - lastnanos.get();

		System.out.format("[PROFILER] [%d] %d: %s\n", elapsed, diff, text);

		lastnanos.set(System.nanoTime());

	}
}
