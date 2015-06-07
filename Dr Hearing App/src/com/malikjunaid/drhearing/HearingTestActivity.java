/*
 * This class contains main operation of hearing test
 * rendering of audiogram
 *
 * @version Build (6 June 2015)
 * @author Junaid Malik
 */
package com.malikjunaid.drhearing;

import java.util.Arrays;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

/**
 * A class used to create instances of Hearing Test with Audiogram and Frequency
 * Generator
 */
public class HearingTestActivity extends
		android.support.v4.app.FragmentActivity implements OnClickListener {

	public HearingTestActivity() { // default constructor

	}

	private Button canHearButton, cannotHearButton, nextFrequencyButton,
			finishButton, playFrequencyButton;
	private RadioButton rightEarButton, leftEarButton;
	static int defaultdB = 40; // default dB level (yVal point)
	private FrequencyGenerator frequencygen = new FrequencyGenerator(); // create
																		// FreqGen

	// private AudioManager audioManager; // reference to AudioManager class
	public XYPlot audiogram; // reference to XYPlot class
	private SimpleXYSeries series1, series2;
	// private int[] frequencies;
	private int currentFreq = 0;
	private int indexYval = 0; // initial index position for yVals
	private int FREQ_LEN = 120; // length of array for x vals
	private int DB_LEN = 120; // length of array for y vals

	// private int[] frequencies = new frequencies;
	Integer[] frequencies = { 0, 250, 500, 1000, 2000, 4000, 6000, 8000 };
	Integer[] xVals = new Integer[FREQ_LEN]; // array of type Integer with
												// length defined
	Integer[] yVals = new Integer[DB_LEN];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hearingtest_view); // setting the view with
													// defined XML file
		startUp(); // opens up info box
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC); // reference to
																// current
																// volume stream
		// frequencies = getResources().getIntArray(R.array.frequencies);

		// frequencies = getResources().getIntArray(R.array.frequencies);

		leftEarButton = (RadioButton) findViewById(R.id.leftEarButton);
		leftEarButton.setOnClickListener(this);// if button clicked
												// onClickListener runs
												// corresponding onClick()
												// method

		rightEarButton = (RadioButton) findViewById(R.id.rightEarButton);
		rightEarButton.setOnClickListener(this);// if button clicked
												// onClickListener runs
												// corresponding onClick()
												// method

		playFrequencyButton = (Button) findViewById(R.id.playFrequencyButton);
		playFrequencyButton.setOnClickListener(this);// if button clicked
														// onClickListener
		// runs corresponding onClick()
		// method

		nextFrequencyButton = (Button) findViewById(R.id.nextFrequencyButton);
		nextFrequencyButton.setOnClickListener(this);// if button clicked
														// onClickListener
		// runs corresponding onClick()
		// method

		canHearButton = (Button) findViewById(R.id.canHearButton);
		canHearButton.setOnClickListener(this);// if button clicked
												// onClickListener runs
												// corresponding onClick()
												// method

		cannotHearButton = (Button) findViewById(R.id.cannotHearButton);
		cannotHearButton.setOnClickListener(this);// if button clicked
													// onClickListener runs
													// corresponding onClick()
													// method

		finishButton = (Button) findViewById(R.id.finishButton);
		finishButton.setOnClickListener(this);// if button clicked
												// onClickListener runs
												// corresponding onClick()
												// method

		// initialize our XYPlot reference:
		audiogram = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		graphSettings(); // retrieve layout for audiogram

	}

	// onClick method gets called each time a button is pressed
	@Override
	public void onClick(View v) {
		// audio manager provides access to volume control
		// Context is an interface to global info about app environment
		// context implementation provided by OS.
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// Run the function findViewById and pass it R.id.button1 as parameters
		// Find out which button was pushed based on its ID
		// Switch statement checks for which button was checked and changes that
		switch (v.getId()) {
		case R.id.nextFrequencyButton: {
			try {
				setFreqIndex();
				// setFreqValue();
				indexYval += 1;
				currentFreq += 1;
				yVals[indexYval] = 40; // set next index to default value 40
				updatePlot();
			} catch (ArrayIndexOutOfBoundsException exception) {
				if (currentFreq > 8) {

				}
				Toast.makeText(getApplicationContext(),
						"You have reached end of test", Toast.LENGTH_SHORT)
						.show();
			}
		}

		case R.id.playFrequencyButton: {
			try {
				frequencygen.freqOfTone = frequencies[currentFreq];
				frequencygen.genTone();
				frequencygen.playSound();
			} catch (ArrayIndexOutOfBoundsException exception) {
				Toast.makeText(getApplicationContext(),
						"You have reached end of test", Toast.LENGTH_SHORT)
						.show();
				restartTestAlert();
			}

		}

		case R.id.leftEarButton: {
			try {
				if (leftEarButton.isChecked())
					frequencygen.playTone(FrequencyGenerator.LEFT_EAR);

				else
					frequencygen.playTone(0.0f, 1.0f);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.rightEarButton: {
			try {
				if (rightEarButton.isChecked())
					frequencygen.playTone(FrequencyGenerator.RIGHT_EAR);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.canHearButton: {
			try {
				
				yVals[indexYval] -= 5;
				updatePlot();
				audioManager.adjustVolume(AudioManager.ADJUST_LOWER,
						AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
				if (yVals[indexYval] < 1) {
					yVals[indexYval] += 1;
					Toast.makeText(getApplicationContext(), "Lowest dB reached!",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.cannotHearButton: {
			try {
				yVals[indexYval] += 5;
				updatePlot();
				audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_VIBRATE);
				if (yVals[indexYval] > 120) {
					
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.finishButton: {
			finisTest();
		}
		} // end of switch case
	} // end of onClick methodrightEarButton

	private void setFreqIndex() {
		xVals[0] = 250;
		xVals[1] = 500;
		xVals[2] = 1000;
		xVals[3] = 2000;
		xVals[4] = 3000;
		xVals[5] = 4000;
		xVals[6] = 6000;
		xVals[7] = 8000;
	}

	// not currently used
	private void setFreqValue() {

		frequencygen.freqOfTone = frequencies[currentFreq];
		if (currentFreq > 6) {
			frequencies[0] = currentFreq;
		}
		currentFreq += 1;

	}

	/**
	 * Nested class that retusn series based on what ear is selected
	 */
	public SimpleXYSeries getSeries() {
		if (leftEarButton.isChecked()) {
			return new SimpleXYSeries(Arrays.asList(xVals),
					Arrays.asList(yVals), "Left Ear");
		} else {
			return new SimpleXYSeries(Arrays.asList(xVals),
					Arrays.asList(yVals), "Right Ear");
			// same as above:
		}
	}

	/**
	 * Method changes which points and what color is rendered
	 */
	private void updatePlot() {
		// Remove all current series from each plot
		Iterator<XYSeries> iterator1 = audiogram.getSeriesSet().iterator();
		while (iterator1.hasNext()) {
			XYSeries setElement = iterator1.next();
			audiogram.removeSeries(setElement);
		}
		drawBackground();

		if (leftEarButton.isChecked()) {
			// Create a formatter to use for drawing a series using
			// LineAndPointRenderer
			// and configure it from xml:
			LineAndPointFormatter series1Format = new LineAndPointFormatter();
			series1Format.setPointLabelFormatter(new PointLabelFormatter());
			series1Format.configure(getApplicationContext(),
					R.xml.line_point_formatter_with_plf1);
			audiogram.clear();

			series1 = (SimpleXYSeries) getSeries(); // call getSeries function
			audiogram.addSeries(series1, series1Format);
			audiogram.redraw(); // redraw series
		}

		else {
			LineAndPointFormatter series2Format = new LineAndPointFormatter();
			series2Format.setPointLabelFormatter(new PointLabelFormatter());
			series2Format.configure(getApplicationContext(),
					R.xml.line_point_formatter_with_plf2);
			audiogram.clear();
			series2 = (SimpleXYSeries) getSeries(); // call getSeries function
			// add a new series' to the xyplot:
			audiogram.addSeries(series2, series2Format);

			audiogram.redraw();
		}
	}

	/**
	 * Method clears plot points
	 */
	private void clearPlot() {
		// Remove all current series from each plot
		Iterator<XYSeries> iterator1 = audiogram.getSeriesSet().iterator();
		while (iterator1.hasNext()) {
			XYSeries setElement = iterator1.next();
			audiogram.removeSeries(setElement);
			audiogram.redraw();
			xVals[0] = 0;
			yVals[0] = 0;
			currentFreq = 0;
			FREQ_LEN = 0;
			audiogram.redraw();
		}
	}

	/**
	 * Method draws simple bitmap image from resources /Drawable
	 */
	private void drawBackground() {
		RectF rect = audiogram.getGraphWidget().getGridRect();
		BitmapShader myShader = new BitmapShader(Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(),
						R.drawable.audiogram_background), 1, (int) rect
						.height(), false), Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);
		Matrix m = new Matrix();
		m.setTranslate(rect.left, rect.top);
		myShader.setLocalMatrix(m);

		audiogram.getGraphWidget().getGridBackgroundPaint()
				.setShader(myShader);

	}

	/**
	 * Method to create the audiogram it is placed in xml layout here we add the
	 * extra details
	 */
	public void graphSettings() {

		audiogram.setTitle("Audiogram");
		audiogram.setRangeLabel("[dB] Hearing level");
		audiogram.setDomainLabel("Frequency [Hz]");

		audiogram.setDomainBoundaries(0, 8000, BoundaryMode.FIXED);
		audiogram.setRangeBoundaries(120, 0, BoundaryMode.FIXED);

		audiogram.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1000);
		// mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 10);

		audiogram.getBackgroundPaint().setColor(Color.WHITE);
		audiogram.getGraphWidget().getBackgroundPaint()
				.setColor(Color.WHITE);
		audiogram.getGraphWidget().getGridBackgroundPaint()
				.setColor(Color.WHITE);
		audiogram.getGraphWidget().getDomainLabelPaint()
				.setColor(Color.BLACK);
		audiogram.getGraphWidget().getRangeLabelPaint()
				.setColor(Color.BLACK);
		audiogram.getGraphWidget().getDomainOriginLabelPaint()
				.setColor(Color.BLACK);
		audiogram.getGraphWidget().getDomainOriginLinePaint()
				.setColor(Color.BLACK);
		audiogram.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
		audiogram.getTitleWidget().getLabelPaint().setTextSize(15);
		audiogram.getTitleWidget().setHeight(30);
		audiogram.getTitleWidget().setWidth(400);

		audiogram.getGraphWidget().setPaddingTop(8);
		audiogram.getGraphWidget().setPaddingBottom(12);
		audiogram.getGraphWidget().setPaddingLeft(-8);
		audiogram.getGraphWidget().setPaddingRight(18);

		audiogram.getDomainLabelWidget().getLabelPaint().setTextSize(13);
		audiogram.getDomainLabelWidget().setWidth(100);
		audiogram.getDomainLabelWidget().setHeight(18);
		audiogram.getDomainLabelWidget().getLabelPaint()
				.setColor(Color.BLACK);

		audiogram.getRangeLabelWidget().getLabelPaint().setTextSize(13);
		audiogram.getRangeLabelWidget().setWidth(23);
		audiogram.getRangeLabelWidget().setHeight(500);
		audiogram.getRangeLabelWidget().getLabelPaint()
				.setColor(Color.BLACK);
	}

	/**
	 * Method displays start up Alert Box
	 */
	public void startUp() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setIcon(R.drawable.ic_launcher);
		alertbox.setMessage("You will hear a series of tones for 5 seconds each."
				+ "\n\nSelect whether you can hear or cannot hear"
				+ "\n\nAt clearest dB you can hear a tone then choose select");
		alertbox.setTitle("Getting started");
		alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// finish used for destroyed activity
			}
		});
		alertbox.show();
	}

    /**
     * Method displays alert when user restarting test
     */
	public void restartTestAlert() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(
				HearingTestActivity.this);
		alertbox.setIcon(R.drawable.ic_launcher);
		alertbox.setMessage("Restart Test?");
		alertbox.setTitle("End of Test");
		alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// finish used for destroyed activity
				clearPlot();
			}
		});
		alertbox.show();
	}

	/**
	 * Method displays finish Alert Box
	 */
	public void finisTest() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(
				HearingTestActivity.this);
		alertbox.setIcon(R.drawable.ic_launcher);
		alertbox.setMessage("Finish Test?");
		alertbox.setTitle("End of Test");
		alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// finish used for destroyed activity
			}
		});
		alertbox.show();
		Intent intent = new Intent(this, TestResults.class); // intent used to
																// launch
																// activity
		startActivity(intent);
	}

	/**
	 * Metod for handling back press by user by prompting to avoid accidental
	 * exits
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			AlertDialog.Builder alertbox = new AlertDialog.Builder(
					HearingTestActivity.this);
			alertbox.setIcon(R.drawable.ic_launcher);
			alertbox.setTitle("Cancel the test?");
			alertbox.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							// finish used for destroyed activity
							finish();
						}
					});
			alertbox.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							Toast.makeText(getApplicationContext(),
									"Test continue", Toast.LENGTH_SHORT).show();
						}
					});
			alertbox.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Method onDestror when exiting app to stop playing tone
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("HearingTest", "onDestroy called");
		try {
			frequencygen.audioTrack.release();
			frequencygen.audioTrack = null;
			frequencygen = null;

		} catch (Exception e) {
			Log.e("HearingTest", "onDestroy error: " + e.toString());
		}
	}

	/**
	 * Method onPause when we pause activity we also stop any tones from playing
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("HearingTest", "onPause called");
		try {
			frequencygen.audioTrack.release();
			frequencygen.audioTrack = null;
			frequencygen = null;
		} catch (Exception e) {
			Log.e("HearingTest", "onPause error: " + e.toString());

		}
	}
} // end of class