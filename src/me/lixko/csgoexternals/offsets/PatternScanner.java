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

import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.process.Module;

import me.lixko.csgoexternals.util.StringFormat;

public final class PatternScanner {

	public static final int READ = 1, SUBTRACT = 2;

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, String className) {
		return getAddressForPattern(module, pattern_offset, address_offset, flags, className.getBytes());
	}

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, long value) {
		return getAddressForPattern(module, pattern_offset, address_offset, flags, toByteArray(value));
	}

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, int value) {
		return getAddressForPattern(module, pattern_offset, address_offset, flags, toByteArray(value));
	}

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, int... values) {
		return getAddressForPattern(module, pattern_offset, address_offset, flags, toByteArray(values));
	}

	public static long getAddressForPattern(Module module, String sigstr) {
		return getAddressForPattern(module, 0, 0, 0, hexStringToByteArray(sigstr));
	}

	public static long getAddressForPattern(Module module, long values) {
		byte[] barr = toByteArray(values);
		System.out.println(StringFormat.hex(barr));
		return getAddressForPattern(module, 0, 0, 0, barr);
	}

	public static long getAddressForPattern(Module module, int pattern_offset, int address_offset, int flags, byte... values) {
		long off = module.size() - values.length;
		for (long i = 0; i < off; i++) {
			if (checkMask(module, i, values)) {
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

	private static boolean checkMask(Module module, long offset, byte[] pMask) {
		for (int i = 0; i < pMask.length; i++) {
			if (pMask[i] != 0x0 && (pMask[i] != module.data().getByte(offset + i))) {
				return false;
			}
		}
		return true;
	}

	private static byte[] toByteArray(int value) {
		return new byte[] { (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24) };
	}

	private static byte[] toByteArray(long value) {
		return new byte[] { (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24), (byte) (value >> 32), (byte) (value >> 40), (byte) (value >> 48), (byte) (value >> 56) };
	}

	private static byte[] toByteArray(int... value) {
		byte[] byteVals = Cacheable.array(value.length);
		for (int i = 0; i < value.length; i++) {
			byteVals[i] = (byte) value[i];
		}
		return byteVals;
	}

	public static byte[] hexStringToByteArray(String str) {
		String s = str.replaceAll("\\s+", "").replace("??", "00");
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}
