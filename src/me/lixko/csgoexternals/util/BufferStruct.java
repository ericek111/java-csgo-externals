package me.lixko.csgoexternals.util;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.process.Module;

public class BufferStruct {

	private int read = 0;
	private int size = 0;
	private int fieldlen = 0;

	public BufferStruct() {
	}

	public void readFrom(ByteBuffer buf, ByteOrder order) {
		buf.order(order);
		readFrom(buf);
	}
	
	public void readFrom(ByteBuffer buf) {
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!Modifier.isPublic(f.getModifiers()) || f.isAnnotationPresent(SkipField.class))
				continue;
			fieldlen = 0;
			if (f.isAnnotationPresent(UnsignedField.class)) {
				int len = f.getAnnotation(UnsignedField.class).value();
				if (len == 1 || len == 2 || len == 4) {
					fieldlen = len;
				} else
					throw new IllegalArgumentException("Invalid UnsignedField length on " + f.getName() + ": " + len);
			}
			try {
				if (f.getType().isArray())
					readArray(f.get(this), buf, f);
				else
					readField(f, buf);
				this.afterRead();
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	public void readFrom(Module module, long address, MemoryBuffer buf) {
		module.read(address, buf);
		this.readFrom(buf, 0);
	}

	public void readFrom(MemoryBuffer buf) {
		this.readFrom(buf, 0);
	}

	public void readFrom(MemoryBuffer buf, int offset) {
		read = offset;
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!Modifier.isPublic(f.getModifiers()) || f.isAnnotationPresent(SkipField.class))
				continue;
			fieldlen = 0;
			if (f.isAnnotationPresent(UnsignedField.class)) {
				int len = f.getAnnotation(UnsignedField.class).value();
				if (len == 1 || len == 2 || len == 4) {
					fieldlen = len;
				} else
					throw new IllegalArgumentException("Invalid UnsignedField length on " + f.getName() + ": " + len);
			}
			try {
				if (f.getType().isArray())
					readArray(f.get(this), buf, f);
				else
					readField(f, buf);
				this.afterRead();
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	public void readArray(Object arr, ByteBuffer buf, Field f) throws InstantiationException, IllegalAccessException {
		byte[] strbuf = new byte[0];
		String charset = "";
		if (f.isAnnotationPresent(StringLength.class)) {
			int len = f.getAnnotation(StringLength.class).size();
			charset = f.getAnnotation(StringLength.class).charset();
			strbuf = new byte[len];
		}
		for (int i = 0; i < Array.getLength(arr); i++) {
			Object value = Array.get(arr, i);
			if (value == null) {
				value = arr.getClass().getComponentType().newInstance();
				Array.set(arr, i, value);
			}
			if (value.getClass().isArray()) {
				readArray(value, buf, f);
			} else {
				if (strbuf.length > 0) {
					buf.get(strbuf);
					try {
						Array.set(arr, i, new String(strbuf, charset));
					} catch (UnsupportedEncodingException e) {
						throw new IllegalArgumentException("UnsupportedEncodingException (" + charset + ")! " + f.getName() + " of " + value.getClass().getName());
					}
				} else if (fieldlen > 0) {
					if (fieldlen == 1)
						Array.set(arr, i, buf.get() & 0xFF);
					else if (fieldlen == 2)
						Array.set(arr, i, buf.getShort() & 0xFFFF);
					else if (fieldlen == 4)
						Array.set(arr, i, ((long) buf.getInt()) & 0xFFFFFFFF);
				} else if (value instanceof Byte)
					Array.set(arr, i, buf.get());
				else if (value instanceof Character)
					Array.set(arr, i, buf.getChar());
				else if (value instanceof Boolean)
					Array.set(arr, i, buf.get());
				else if (value instanceof Short)
					Array.set(arr, i, buf.getShort());
				else if (value instanceof Integer)
					Array.set(arr, i, buf.getInt());
				else if (value instanceof Long)
					Array.set(arr, i, buf.getLong());
				else if (value instanceof Float)
					Array.set(arr, i, buf.getFloat());
				else if (value instanceof Double)
					Array.set(arr, i, buf.getDouble());
				else if (value instanceof BufferStruct)
					((BufferStruct) value).readFrom(buf);
				else
					throw new IllegalArgumentException("Non-primitive types are not implemented yet! " + value.getClass().getName());
			}
		}
	}

	public void readArray(Object arr, MemoryBuffer buf, Field f) throws InstantiationException, IllegalAccessException {
		byte[] strbuf = new byte[0];
		String charset = "";
		if (f.isAnnotationPresent(StringLength.class)) {
			int len = f.getAnnotation(StringLength.class).size();
			charset = f.getAnnotation(StringLength.class).charset();
			strbuf = new byte[len];
		}
		for (int i = 0; i < Array.getLength(arr); i++) {
			Object value = Array.get(arr, i);
			if (value == null) {
				value = arr.getClass().getComponentType().newInstance();
				Array.set(arr, i, value);
			}
			if (value.getClass().isArray()) {
				readArray(value, buf, f);
			} else {
				if (strbuf.length > 0) {
					buf.get(strbuf);
					try {
						Array.set(arr, i, new String(strbuf, charset));
					} catch (UnsupportedEncodingException e) {
						throw new IllegalArgumentException("UnsupportedEncodingException (" + charset + ")! " + f.getName() + " of " + value.getClass().getName());
					}
				} else if (fieldlen > 0) {
					if (fieldlen == 1)
						Array.set(arr, i, buf.getByte(read) & 0xFF);
					else if (fieldlen == 2)
						Array.set(arr, i, buf.getShort(read) & 0xFFFF);
					else if (fieldlen == 4)
						Array.set(arr, i, ((long) buf.getInt(read)) & 0xFFFFFFFF);
					read += fieldlen;
				} else if (value instanceof Byte) {
					Array.set(arr, i, buf.getByte(read));
					read += Byte.BYTES;
				} else if (value instanceof Character) {
					Array.set(arr, i, buf.getChar(read));
					read += Character.BYTES;
				} else if (value instanceof Boolean) {
					Array.set(arr, i, buf.getBoolean(read));
					read += 1;
				} else if (value instanceof Short) {
					Array.set(arr, i, buf.getShort(read));
					read += Short.BYTES;
				} else if (value instanceof Integer) {
					Array.set(arr, i, buf.getInt(read));
					read += Integer.BYTES;
				} else if (value instanceof Long) {
					Array.set(arr, i, buf.getLong(read));
					read += Long.BYTES;
				} else if (value instanceof Float) {
					Array.set(arr, i, buf.getFloat(read));
					read += Float.BYTES;
				} else if (value instanceof Double) {
					Array.set(arr, i, buf.getDouble(read));
					read += Double.BYTES;
				} else if (value instanceof BufferStruct)
					((BufferStruct) value).readFrom(buf, read);
				else
					throw new IllegalArgumentException("Non-primitive types are not implemented yet! " + value.getClass().getName());
			}
		}
	}

	private int countStruct() {
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!Modifier.isPublic(f.getModifiers()) || f.isAnnotationPresent(SkipField.class))
				continue;
			fieldlen = 0;
			if (f.isAnnotationPresent(UnsignedField.class)) {
				int len = f.getAnnotation(UnsignedField.class).value();
				if (len == 1 || len == 2 || len == 4) {
					fieldlen = len;
				} else
					throw new IllegalArgumentException("Invalid UnsignedField length on " + f.getName() + ": " + len);
			}
			try {
				if (f.getType().isArray())
					countArray(f.get(this));
				else
					countField(f);
				this.afterRead();
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		return size;
	}

	private void countArray(Object arr) throws InstantiationException, IllegalAccessException {
		for (int i = 0; i < Array.getLength(arr); i++) {
			Object value = Array.get(arr, i);
			if (value == null) {
				value = arr.getClass().getComponentType().newInstance();
				Array.set(arr, i, value);
			}
			if (value.getClass().isArray()) {
				countArray(value);
			} else {
				if (fieldlen > 0)
					size += fieldlen;
				else if (value instanceof Byte)
					size += Byte.BYTES;
				else if (value instanceof Character)
					size += Character.BYTES;
				else if (value instanceof Boolean)
					size += 1;
				else if (value instanceof Short)
					size += Short.BYTES;
				else if (value instanceof Integer)
					size += Integer.BYTES;
				else if (value instanceof Long)
					size += Long.BYTES;
				else if (value instanceof Float)
					size += Float.BYTES;
				else if (value instanceof Double)
					size += Double.BYTES;
				else if (value instanceof BufferStruct)
					size += ((BufferStruct) value).countStruct();
				else
					throw new IllegalArgumentException("Non-primitive types are not implemented yet! " + value.getClass().getName());
			}
			//System.out.println(i + " > " + size);
		}
	}

	private void countField(Field f) throws IllegalArgumentException, IllegalAccessException {
		if (fieldlen > 0) {
			size += fieldlen;
			return;
		}
		Object obj = f.get(this);
		if (obj instanceof Byte)
			size += Byte.BYTES;
		else if (obj instanceof Character)
			size += Character.BYTES;
		else if (obj instanceof Boolean)
			size += 1;
		else if (obj instanceof Short)
			size += Short.BYTES;
		else if (obj instanceof Integer)
			size += Integer.BYTES;
		else if (obj instanceof Long)
			size += Long.BYTES;
		else if (obj instanceof Float)
			size += Float.BYTES;
		else if (obj instanceof Double)
			size += Double.BYTES;
		else if (obj instanceof String) {
			//TODO: Implement zero-terminated strings with variable length
			if (!f.isAnnotationPresent(StringLength.class))
				return;
			int len = f.getAnnotation(StringLength.class).size();
			size += len;
		} else if (BufferStruct.class.isAssignableFrom(f.getType()))
			size += ((BufferStruct) obj).countStruct();
		else
			throw new IllegalArgumentException("Non-primitive types are not implemented yet! " + f.getName() + " of " + obj.getClass().getName());
		//System.err.println(f.getName() + " > " + size);
	}

	public void readField(Field f, ByteBuffer buf) throws IllegalArgumentException, IllegalAccessException {
		if (fieldlen > 0) {
			if (fieldlen == 1)
				f.set(this, buf.get() & 0xFF);
			else if (fieldlen == 2)
				f.set(this, buf.getShort() & 0xFFFF);
			else if (fieldlen == 4)
				f.set(this, ((long) buf.getInt()) & 0xFFFFFFFF);
			return;
		}
		Object obj = f.get(this);
		if (obj instanceof Byte)
			f.set(this, buf.get());
		else if (obj instanceof Character)
			f.set(this, buf.getChar());
		else if (obj instanceof Boolean)
			f.set(this, buf.get());
		else if (obj instanceof Short)
			f.set(this, buf.getShort());
		else if (obj instanceof Integer)
			f.set(this, buf.getInt());
		else if (obj instanceof Long)
			f.set(this, buf.getLong());
		else if (obj instanceof Float)
			f.set(this, buf.getFloat());
		else if (obj instanceof Double)
			f.set(this, buf.getDouble());
		else if (obj instanceof String) {
			if (!f.isAnnotationPresent(StringLength.class))
				return;
			int len = f.getAnnotation(StringLength.class).size();
			String charset = f.getAnnotation(StringLength.class).charset();

			byte[] strbuf = new byte[len];
			buf.get(strbuf);
			try {
				String tempstr = new String(strbuf, charset);
				f.set(this, tempstr);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("UnsupportedEncodingException (" + charset + ")! " + f.getName() + " of " + obj.getClass().getName());
			}
		} else if (BufferStruct.class.isAssignableFrom(f.getType()))
			((BufferStruct) obj).readFrom(buf);
		else
			throw new IllegalArgumentException("Non-primitive types are not implemented yet! " + f.getName() + " of " + obj.getClass().getName());
	}

	public void readField(Field f, MemoryBuffer buf) throws IllegalArgumentException, IllegalAccessException {
		if (fieldlen > 0) {
			if (fieldlen == 1)
				f.set(this, buf.getByte(read) & 0xFF);
			else if (fieldlen == 2)
				f.set(this, buf.getShort(read) & 0xFFFF);
			else if (fieldlen == 4)
				f.set(this, buf.getInt(read) & 0xFFFFFFFF);
			read += fieldlen;
			return;
		}
		Object obj = f.get(this);
		if (obj instanceof Byte) {
			f.set(this, buf.getByte(read));
			read += Byte.BYTES;
		} else if (obj instanceof Character) {
			f.set(this, buf.getChar(read));
			read += Character.BYTES;
		} else if (obj instanceof Boolean) {
			f.set(this, buf.getBoolean(read));
			read += 1;
		} else if (obj instanceof Short) {
			f.set(this, buf.getShort(read));
			read += Short.BYTES;
		} else if (obj instanceof Integer) {
			f.set(this, buf.getInt(read));
			read += Integer.BYTES;
		} else if (obj instanceof Long) {
			f.set(this, buf.getLong(read));
			read += Long.BYTES;
		} else if (obj instanceof Float) {
			f.set(this, buf.getFloat(read));
			read += Float.BYTES;
		} else if (obj instanceof Double) {
			f.set(this, buf.getDouble(read));
			read += Double.BYTES;
		} else if (obj instanceof String) {
			if (!f.isAnnotationPresent(StringLength.class))
				return;
			int len = f.getAnnotation(StringLength.class).size();
			String charset = f.getAnnotation(StringLength.class).charset();

			byte[] strbuf = new byte[len];
			buf.get(strbuf);
			try {
				String tempstr = new String(strbuf, charset);
				f.set(this, tempstr);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("UnsupportedEncodingException (" + charset + ")! " + f.getName() + " of " + obj.getClass().getName());
			}
			read += len;
		} else if (BufferStruct.class.isAssignableFrom(f.getType()))
			((BufferStruct) obj).readFrom(buf, read);
		else
			throw new IllegalArgumentException("Non-primitive types are not implemented yet! " + f.getName() + " of " + obj.getClass().getName());
	}

	public int size() {
		if (size == 0)
			countStruct();
		return this.size;
	}

	public void afterRead() {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface SkipField {
		public boolean skip() default true;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface UnsignedField {
		int value() default 1;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface StringLength {
		int size() default 1;
		String charset() default "UTF-8";
	}

}
