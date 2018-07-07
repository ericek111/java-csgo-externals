package me.lixko.csgoexternals.util;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class GLGraph {
	private GL2 gl;
	private int maxSamples;
	private int vboVertices = -1, vboIndices = -1, vboColors = -1;
	private FloatBuffer vertex_data;
	
	private float startX = 0f, startY = 0f, sizeX = 0f, sizeY = 0f, scaleY = 1f;
	private float colorR = 1f, colorG = 0f, colorB = 0f, colorA = 1f, lineWidth = 1f;
	private boolean smoothLines = false;
	private int curSample = 0;
	
	private boolean loaded = false, updateGL = false;
	private long firstSampleTime;
	
	private boolean oscillationFinder = false, drawCrossings = true;
	private float lastOscillationPeriod = -1f;
	private float peakThreshold = 0f;
	private float[][] valuePairs;
	private FloatBuffer crossing_data;
	
	public GLGraph(GL2 gl, float startX, float startY, float sizeX, float sizeY, float scaleY, int samples) {
		this.startX = startX;
		this.sizeX = sizeX;
		this.startY = startY;
		this.sizeY = sizeY;
		this.scaleY = scaleY;
		this.maxSamples = samples;
		this.gl = gl;
		vertex_data = FloatBuffer.allocate(this.maxSamples * 2);
		// cannot have 1 crossing per sample
		crossing_data = FloatBuffer.allocate(this.maxSamples);
		valuePairs = new float[this.maxSamples][2];
	}
	
	public void putSample(float y) {
		vertex_data.position(curSample * 2);
		vertex_data.put(new float[] { startX + ((float)curSample / (float)maxSamples) * sizeX, startY + y * scaleY });
		
		if(curSample == 0)
			firstSampleTime = System.currentTimeMillis();
		
		if(this.oscillationFinder) {
			valuePairs[curSample][0] = (float)(System.currentTimeMillis() - firstSampleTime) / 1000f;
			valuePairs[curSample][1] = y;
		}
		
		curSample++;
		// if(!vertex_data.hasRemaining()) {
		if(curSample == maxSamples) {
			vertex_data.position(0);
			curSample = 0;
			if(oscillationFinder) {
				lastOscillationPeriod = this.getOscillationPeriod();
			}
		}
	}
	
	public float getOscillationPeriod() {
		ArrayList<Float> crossTimes = new ArrayList<>();
		
		crossing_data.clear();
		
		float prevThreshCrossingTime = 0f;
		boolean risingEdge = true; // false = on falling edge
		int edgeSamples = 1; // how many consecutive samples need to match to consider an edge rising/falling and try to trigger
		int triggeredTimes = 0;
		
		for(int i = 1; i < valuePairs.length; i++) {
			// break on old entries
			if(valuePairs[i - 1][0] > valuePairs[i][0])
				break;
			// break when we cannot determine waveform properties
			if(valuePairs.length - i < edgeSamples)
				break;
			boolean cg = true;
			for(int e = 1; e < edgeSamples; e++) {
				// rising: previous > next => break; falling: previous < this => break; 
				if(risingEdge && !(valuePairs[i + e][1] > valuePairs[i + e - 1][1]) || !risingEdge && !(valuePairs[i + e][1] < valuePairs[i + e - 1][1])) {
					cg = false;
					break;
				}
			}
			if(!cg)
				continue;
			
			// previous sample below threshold?
			if(risingEdge && (valuePairs[i - 1][1] < peakThreshold && valuePairs[i][1] >= peakThreshold) || !risingEdge && (valuePairs[i - 1][1] > peakThreshold && valuePairs[i][1] <= peakThreshold)) {
				float crossingPeriod = valuePairs[i][0] - prevThreshCrossingTime;
				prevThreshCrossingTime = valuePairs[i][0];
				triggeredTimes++;
				if(triggeredTimes == 1)
					continue;
				crossTimes.add(crossingPeriod);
				crossing_data.put(new float[] { vertex_data.get(i * 2), vertex_data.get(i * 2 + 1) });
			}
		}
		
		// average out gathered periods
		double periodSum = 0.0;
		for(float period : crossTimes) {
			periodSum += period;
		}
		return (float) (periodSum / (double) crossTimes.size());
	}
	
	public void pushSample(float y) {
		// TODO: gl.glMapBufferRange(GL.GL_ARRAY_BUFFER, (vertex_data.position() - 3) * FLOAT.BYTES, 3 * Float.BYTES, GL.GL_MAP_UNSYNCHRONIZED_BIT);
		this.putSample(y);
	}
	
	public void render() {
		if(!loaded)
			loadGL();
		
		if(updateGL)
			_update();
				
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
		
		if(drawCrossings) {
			gl.glBegin(GL2.GL_POINTS);
			for(int i = 0; i < crossing_data.position(); i += 2) {
				gl.glVertex2f(crossing_data.get(i), crossing_data.get(i + 1));
			}
			gl.glEnd();
		}
		
		DrawUtils.setColor(0.8f, 0.8f, 0.8f, 0.8f);
		DrawUtils.drawLinew((int) startX, (int) startY, (int) sizeX, 1);
				
		/*gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertices);
		gl.glVertexPointer(2, GL.GL_FLOAT, 0, 0);
		
		// gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboColors);
		// gl.glColorPointer(3, GL.GL_FLOAT, 0, 0);
		
		// for shaders:
		// gl.glEnableVertexAttribArray(vboVertices);
		// gl.glVertexAttribPointer(vboVertices, 2, GL.GL_FLOAT, false, 0, 0);
		
		if(smoothLines) {
			gl.glEnable(GL.GL_BLEND);
			gl.glEnable(GL.GL_LINE_SMOOTH);
			gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		// poor man's shadow
		// gl.glLineWidth(3f);
		// gl.glColor4f(0f, 0f, 0f, 0.8f);
		// gl.glDrawArrays(GL.GL_LINE_STRIP, 0, maxSamples);
		
		//l.glEnable(GL.GL_BLEND);
		gl.glLineWidth(lineWidth);
		gl.glColor4f(colorR, colorG, colorB, colorA);
		gl.glDrawArrays(GL.GL_LINE_LOOP, 0, maxSamples);
		gl.glDisable(GL.GL_BLEND);
		
		
		if(smoothLines) {
			gl.glDisable(GL.GL_BLEND);
			gl.glDisable(GL.GL_LINE_SMOOTH);
		}
		
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		//gl.glDisableClientState(GL2.GL_COLOR_ARRAY);*/
	}
	
	private void loadGL() {
		int[] temp = new int[2];
		gl.glGenBuffers(2, temp, 0);
		
		vboVertices = temp[0];
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertices);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, maxSamples * Float.BYTES, vertex_data, GL.GL_DYNAMIC_DRAW);

		/*vboColors = temp[1];
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboColors);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, color_data.capacity() * Float.BYTES, color_data, GL.GL_STATIC_DRAW);*/
	}
	
	public void update() {
		this.updateGL = true;
	}
	
	private void _update() {
		this.updateGL = false;
		gl.glMapBufferRange(GL.GL_ARRAY_BUFFER, 0, maxSamples * Float.BYTES, GL.GL_MAP_UNSYNCHRONIZED_BIT);
	}
	
	public void reset() {
		this.curSample = 0;
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
	
	public void setSmoothing(boolean smooth) {
		this.smoothLines = smooth;
	}
	
	public int maxSamples() {
		return this.maxSamples;
	}
	
	public int currentSample() {
		return this.curSample;
	}
	
	public void setOscillationFinder(boolean enable) {
		this.oscillationFinder = enable;
	}
	
	public void setOscFinderThreshold(float val) {
		this.peakThreshold = val;
	}
	
	public float getLastOscillationPeriod() {
		return this.lastOscillationPeriod;
	}
	
}
