package me.lixko.csgoexternals.util.bsp;

import me.lixko.csgoexternals.util.MathUtils;

public class Camera {
	public float[] position = new float[3];

	public float zNear, zFar, aspectRatio;
	public float fov, pitch, yaw;
	public float maxAngle;

	public Camera(int width, int height) {
		position = new float[] { 0, 0, 1 };
		yaw = 0.0f;
		pitch = 0.0f;
		maxAngle = 89.0f;
		fov = 70.0f;
		zNear = 0.1f;
		zFar = 10000.0f;
		aspectRatio = ((float) width / (float) height);
	}

	// TODO
	public void LookAt(float[] pos) {
		if (pos == position)
			return;
	}

	public float getFOV() {
		return this.fov;
	}

	public void setFOV(float fov) {
		this.fov = fov;
	}

	public void addPosition(float[] off) {
		MathUtils.add(position, off);
	}

	public float[] getPosition() {
		return this.position;
	}

	public void setPosition(float[] off) {
		position = off;
	}

	public void setOrientation(float pitch, float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
		normalizeAngles();
	}

	public void addOrientation(float pitch, float yaw) {
		this.pitch += pitch;
		this.yaw += yaw;
		normalizeAngles();
	}
	
	public float[] getProjection() {
		return MathUtils.Matrix4x4.perspective(fov, aspectRatio, zNear, zFar);
	}
	
	public float[] getMatrix() {
		return MathUtils.Matrix4x4.mul(getProjection(), getView());
	}
	
	public float[] getView() {
		return MathUtils.Matrix4x4.mul(getOrientation(), MathUtils.Matrix4x4.translate(new float[4*4], -position[0], -position[1], -position[2]));
	}
	
	public float[] getOrientation()	{
		float[] orientation = MathUtils.Matrix4x4.rotate(pitch - 90.0f, 1, 0, 0);
		orientation = MathUtils.Matrix4x4.mul(orientation, MathUtils.Matrix4x4.rotate(yaw + 90.0f, 0, 0, 1));
		return orientation;
	}
    
	public void normalizeAngles() {
		yaw = yaw % 360f;
		if (yaw < 0.0f)
			yaw += 360;
		if (pitch > maxAngle)
			pitch = maxAngle;
		else if (pitch < -maxAngle)
			pitch = -maxAngle;
	}

}
