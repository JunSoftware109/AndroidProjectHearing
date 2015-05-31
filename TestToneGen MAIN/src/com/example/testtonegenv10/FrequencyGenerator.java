/*
 * {This class is used to run methods based on what user chooses
 * 	for the Frequency Generator }
 *
 * @version Build {1.0} (14 May 2015)
 * @author Junaid Malik
 */
package com.example.testtonegenv10;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.widget.Toast;

// this class is based on Marble Mice: Generate And Play A Tone In Android
// www.marblemice.blogspot.ie/2010/04/generate-and-play-tone-in-android.html
public class FrequencyGenerator extends Activity {

	public final int duration = 5; // length of tone in seconds
	public int sampleRate = 44100; // sample rate at 44,100 times per second
	public final int numSamples = duration * sampleRate;
	public final double sample[] = new double[numSamples]; // number of samples
															// = duration multiplied by
															// sample rate
	public double freqOfTone; // frequency in Hz
	public final byte generatedSound[] = new byte[2 * numSamples]; 
	public static final int LEFT_EAR = 1;
	public static final int RIGHT_EAR = 2;
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
		for (final double doubleValue : sample) { 
			// scale to maximum amplitude
			final short value = (short) ((doubleValue * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSound[index++] = (byte) (value & 0x00ff);
			generatedSound[index++] = (byte) ((value & 0xff00) >>> 8);
		}
	}

	/*
	 * audiotrack plays pcm audio buffers data is pushed to audiotrack object in
	 * streaming mode the app writes continous data using write() method write
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
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error playing audio",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void playTone() {
		playTone(1.0f, 1.0f);
	}

	public void playTone(float left, float right) {
		Float leftVolume = left;
		Float rightVolume = right;

		audioTrack.write(generatedSound, 0, generatedSound.length);
		audioTrack.setStereoVolume(leftVolume, rightVolume);
		audioTrack.play();
	}

	public void playTone(float left, float right, int earSelect) {
		switch (earSelect) {
		case LEFT_EAR:
			playTone(left, 0.0f);
			break;
		case RIGHT_EAR:
			playTone(0.0f, right);
			break;
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
