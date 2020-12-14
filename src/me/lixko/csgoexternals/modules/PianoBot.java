package me.lixko.csgoexternals.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.sdk.BSPFile.dmodel_t;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.XKeySym;
import me.lixko.csgoexternals.util.bsp.BSPParser;
import me.lixko.csgoexternals.util.bsp.BSPParser.Entity;

public class PianoBot extends Module {
	
	public static BSPParser bsp;
	
	ArrayList<Entity> pianoKeysEnts = new ArrayList<>();
	// HashMap<Integer, float[]> keysPos = new HashMap<>();
	float[][] keysPos = new float[25][3];
	boolean runningSequencer = false;
	Sequencer sequencer;
	List<Integer> noteQueue = Collections.synchronizedList(new ArrayList<Integer>());
	int transposeOctaves = -5;
	boolean onlyInclude = false;
	boolean skipD2 = true;
	int[] skipChannels = new int[] {};	

	Thread queueLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(5);
					if (!runningSequencer)
						continue;
					
					int noteKey = 0;

					synchronized(noteQueue) {
						if (noteQueue.isEmpty())
							continue;
						
						noteKey = noteQueue.remove(0);						
					}
					
					System.out.println("Playing " + noteKey);
					aimToKeyByIdx(noteKey);
					Thread.sleep(5);
					aimToKeyByIdx(noteKey);
					Thread.sleep(5);
					aimToKeyByIdx(noteKey);
					Thread.sleep(5);
					aimToKeyByIdx(noteKey);
					Thread.sleep(5);
					pressUse();
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		queueLoop.start();
	}

	@Override
	public void onRegister() {
		try {
			bsp = new BSPParser(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/maps/jb_undertale_v1e.bsp"));
			bsp.parse();
		} catch (IOException e) {
			e.printStackTrace();
			bsp = null;
			return;
		}
		
		for (Entity e : bsp.pEntities) {
			String name = e.properties.get("targetname");
			if (name == null) {
				continue;
			}
			
			String pfx = "piano_button";
			if (!name.startsWith(pfx)) {
				continue;
			}
						
			String modelStr = e.properties.get("model");
			if (!modelStr.startsWith("*")) {
				continue;
			}
			
			// e.properties.entrySet().forEach(entry -> System.out.println(entry.getKey() + " / " + entry.getValue()));
			pianoKeysEnts.add(e);
			
			int modelIdx = Integer.parseInt(modelStr.substring(1));
			// should be error-handled, but assume correct BSPs for now
			dmodel_t model = bsp.models[modelIdx];
			
			float[] mins = MathUtils.cadd(model.origin, model.mins);
			float[] maxs = MathUtils.cadd(model.origin, model.maxs);
			
			float minX = Math.min(mins[0], maxs[0]) + 2f;
			float maxY = Math.max(mins[2], maxs[2]);
			float z1 = Math.min(mins[1], maxs[1]);
			float z2 = Math.max(mins[1], maxs[1]);
			float zMid = (z2 + z1) / 2f;
			
			int keyNote = Integer.parseInt(name.substring(pfx.length()));
			keyNote -= 1;
			keysPos[keyNote][0] = minX;
			keysPos[keyNote][1] = zMid;
			keysPos[keyNote][2] = maxY;
			
			// keysPos.put(keyNote, new float[] { minX, zMid, maxY });
		}
		
		try {
			sequencer = MidiSystem.getSequencer(false);
			sequencer.open();
			
			// Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/beethoven_ode_to_joy.mid"));
			// Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/Ecce_homo_qui_est_faba.mid"));skipChannels = new int[] {6}; skipD2 = true;transposeOctaves = -6;
			// Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/mices.mid"));
			Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/wet_hands.mid"));
			//Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/sg_end.mid"));skipD2 = false;
			//meh Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/jurassic2.mid"));skipD2 = true;skipChannels = new int[] {0, 1}; onlyInclude = true; transposeOctaves = -6;
			//Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/jurassic.mid"));skipD2 = true;skipChannels = new int[] {0, 1}; onlyInclude = true;
			// Sequence sequence = MidiSystem.getSequence(new File("/home/erik/Documents/MIDI/mario.mid"));skipD2 = true;skipChannels = new int[] {0, 1}; onlyInclude = true; transposeOctaves = -6;
			sequencer.setSequence(sequence);
			this.prepareSequencer();
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private void prepareSequencer() throws MidiUnavailableException {
		final Map<Integer, Integer> patches = new HashMap<>();
		
		sequencer.getTransmitter().setReceiver(new Receiver() {
			int minOctave = 64;
			
			@Override
			public void send(MidiMessage message, long timestamp) {
				if ((message.getStatus() & 0xF0) == ShortMessage.PROGRAM_CHANGE) {
				    ShortMessage msg = (ShortMessage) message;
				    int chan = msg.getChannel();
				    int patch = msg.getData1();
				    patches.put(chan, patch);
				    System.out.println("PROGRAM_CHANGE " + chan + " / " + patch);
				} else if ((message.getStatus() & 0xF0) == ShortMessage.NOTE_ON) {
					ShortMessage msg = (ShortMessage) message;
					int chan = msg.getChannel();
					int n = msg.getData1();
					int d2 = msg.getData2();
										
				    int octave = n / 12 + 1;
				    int note = n % 12;

				    octave += transposeOctaves;
				    
				    int keyIdx = octave * 12 + note;
				    
				    System.out.print(timestamp + ": on " + chan + " note " + n + " / " + d2 + " / oct: " + octave + " / note: " + note);
				    
				    if (Arrays.stream(skipChannels).anyMatch(i -> i == chan) ^ onlyInclude) {
				    	System.out.println(" OUT");
						return;
				    }
					
				    if (d2 == 0) { // simulatenous note
				    	System.out.print(" D2");
				    	if (skipD2) {
				    		System.out.println(" SKIP");
				    		return;
				    	}
					}
					
					System.out.println();
					
					if (keysPos.length <= keyIdx || keyIdx < 0) {
						return;
					}
					
					synchronized(noteQueue) {
						noteQueue.add(keyIdx);
					}
					
					
				}
			}
			
			@Override
			public void close() {
				haltSequencer();
			}
		});
		
		sequencer.addMetaEventListener(meta -> {
            // END_OF_TRACK_MESSAGE
            if (meta.getType() == 47) {
            	this.haltSequencer();
            }
        });
	}
	
	@Override
	public void onWorldRender() {
		if (!Client.theClient.isRunning || true)
			return;

		int idx = 0;
		// for(Entry<Integer, float[]> entry : this.keysPos.entrySet()) {
		for (int keyIdx = 0; keyIdx < this.keysPos.length; keyIdx++) {
			float[] keyPos = this.keysPos[keyIdx];
			float distance = MathUtils.VecDist(keyPos, DrawUtils.lppos.getOrigin());
			float[] v = MathUtils.CalcAngle(DrawUtils.lppos.getViewOrigin(), keyPos);
			float pitch = 360f - (float) MathUtils.normalizeAngle(v[1] - 90f);
			
			float roll = v[0];
			float scale = Math.max(0.2f, distance / 1000f);
			float ex = keyPos[0];
			float ey = keyPos[2];
			float ez = -keyPos[1];
			
			
			DrawUtils.draw3DString(keyIdx + "", ex, ey, ez, pitch, roll, scale*0.4f);
			// DrawUtils.drawLine(DrawUtils.lppos.getOrigin(), keyPos);
			// DrawUtils.drawCube(mins, maxs);			
			
		}

	}
	
	@Override
	public void onUIRender() {
		if (!Client.theClient.isRunning || !this.runningSequencer)
			return;
		
		int w = 250;
		int h = 10;
		int border = 2;
		int x1 = DrawUtils.getScreenWidth() / 2 - w / 2;
		int x2 = DrawUtils.getScreenWidth() / 2 + w / 2;
		
		int y1 = DrawUtils.getScreenHeight() / 2 - h / 2 + 230;
		int y2 = DrawUtils.getScreenHeight() / 2 + h / 2 + 230;
		
		DrawUtils.setColor(0x000000A0);
		DrawUtils.fillRectangle(x1, y1, x2, y2);
		
		DrawUtils.setTextColor(0xFFFFFFFF);
		DrawUtils.drawString(x2 + 10, y1, this.transposeOctaves + "");
		
		x1 += border;
		y1 += border;
		y2 -= border;
		
		int wInner = (int) ((float) (w - border) * ((float) sequencer.getTickPosition() / (float) sequencer.getTickLength()));
		x2 = x1 + border + wInner;
		
		DrawUtils.setColor(0xA00000D0);
		DrawUtils.fillRectangle(x1, y1, x2, y2);
		
		// DrawUtils.fillRectangle(x1, y1, x2, y2);
		
	}
	
	@Override
	public void onLoop() {		
		if (!runningSequencer) {
			return;
		}

	}
	
	int keyi = 0;
	@Override
	public void onKeyPress(KeySym key) {
		
		if (key.intValue() == XKeySym.XK_g) {
			
			try {
				this.aimToKeyByIdx(keyi);
				Thread.sleep(10);
				this.aimToKeyByIdx(keyi);
				Thread.sleep(10);
				this.aimToKeyByIdx(keyi);
				Thread.sleep(10);
				this.aimToKeyByIdx(keyi);
				Thread.sleep(10);
				this.aimToKeyByIdx(keyi);
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.pressUse();
			keyi++;
			keyi = keyi % this.keysPos.length;
		}

		if (key.intValue() != XKeySym.XK_f) {
			return;
		}

		if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Shift_L)) {
			this.transposeOctaves++;
			System.out.println("Transposing: " + this.transposeOctaves);
			return;
		} else if (Client.theClient.keyboardHandler.isPressed(XKeySym.XK_Control_L)) {
			this.transposeOctaves--;
			System.out.println("Transposing: " + this.transposeOctaves);
			return;
		}
		
		if (runningSequencer) {
			this.haltSequencer();
		} else {
			this.haltSequencer();
			sequencer.start();
			runningSequencer = true;
		}
	}
	
	private void haltSequencer() {
		runningSequencer = false;
		sequencer.stop();
		sequencer.setTickPosition(0);
		this.noteQueue.clear();
	}
	
	private void aimToKeyByIdx(int idx) {
		float[] pos = this.keysPos[idx];
		float[] va = MathUtils.CalcAngle(DrawUtils.lppos.getViewOrigin(), pos);
		DrawUtils.lppos.setEngineAngles(va);
	}
	
	private void pressUse() {
		Engine.clientModule().writeInt(Offsets.input.use, 6);
	}
	
}
