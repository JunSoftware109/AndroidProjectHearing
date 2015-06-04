/*
 * {This class is used to run methods based on what user chooses
 * 	for the Frequency Generator }
 *
 * @version Build {1.0} (14 May 2015)
 * @author Junaid Malik
 */
package com.malikjunaid.testtonegenv10;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.Toast;

// this class is based on Marble Mice: Generate And Play A Tone In Android
// www.marblemice.blogspot.ie/2010/04/generate-and-play-tone-in-android.html
public class FrequencyGenerator extends Activity {

	public final int duration = 5; // length of tone in seconds
	public int sampleRate = 44100; // sample rate at 44,100 times per second
	public final int numSamples = duration * sampleRate;// in order to avoid
														// distortion
														// number of samples =
														// duration multiplied
														// by sample rate
	public final double sample[] = new double[numSamples];
	public double freqOfTone; // frequency in Hz
	public final byte generatedSound[] = new byte[2 * numSamples]; // array of
																	// genSoun =
																	// 2 times
																	// the
																	// number of
																	// samples
	public static final int LEFT_EAR = 1; // immutable int for left ear
	public static final int RIGHT_EAR = 2; // immutable int for light ear
	public AudioTrack audioTrack = null; // null object of type audioTrack

	public void genTone() {
		// fill out the array with samples
		for (int counter = 0; counter < numSamples; ++counter) {
			sample[counter] = Math.sin(2 * Math.PI * counter // x(t)=A Sin 2pi n
																// fa/fs
					/ (sampleRate / freqOfTone));
		}
		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int index = 0;
		// enhanced for loop going through sampa array
		for (final double doubleValue : sample) {
			// scale to maximum amplitude of 32,767
			final short value = (short) ((doubleValue * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			// 0xff00 is hex literal for the value 65,280 bitwise
			// it resets the rightmost 8 bits
			// operation, right shift to 8bits
			// 0x00ff only the rightmost 8 bits are kept
			// value is anded with 0x00FF
			// Anding with 8 zeros we are masking off 8 right MSB
			generatedSound[index++] = (byte) (value & 0x00ff);
			generatedSound[index++] = (byte) ((value & 0xff00) >>> 8);
		}
	}

	/*
	 * audiotrack plays pcm audio buffers data is pushed to audiotrack object in
	 * streaming mode the system writes continous data using write() method this
	 * method takes three params audioData, offfsetinBYtes and sizeinBytes
	 */
	public void playSound() {
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
				numSamples, AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSound, 0, generatedSound.length);
		audioTrack.getChannelConfiguration();

		try {
			audioTrack.play();
		} catch (Exception e) { // error message if not playable
			Toast.makeText(getApplicationContext(), "Error playing audio",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void playTone() {
		playTone(1.0f, 1.0f); // this method plays tone with full
								// volume in both ear
	}

	public void playTone(float left, float right) { // overloaded playTone
													// method with volume
													// determined by user
		Float leftVolume = left;
		Float rightVolume = right;
		try {
			audioTrack.write(generatedSound, 0, generatedSound.length);// pass
																		// genSound
																		// into
																		// write
																		// method
			audioTrack.setStereoVolume(leftVolume, rightVolume); // left and
																	// right
																	// are set
																	// by
																	// user
																	// choice
			audioTrack.play();
		} catch (Exception e) { // error message if not playable
			Toast.makeText(getApplicationContext(), "Error playing audio",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void playTone(int earSelect) {
		switch (earSelect) {
		case LEFT_EAR:
			playTone(1.0f, 0.0f);
			break;
		case RIGHT_EAR:
			playTone(0.0f, 0.05f);
			break;
		}
	}

	public void start() {
		audioTrack.play();
	}

	public void stop() {
		audioTrack.stop();
	}
}
