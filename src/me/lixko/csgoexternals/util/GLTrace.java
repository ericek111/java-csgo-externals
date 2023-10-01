package me.lixko.csgoexternals.util;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL2;

public class GLTrace {
	private GL2 gl;
	private int maxSamples;
	private FloatBuffer vertex_data;
	
	private float startX = 0f, startY = 0f, sizeX = 0f, sizeY = 0f, scaleX = 1f, scaleY = 1f;
	private float colorR = 1f, colorG = 0f, colorB = 0f, colorA = 1f, lineWidth = 1f;
	private float maxX = 0f, maxY = 0f;
	private int curSample = 0;
	private boolean shouldAutoscale = false;
	
	private long firstSampleTime;
		
	public GLTrace(GL2 gl, float startX, float startY, float sizeX, float sizeY, int samples) {
		this.startX = startX;
		this.sizeX = sizeX;
		this.startY = startY;
		this.sizeY = sizeY;
		this.maxSamples = samples;
		this.gl = gl;
		vertex_data = FloatBuffer.allocate(this.maxSamples * 2);
	}
	
	public void putSample(float x, float y) {
		vertex_data.position(curSample * 2);
		float[] sample = new float[] { startX + sizeX / 2 + x * scaleX, startY + sizeY / 2 + y * scaleY };
		vertex_data.put(sample);		
		
		if(curSample == 0)
			firstSampleTime = System.currentTimeMillis();
		
		if (this.shouldAutoscale) {
			float absX = Math.abs(x), absY = Math.abs(y);
			if (absX * scaleX > sizeX / 2 || absY * scaleY > sizeY / 2) {
				float newScale = Math.max(absX / sizeX / 2, absY / sizeY / 2);
				scaleX = scaleY = newScale;
				System.out.println("scaling by " + newScale);
			}
		}

		curSample++;
		if(curSample == maxSamples) {
			vertex_data.position(0);
			curSample = 0;
		}
	}
	
	public void render() {
		DrawUtils.setLineWidth(1f);
		DrawUtils.setColor(0f, 0f, 0f, 0.8f);
		DrawUtils.fillRectanglew(startX, startY, sizeX, sizeY);
		
		DrawUtils.setColor(0.8f, 0.8f, 0.8f, 0.8f);
		DrawUtils.drawRectanglew(startX, startY, sizeX, sizeY);
		
		gl.glLineWidth(lineWidth);
		gl.glColor4f(colorR, colorG, colorB, colorA);
			
		gl.glBegin(GL2.GL_LINE_STRIP);
		for(int i = 0; i < curSample * 2; i += 2) {
			gl.glVertex2f(vertex_data.get(i), vertex_data.get(i + 1));
		}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP);
		for(int i = curSample * 2; i < maxSamples * 2; i += 2) {
			gl.glVertex2f(vertex_data.get(i), vertex_data.get(i + 1));
		}
		gl.glEnd();
	}
	
	public void reset() {
		this.curSample = 0;
		if (this.shouldAutoscale) {
			this.scaleX = 1f;
			this.scaleY = 1f;
		}
	}
	
	public void empty() {
		for (int i = 0; i < vertex_data.capacity(); i++)
			vertex_data.put(i, 0f);
		vertex_data.clear();
		this.reset();
	}
	
	public void setColor(float r, float g, float b, float a) {
		this.colorR = r;
		this.colorG = g;
		this.colorB = b;
		this.colorA = a;
	}
	
	public void setLineWidth(float w) {
		this.lineWidth = w;
	}
	
	public int maxSamples() {
		return this.maxSamples;
	}
	
	public int currentSample() {
		return this.curSample;
	}
	
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}
	
	public void setScaleY(float scaleY) {
		this.scaleY = scaleX;
	}
	
	public void setAutoscale(boolean autoscale) {
		this.shouldAutoscale = autoscale;
	}
	
}
