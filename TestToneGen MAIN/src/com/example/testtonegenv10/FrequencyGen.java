package com.example.testtonegenv10;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.widget.Toast;

public class FrequencyGen extends Activity {

	public final int duration = 5; // seconds
	public int sampleRate = 44100;
	public final int numSamples = duration * sampleRate;
	public final double sample[] = new double[numSamples];
	public double freqOfTone; // hz
	public final byte generatedSound[] = new byte[2 * numSamples];
	public static final int LEFT_EAR = 1;
	public static final int RIGHT_EAR = 2;
	public AudioTrack audioTrack = null;

	public void genTone() {

		// fill out the array
		for (int counter = 0; counter < numSamples; ++counter) {
			sample[counter] = Math.sin(2 * Math.PI * counter
					/ (sampleRate / freqOfTone));
		}

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int index = 0;
		for (final double dVal : sample) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSound[index++] = (byte) (val & 0x00ff);
			generatedSound[index++] = (byte) ((val & 0xff00) >>> 8);
		}
	}

	public void playSound() {
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
				numSamples, AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSound, 0, generatedSound.length);
		audioTrack.getChannelConfiguration();

		try {

			audioTrack.play();
			// audioTrack.setLoopPoints(0, numSamples/4, -1);

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
