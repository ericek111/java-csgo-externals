package me.lixko.csgoexternals.util;

public class MathUtils {

	public static double calculateDistance(double x1, double y1, double x2, double y2) {
	    //return Math.hypot(x2 - x1, y2 - y1); | What's faster?
	    return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
	}
	
	public static double calculateAngleDeg(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return calculateAngleRad(x1, y1, x2, y2, x3, y3, x4, y4) / (Math.PI*2) * 360f;
	}
	
	public static double calculateAngleRad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return (Math.atan2(y1 - y2, x1 - x2) - Math.atan2(y3 - y4, x3 - x4));
	}
	
	public static double normalizeAngle(double angle) {
		return ((angle % 360) + 360) % 360;
	}
}
