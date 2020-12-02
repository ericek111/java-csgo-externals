/*
 *    Copyright 2016 Jonathan Beaudoin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.lixko.csgoexternals.offsets;

import java.util.Arrays;

import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.process.Module;

import me.lixko.csgoexternals.util.StringFormat;

public final class PatternScanner {

	public static final int READ = 1, SUBTRACT = 2;

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, String className) {
		return getAddressForPattern(module, pattern_offset, address_offset, flags, null, className.getBytes());
	}

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, int... values) {
		return getAddressForPattern(module, pattern_offset, address_offset, flags, null, toByteArray(values));
	}

	/**
	 * Match an IDA-style signature (EF F3 ?? ?? 00 00).
	 * 
	 * @param module
	 * @param sigstr
	 * @return
	 */
	public static long getAddressForPattern(Module module, String sigstr) {
		return getAddressForPattern(module, 0, 0, 0, hexStringToMask(sigstr), hexStringToByteArray(sigstr));
	}
	
	/**
	 * Find a signature in the memory space of the provided Module.
	 * 
	 * @param module
	 * @param pattern_offset
	 * @param address_offset
	 * @param flags
	 * @param mask If null, values are matched only if they're truly equal. Only 0xff/0x00 supported at the moment.
	 * @param values Bytes to match against, must align with mask.
	 * @return
	 */
	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, byte[] mask, byte[] values) {
		if (mask == null) {
			mask = new byte[values.length];
			Arrays.fill(mask, (byte) 0xff);
		}
		
		long off = module.size() - values.length;
		for (long i = 0; i < off; i++) {
			if (checkMask(module, i, mask, values)) {
				i += module.start() + pattern_offset;
				if ((flags & READ) == READ) {
					i = module.process().readLong(i);
				}
				if ((flags & SUBTRACT) == SUBTRACT) {
					i -= module.start();
				}
				return i + address_offset;
			}
		}
		throw new IllegalStateException("Can not find offset inside of " + module.name() + " with pattern " + StringFormat.hex(values)); // + Arrays.toString(values));
	}

	private static boolean checkMask(Module module, long offset, byte[] mask, byte[] data) {
		for (int i = 0; i < data.length; i++) {
			if (mask[i] != 0x00 && (data[i] != module.data().getByte(offset + i))) {
				return false;
			}
		}
		return true;
	}

	private static byte[] toByteArray(int... value) {
		byte[] byteVals = Cacheable.array(value.length);
		for (int i = 0; i < value.length; i++) {
			byteVals[i] = (byte) value[i];
		}
		return byteVals;
	}

	public static byte[] hexStringToByteArray(String str) {
		String s = str.replaceAll("\\s+", "").replace("??", "00").toUpperCase();
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	public static byte[] hexStringToMask(String str) {
		String s = str.replaceAll("\\s+", "").toUpperCase();
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			if (s.charAt(i) == '?' && s.charAt(i + 1) == '?') {
				data[i / 2] = (byte) 0x00;
			} else {
				data[i / 2] = (byte) 0xff;
			}
		}
		return data;
	}
	
}
