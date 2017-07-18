package me.lixko.csgoexternals.util;

public class MathUtils {

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

	public static double normalizeAngle(double angle) {
		return ((angle % 360) + 360) % 360;
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
