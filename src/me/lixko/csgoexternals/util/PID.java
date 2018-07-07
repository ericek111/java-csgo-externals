package me.lixko.csgoexternals.util;

public class PID {
	
	float kP, kI, kD;
	float prev_err, integral;
	float last_step;
	
	public PID(float kP, float kI, float kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}
	
	public float step(float err) {
		//System.out.println(err);
		float timeNow = System.currentTimeMillis() / 1000f;
		float dt = timeNow - last_step;
		if (dt > 0.1) dt = 0.1f; // cap dt at 0.1 second to avoid extremely big dt, which could cause the output to drift uncontrollably.
		 
		last_step = timeNow;
	 
		float derivative = err - prev_err;
		if (dt > 0.0001) { // prevent division by zero
			integral += err * dt;
			derivative /= dt;
		}
	 
		float output = kP * err + kI * integral + kD * derivative;
		prev_err = err;
		
		return output;
	}
	
	public void clear() {
		prev_err = 0;
		integral = 0;
	}
	
	public void setPID(float kP, float kI, float kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}
	
	public void changePID(float kP, float kI, float kD) {
		this.kP += kP;
		this.kI += kI;
		this.kD += kD;
	}
	
	public float getP() {
		return this.kP;
	}
	
	public float getI() {
		return this.kI;
	}
	
	public float getD() {
		return this.kD;
	}
}
