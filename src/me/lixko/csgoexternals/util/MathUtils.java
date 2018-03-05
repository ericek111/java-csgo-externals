package me.lixko.csgoexternals.util;

public class MathUtils {

	public static double M_RADPI = 180f / Math.PI;

	public static float[] calculateAngle(float[] a, float[] b) {
		if (a.length > 2 && b.length > 2) {

		} else if (a.length > 1 && b.length > 1) {
			float angle = (float) Math.toDegrees(Math.atan2(a[0] - b[0], a[1] - b[1]));

			if (angle < 0) {
				angle += 360;
			}

			return new float[] { angle };
		}
		return new float[] {};
	}

	public static double calculateDistance(double x1, double y1, double x2, double y2) {
		// return Math.hypot(x2 - x1, y2 - y1); | What's faster?
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public static double calculateAngleDeg(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return calculateAngleRad(x1, y1, x2, y2, x3, y3, x4, y4) / (Math.PI * 2) * 360f;
	}

	public static double calculateAngleRad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return (Math.atan2(y1 - y2, x1 - x2) - Math.atan2(y3 - y4, x3 - x4));
	}

	public static float VecDist(float[] a, float[] b) {
		return (float) Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2) + Math.pow(a[2] - b[2], 2));
	}

	public static float VecLength(float[] a) {
		return (float) Math.sqrt(Math.pow(a[0], 2) + Math.pow(a[1], 2) + Math.pow(a[2], 2));
	}

	public static float AngleLength(float[] a) {
		return (float) Math.sqrt(Math.pow(a[0], 2) + Math.pow(a[1], 2));
	}

	public static double normalizeAngle(double angle) {
		return ((angle % 360) + 360) % 360;
	}

	public static float[] ClampAngle(float[] angles) {
		if (angles[0] < -89.0f)
			angles[0] = 89.0f;
		if (angles[0] > 89.0f)
			angles[0] = 89.0f;
		while (angles[1] < -180.0f)
			angles[1] += 360.0f;
		while (angles[1] > 180.0f)
			angles[1] -= 360.0f;
		while (angles[2] < -180.0f)
			angles[2] += 360.0f;
		while (angles[2] > 180.0f)
			angles[2] -= 360.0f;
		return angles;
	}

	public static float[] normalizeVector(float[] vec) {
		float l = VecLength(vec);
		if (l != 0.0f) {
			for (int i = 0; i < vec.length; i++)
				vec[i] /= l;
		} else {
			for (int i = 0; i < vec.length; i++)
				vec[i] /= l;
			vec[vec.length - 1] = 1.0f;
		}
		return vec;
	}

	public static float[] subtract(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			a[i] -= b[i];
		return a;
	}

	public static float[] subtract(float[] a, float b) {
		for (int i = 0; i < a.length; i++)
			a[i] -= b;
		return a;
	}

	public static float[] add(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			a[i] += b[i];
		return a;
	}

	public static float[] add(float[] a, float b) {
		for (int i = 0; i < a.length; i++)
			a[i] += b;
		return a;
	}

	public static float[] multiply(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			a[i] *= b[i];
		return a;
	}

	public static float[] multiply(float[] a, float b) {
		for (int i = 0; i < a.length; i++)
			a[i] *= b;
		return a;
	}

	public static float[] divide(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			a[i] /= b[i];
		return a;
	}

	public static float[] divide(float[] a, float b) {
		for (int i = 0; i < a.length; i++)
			a[i] /= b;
		return a;
	}

	public static float[] abs(float[] x) {
		for (int i = 0; i < x.length; i++)
			x[i] = Math.abs(x[i]);
		return x;
	}

	public static float[] negate(float[] a) {
		for (int i = 0; i < a.length; i++)
			a[i] = -a[i];
		return a;
	}

	public static float[] csubtract(float[] a, float[] b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] -= b[i];
		return x;
	}

	public static float[] cadd(float[] a, float[] b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] += b[i];
		return x;
	}

	public static float[] cmultiply(float[] a, float[] b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] *= b[i];
		return x;
	}

	public static float[] cdivide(float[] a, float[] b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] /= b[i];
		return x;
	}

	public static float[] csubtract(float[] a, float b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] -= b;
		return x;
	}

	public static float[] cadd(float[] a, float b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] += b;
		return x;
	}

	public static float[] cmultiply(float[] a, float b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] *= b;
		return x;
	}

	public static float[] cdivide(float[] a, float b) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] /= b;
		return x;
	}

	public static float[] cabs(float[] a) {
		float[] x = a.clone();
		for (int i = 0; i < x.length; i++)
			x[i] = Math.abs(a[i]);
		return x;
	}

	public static float[] cnegate(float[] a) {
		float[] x = a.clone();
		for (int i = 0; i < a.length; i++)
			x[i] = -a[i];
		return x;
	}

	public static float dotProduct(float[] a, float[] b) {
		float result = 0f;
		for (int i = 0; i < Math.min(a.length, b.length); i++)
			result += a[i] * b[i];
		return result;
	}

	public static boolean pointIsInRange(float[] vec, short[] min, short[] max) {
		//@formatter:off
		if (   (min[0] <= vec[0] && vec[0] <= max[0] &&
		        min[1] <= vec[1] && vec[1] <= max[1] &&
		        min[2] <= vec[2] && vec[2] <= max[2]) ||
		       (min[0] >= vec[0] && vec[0] >= max[0] &&
		        min[1] >= vec[1] && vec[1] >= max[1] &&
		        min[2] >= vec[2] && vec[2] >= max[2]))
			return true;
		else
			return false;
		//@formatter:on
	}

	public static float[] VectorAngles(float[] dir) {
		float[] angles = new float[3];
		double hyp = Math.sqrt((dir[0] * dir[0]) + (dir[1] * dir[1]));
		angles[0] = (float) (Math.atan(dir[2] / hyp) * M_RADPI);
		angles[1] = (float) (Math.atan(dir[1] / dir[0]) * M_RADPI);
		if (dir[0] >= 0.0)
			angles[1] += 180.0f;
		return angles;
	}

	public static float[] CalcAngle(float[] playerpos, float[] enemypos) {
		float[] aim = new float[3];
		float[] delta = csubtract(playerpos, enemypos);
		double hyp = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);
		aim[0] = (float) (Math.atan(delta[2] / hyp) * 180f / Math.PI);
		aim[1] = (float) (Math.atan(delta[1] / delta[0]) * 180f / Math.PI);
		if (delta[0] >= 0.0)
			aim[1] += 180.0f;
		return aim;
	}

	public static float GetFov(float[] eyePosition, float[] playerHeadPosition, float[] entityHeadPosition) {
		float[] newAngles = CalcAngle(playerHeadPosition, entityHeadPosition);
		float yaw = ((float) Math.sin(Math.abs(eyePosition[1] - newAngles[1]) * Math.PI / 180)) * VecDist(playerHeadPosition, entityHeadPosition);
		float pitch = ((float) Math.sin(Math.abs(eyePosition[0] - newAngles[0]) * Math.PI / 180)) * VecDist(playerHeadPosition, entityHeadPosition);
		return (float) Math.sqrt((yaw * yaw) + (pitch * pitch));
	}

	public static float GetArmourHealth(float flDamage, int ArmorValue) {
		float flCurDamage = flDamage;

		if (flCurDamage == 0.0f || ArmorValue == 0)
			return flCurDamage;

		float flArmorRatio = 0.5f;
		float flArmorBonus = 0.5f;
		float flNew = flCurDamage * flArmorRatio;
		float flArmor = (flCurDamage - flNew) * flArmorBonus;

		if (flArmor > ArmorValue) {
			flArmor = ArmorValue * (1.0f / flArmorBonus);
			flNew = flCurDamage - flArmor;
		}

		return flNew;
	}

	public static double exp(double val) {
		final long tmp = (long) (1512775 * val + (1072693248 - 60801));
		return Double.longBitsToDouble(tmp << 32);
	}

	public static float[] worldVecToOpenGL(float[] v) {
		if (v.length < 3)
			return new float[] { -1, -1, -1 };
		return new float[] { v[0], v[2], v[1] };
	}

	public static class Matrix4x4 {

		// http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-17-quaternions/
		// https://www.mathsisfun.com/algebra/matrix-inverse.html

		public static float[] perspective(float fov, float aspect, float near, float far) {
			float[] perspective = new float[4 * 4];
			float f = (float) (1f / Math.tan(Math.toRadians(fov) / 2f));
			float frustum = near - far;

			perspective[0] = f / aspect;
			perspective[4 * 1 + 1] = f;
			perspective[4 * 2 + 2] = (far + near) / frustum;
			perspective[4 * 3 + 2] = -1f;
			perspective[4 * 2 + 3] = (2f * far * near) / frustum;
			perspective[4 * 3 + 3] = 0f;

			return perspective;
		}

		public static float[] translate(float x, float y, float z) {
			float[] translation = new float[4 * 4];
			translation[3] = x;
			translation[4 * 1 + 3] = y;
			translation[4 * 2 + 3] = y;
			return translation;
		}

		public static float[] translate(float[] in, float x, float y, float z) {
			float[] res = in.clone();
			// translation matrix elements:[4*0 + 0],[4*1 + 1],[4*2 + 2],[4*3 + 3] = 1
			//[4*3 + 0] = x,[4*3 + 1] = y,[4*3 + 2] = z, all others = 0
			res[4 * 3 + 0] = in[4 * 0 + 0] * x + in[4 * 1 + 0] * y + in[4 * 2 + 0] * z + in[4 * 3 + 0];
			res[4 * 3 + 1] = in[4 * 0 + 1] * x + in[4 * 1 + 1] * y + in[4 * 2 + 1] * z + in[4 * 3 + 1];
			res[4 * 3 + 2] = in[4 * 0 + 2] * x + in[4 * 1 + 2] * y + in[4 * 2 + 2] * z + in[4 * 3 + 2];
			res[4 * 3 + 3] = in[4 * 0 + 3] * x + in[4 * 1 + 3] * y + in[4 * 2 + 3] * z + in[4 * 3 + 3];
			return res;
		}

		public static float[] translate(float[] in, float[] vec) {
			return translate(in, vec[0], vec[1], vec[2]);
		}

		public static float[] rotate(float angle, float x, float y, float z) {
			float[] rotation = new float[4 * 4];

			float c = (float) Math.cos(Math.toRadians(angle));
			float s = (float) Math.sin(Math.toRadians(angle));
			float[] vec = new float[] { x, y, z };

			if (MathUtils.VecLength(vec) != 1f) {
				vec = MathUtils.normalizeVector(vec);
				x = vec[0];
				y = vec[1];
				z = vec[2];
			}

			rotation[4 * 0 + 0] = x * x * (1f - c) + c;
			rotation[4 * 1 + 0] = y * x * (1f - c) + z * s;
			rotation[4 * 2 + 0] = x * z * (1f - c) - y * s;
			rotation[4 * 0 + 1] = x * y * (1f - c) - z * s;
			rotation[4 * 1 + 1] = y * y * (1f - c) + c;
			rotation[4 * 2 + 1] = y * z * (1f - c) + x * s;
			rotation[4 * 0 + 2] = x * z * (1f - c) + y * s;
			rotation[4 * 1 + 2] = y * z * (1f - c) - x * s;
			rotation[4 * 2 + 2] = z * z * (1f - c) + c;

			return rotation;
		}

		//@formatter:off
		public static float determinant(float[] m) {
			float f =
				m[4*0 + 0]
					* ((m[4*1 + 1] * m[4*2 + 2] * m[4*3 + 3] + m[4*1 + 2] * m[4*2 + 3] * m[4*3 + 1] + m[4*1 + 3] * m[4*2 + 1] * m[4*3 + 2])
						- m[4*1 + 3] * m[4*2 + 2] * m[4*3 + 1]
						- m[4*1 + 1] * m[4*2 + 3] * m[4*3 + 2]
						- m[4*1 + 2] * m[4*2 + 1] * m[4*3 + 3]);
			f -= m[4*0 + 1]
				* ((m[4*1 + 0] * m[4*2 + 2] * m[4*3 + 3] + m[4*1 + 2] * m[4*2 + 3] * m[4*3 + 0] + m[4*1 + 3] * m[4*2 + 0] * m[4*3 + 2])
					- m[4*1 + 3] * m[4*2 + 2] * m[4*3 + 0]
					- m[4*1 + 0] * m[4*2 + 3] * m[4*3 + 2]
					- m[4*1 + 2] * m[4*2 + 0] * m[4*3 + 3]);
			f += m[4*0 + 2]
				* ((m[4*1 + 0] * m[4*2 + 1] * m[4*3 + 3] + m[4*1 + 1] * m[4*2 + 3] * m[4*3 + 0] + m[4*1 + 3] * m[4*2 + 0] * m[4*3 + 1])
					- m[4*1 + 3] * m[4*2 + 1] * m[4*3 + 0]
					- m[4*1 + 0] * m[4*2 + 3] * m[4*3 + 1]
					- m[4*1 + 1] * m[4*2 + 0] * m[4*3 + 3]);
			f -= m[4*0 + 3]
				* ((m[4*1 + 0] * m[4*2 + 1] * m[4*3 + 2] + m[4*1 + 1] * m[4*2 + 2] * m[4*3 + 0] + m[4*1 + 2] * m[4*2 + 0] * m[4*3 + 1])
					- m[4*1 + 2] * m[4*2 + 1] * m[4*3 + 0]
					- m[4*1 + 0] * m[4*2 + 2] * m[4*3 + 1]
					- m[4*1 + 1] * m[4*2 + 0] * m[4*3 + 2]);
			return f;
		}
		
		
		private static float determinant3x3(float t00, float t01, float t02,
			     float t10, float t11, float t12,
			     float t20, float t21, float t22) {
			return   t00 * (t11 * t22 - t12 * t21)
			       + t01 * (t12 * t20 - t10 * t22)
			       + t02 * (t10 * t21 - t11 * t20);
		}
		//@formatter:on

		public static float[] invert(float[] src) {
			float determinant = determinant(src);

			if (determinant != 0) {
				/*
				 * [4*0 + 0][4*0 + 1][4*0 + 2][4*0 + 3]
				 * [4*1 + 0][4*1 + 1][4*1 + 2][4*1 + 3]
				 * [4*2 + 0][4*2 + 1][4*2 + 2][4*2 + 3]
				 * [4*3 + 0][4*3 + 1][4*3 + 2][4*3 + 3]
				 */
				float[] dest = new float[4 * 4];
				float determinant_inv = 1f / determinant;

				// first row
				float t00 = determinant3x3(src[4 * 1 + 1], src[4 * 1 + 2], src[4 * 1 + 3], src[4 * 2 + 1], src[4 * 2 + 2], src[4 * 2 + 3], src[4 * 3 + 1], src[4 * 3 + 2], src[4 * 3 + 3]);
				float t01 = -determinant3x3(src[4 * 1 + 0], src[4 * 1 + 2], src[4 * 1 + 3], src[4 * 2 + 0], src[4 * 2 + 2], src[4 * 2 + 3], src[4 * 3 + 0], src[4 * 3 + 2], src[4 * 3 + 3]);
				float t02 = determinant3x3(src[4 * 1 + 0], src[4 * 1 + 1], src[4 * 1 + 3], src[4 * 2 + 0], src[4 * 2 + 1], src[4 * 2 + 3], src[4 * 3 + 0], src[4 * 3 + 1], src[4 * 3 + 3]);
				float t03 = -determinant3x3(src[4 * 1 + 0], src[4 * 1 + 1], src[4 * 1 + 2], src[4 * 2 + 0], src[4 * 2 + 1], src[4 * 2 + 2], src[4 * 3 + 0], src[4 * 3 + 1], src[4 * 3 + 2]);
				// second row
				float t10 = -determinant3x3(src[4 * 0 + 1], src[4 * 0 + 2], src[4 * 0 + 3], src[4 * 2 + 1], src[4 * 2 + 2], src[4 * 2 + 3], src[4 * 3 + 1], src[4 * 3 + 2], src[4 * 3 + 3]);
				float t11 = determinant3x3(src[4 * 0 + 0], src[4 * 0 + 2], src[4 * 0 + 3], src[4 * 2 + 0], src[4 * 2 + 2], src[4 * 2 + 3], src[4 * 3 + 0], src[4 * 3 + 2], src[4 * 3 + 3]);
				float t12 = -determinant3x3(src[4 * 0 + 0], src[4 * 0 + 1], src[4 * 0 + 3], src[4 * 2 + 0], src[4 * 2 + 1], src[4 * 2 + 3], src[4 * 3 + 0], src[4 * 3 + 1], src[4 * 3 + 3]);
				float t13 = determinant3x3(src[4 * 0 + 0], src[4 * 0 + 1], src[4 * 0 + 2], src[4 * 2 + 0], src[4 * 2 + 1], src[4 * 2 + 2], src[4 * 3 + 0], src[4 * 3 + 1], src[4 * 3 + 2]);
				// third row
				float t20 = determinant3x3(src[4 * 0 + 1], src[4 * 0 + 2], src[4 * 0 + 3], src[4 * 1 + 1], src[4 * 1 + 2], src[4 * 1 + 3], src[4 * 3 + 1], src[4 * 3 + 2], src[4 * 3 + 3]);
				float t21 = -determinant3x3(src[4 * 0 + 0], src[4 * 0 + 2], src[4 * 0 + 3], src[4 * 1 + 0], src[4 * 1 + 2], src[4 * 1 + 3], src[4 * 3 + 0], src[4 * 3 + 2], src[4 * 3 + 3]);
				float t22 = determinant3x3(src[4 * 0 + 0], src[4 * 0 + 1], src[4 * 0 + 3], src[4 * 1 + 0], src[4 * 1 + 1], src[4 * 1 + 3], src[4 * 3 + 0], src[4 * 3 + 1], src[4 * 3 + 3]);
				float t23 = -determinant3x3(src[4 * 0 + 0], src[4 * 0 + 1], src[4 * 0 + 2], src[4 * 1 + 0], src[4 * 1 + 1], src[4 * 1 + 2], src[4 * 3 + 0], src[4 * 3 + 1], src[4 * 3 + 2]);
				// fourth row
				float t30 = -determinant3x3(src[4 * 0 + 1], src[4 * 0 + 2], src[4 * 0 + 3], src[4 * 1 + 1], src[4 * 1 + 2], src[4 * 1 + 3], src[4 * 2 + 1], src[4 * 2 + 2], src[4 * 2 + 3]);
				float t31 = determinant3x3(src[4 * 0 + 0], src[4 * 0 + 2], src[4 * 0 + 3], src[4 * 1 + 0], src[4 * 1 + 2], src[4 * 1 + 3], src[4 * 2 + 0], src[4 * 2 + 2], src[4 * 2 + 3]);
				float t32 = -determinant3x3(src[4 * 0 + 0], src[4 * 0 + 1], src[4 * 0 + 3], src[4 * 1 + 0], src[4 * 1 + 1], src[4 * 1 + 3], src[4 * 2 + 0], src[4 * 2 + 1], src[4 * 2 + 3]);
				float t33 = determinant3x3(src[4 * 0 + 0], src[4 * 0 + 1], src[4 * 0 + 2], src[4 * 1 + 0], src[4 * 1 + 1], src[4 * 1 + 2], src[4 * 2 + 0], src[4 * 2 + 1], src[4 * 2 + 2]);

				// transpose and divide by the determinant
				dest[4 * 0 + 0] = t00 * determinant_inv;
				dest[4 * 1 + 1] = t11 * determinant_inv;
				dest[4 * 2 + 2] = t22 * determinant_inv;
				dest[4 * 3 + 3] = t33 * determinant_inv;
				dest[4 * 0 + 1] = t10 * determinant_inv;
				dest[4 * 1 + 0] = t01 * determinant_inv;
				dest[4 * 2 + 0] = t02 * determinant_inv;
				dest[4 * 0 + 2] = t20 * determinant_inv;
				dest[4 * 1 + 2] = t21 * determinant_inv;
				dest[4 * 2 + 1] = t12 * determinant_inv;
				dest[4 * 0 + 3] = t30 * determinant_inv;
				dest[4 * 3 + 0] = t03 * determinant_inv;
				dest[4 * 1 + 3] = t31 * determinant_inv;
				dest[4 * 3 + 1] = t13 * determinant_inv;
				dest[4 * 3 + 2] = t23 * determinant_inv;
				dest[4 * 2 + 3] = t32 * determinant_inv;
				return dest;
			} else
				return null;
		}

		public static float[] mul(float[]... mats) {
			float[] res = mats[0];
			for (int i = 1; i < mats.length; i++) {
				MathUtils.Matrix4x4.mul(res, mats[i]);
			}
			return res;
		}

		public static float[] mul(float[] left, float[] right) {
			float[] dest = new float[4 * 4];
			dest[4 * 0 + 0] = left[4 * 0 + 0] * right[4 * 0 + 0] + left[4 * 1 + 0] * right[4 * 0 + 1] + left[4 * 2 + 0] * right[4 * 0 + 2] + left[4 * 3 + 0] * right[4 * 0 + 3];
			dest[4 * 0 + 1] = left[4 * 0 + 1] * right[4 * 0 + 0] + left[4 * 1 + 1] * right[4 * 0 + 1] + left[4 * 2 + 1] * right[4 * 0 + 2] + left[4 * 3 + 1] * right[4 * 0 + 3];
			dest[4 * 0 + 2] = left[4 * 0 + 2] * right[4 * 0 + 0] + left[4 * 1 + 2] * right[4 * 0 + 1] + left[4 * 2 + 2] * right[4 * 0 + 2] + left[4 * 3 + 2] * right[4 * 0 + 3];
			dest[4 * 0 + 3] = left[4 * 0 + 3] * right[4 * 0 + 0] + left[4 * 1 + 3] * right[4 * 0 + 1] + left[4 * 2 + 3] * right[4 * 0 + 2] + left[4 * 3 + 3] * right[4 * 0 + 3];
			dest[4 * 1 + 0] = left[4 * 0 + 0] * right[4 * 1 + 0] + left[4 * 1 + 0] * right[4 * 1 + 1] + left[4 * 2 + 0] * right[4 * 1 + 2] + left[4 * 3 + 0] * right[4 * 1 + 3];
			dest[4 * 1 + 1] = left[4 * 0 + 1] * right[4 * 1 + 0] + left[4 * 1 + 1] * right[4 * 1 + 1] + left[4 * 2 + 1] * right[4 * 1 + 2] + left[4 * 3 + 1] * right[4 * 1 + 3];
			dest[4 * 1 + 2] = left[4 * 0 + 2] * right[4 * 1 + 0] + left[4 * 1 + 2] * right[4 * 1 + 1] + left[4 * 2 + 2] * right[4 * 1 + 2] + left[4 * 3 + 2] * right[4 * 1 + 3];
			dest[4 * 1 + 3] = left[4 * 0 + 3] * right[4 * 1 + 0] + left[4 * 1 + 3] * right[4 * 1 + 1] + left[4 * 2 + 3] * right[4 * 1 + 2] + left[4 * 3 + 3] * right[4 * 1 + 3];
			dest[4 * 2 + 0] = left[4 * 0 + 0] * right[4 * 2 + 0] + left[4 * 1 + 0] * right[4 * 2 + 1] + left[4 * 2 + 0] * right[4 * 2 + 2] + left[4 * 3 + 0] * right[4 * 2 + 3];
			dest[4 * 2 + 1] = left[4 * 0 + 1] * right[4 * 2 + 0] + left[4 * 1 + 1] * right[4 * 2 + 1] + left[4 * 2 + 1] * right[4 * 2 + 2] + left[4 * 3 + 1] * right[4 * 2 + 3];
			dest[4 * 2 + 2] = left[4 * 0 + 2] * right[4 * 2 + 0] + left[4 * 1 + 2] * right[4 * 2 + 1] + left[4 * 2 + 2] * right[4 * 2 + 2] + left[4 * 3 + 2] * right[4 * 2 + 3];
			dest[4 * 2 + 3] = left[4 * 0 + 3] * right[4 * 2 + 0] + left[4 * 1 + 3] * right[4 * 2 + 1] + left[4 * 2 + 3] * right[4 * 2 + 2] + left[4 * 3 + 3] * right[4 * 2 + 3];
			dest[4 * 3 + 0] = left[4 * 0 + 0] * right[4 * 3 + 0] + left[4 * 1 + 0] * right[4 * 3 + 1] + left[4 * 2 + 0] * right[4 * 3 + 2] + left[4 * 3 + 0] * right[4 * 3 + 3];
			dest[4 * 3 + 1] = left[4 * 0 + 1] * right[4 * 3 + 0] + left[4 * 1 + 1] * right[4 * 3 + 1] + left[4 * 2 + 1] * right[4 * 3 + 2] + left[4 * 3 + 1] * right[4 * 3 + 3];
			dest[4 * 3 + 2] = left[4 * 0 + 2] * right[4 * 3 + 0] + left[4 * 1 + 2] * right[4 * 3 + 1] + left[4 * 2 + 2] * right[4 * 3 + 2] + left[4 * 3 + 2] * right[4 * 3 + 3];
			dest[4 * 3 + 3] = left[4 * 0 + 3] * right[4 * 3 + 0] + left[4 * 1 + 3] * right[4 * 3 + 1] + left[4 * 2 + 3] * right[4 * 3 + 2] + left[4 * 3 + 3] * right[4 * 3 + 3];
			return dest;
		}

	}
}
