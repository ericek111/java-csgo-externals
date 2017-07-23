package me.lixko.csgoexternals.util;

public class MathUtils {

	public static double M_RADPI = 180 / Math.PI;

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
		if (angles[1] < -180.0f)
			angles[1] += 360.0f;
		if (angles[1] > 180.0f)
			angles[1] -= 360.0f;
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

	public static float[] add(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			a[i] += b[i];
		return a;
	}

	public static float[] multiply(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			a[i] *= b[i];
		return a;
	}

	public static float[] abs(float[] x) {
		for (int i = 0; i < x.length; i++)
			x[i] = Math.abs(x[i]);
		return x;
	}

	public static float[] VectorAngles(float[] dir) {
		float[] angles = new float[2];
		double hyp = Math.sqrt((dir[0] * dir[0]) + (dir[1] * dir[1]));
		angles[0] = (float) (Math.atan(dir[2] / hyp) * M_RADPI);
		angles[1] = (float) (Math.atan(dir[1] / dir[0]) * M_RADPI);
		if (dir[0] >= 0.0)
			angles[1] += 180.0f;
		return angles;
	}

	public static float[] CalcAngle(float[] playerpos, float[] enemypos) {
		float[] aim = new float[2];
		float[] delta = subtract(playerpos, enemypos);
		double hyp = Math.sqrt((delta[0] * delta[0]) + (delta[1] * delta[1]));
		aim[0] = (float) (Math.atan(delta[2] / hyp) * M_RADPI);
		aim[1] = (float) (Math.atan(delta[1] / delta[0]) * M_RADPI);
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
}
