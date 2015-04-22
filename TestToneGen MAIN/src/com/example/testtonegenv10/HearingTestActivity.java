package com.example.testtonegenv10;

/* main class*/
import java.util.Arrays;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

public class HearingTestActivity extends
		android.support.v4.app.FragmentActivity implements OnClickListener {

	public HearingTestActivity() {
	}

	private Button canHearButton, cannotHearButton, nextButton, finishButton;
	private RadioButton rightEarButton, leftEarButton;
	static int TRUE = 1;// static variable only exists in one copy and belongs
						// to class
	static int newvalue = TRUE;// static variable only exists in one copy and
								// belongs to classic
	static final double currentdB = 40;
	private FrequencyGen frequencygen = new FrequencyGen(); // create FreqGen
															// reference
	private FragmentAudiogram audiogram = new FragmentAudiogram(); // create
																	// AudioGram
																	// reference
	// private AudioManager audioManager; // reference to AudioManager class
	private XYPlot mySimpleXYPlot; // reference to XYPlot class
	private SimpleXYSeries series1, series2;
	private Shader WHITE_SHADER = new LinearGradient(1, 1, 1, 1, Color.WHITE,
			Color.WHITE, Shader.TileMode.REPEAT);

	private int SERIES_LEN = 10; // length of array for x and y vals

	Integer[] xVals = new Integer[SERIES_LEN];
	Integer[] yVals = new Integer[SERIES_LEN];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hearingtest_view);

		startUp(); // opens up info box

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

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

		nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setOnClickListener(this);// if button clicked onClickListener
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

		// initialize our XYPlot reference:
		mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		mySimpleXYPlot.setTitle("Audiogram");
		mySimpleXYPlot.setRangeLabel("[dB] Hearing level");
		mySimpleXYPlot.setDomainLabel("Frequency Hz");

		mySimpleXYPlot.setDomainBoundaries(0, 8000, BoundaryMode.FIXED);
		mySimpleXYPlot.setRangeBoundaries(120, 0, BoundaryMode.FIXED);

		mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1000);
		// mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);

		mySimpleXYPlot.getBackgroundPaint().setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getBackgroundPaint()
				.setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint()
				.setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getDomainLabelPaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getRangeLabelPaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getDomainOriginLabelPaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
		mySimpleXYPlot.getTitleWidget().getLabelPaint().setTextSize(15);
		mySimpleXYPlot.getTitleWidget().setHeight(30);
		mySimpleXYPlot.getTitleWidget().setWidth(400);

		mySimpleXYPlot.getGraphWidget().setPaddingTop(8);
		mySimpleXYPlot.getGraphWidget().setPaddingBottom(12);
		mySimpleXYPlot.getGraphWidget().setPaddingLeft(-8);
		mySimpleXYPlot.getGraphWidget().setPaddingRight(18);

		// mySimpleXYPlot.getLegendWidget().setHeight(50);
		// mySimpleXYPlot.getLegendWidget().setBackgroundPaint(new
		// Paint(Color.BLUE));
		// mySimpleXYPlot.getLegendWidget().setVisible(false);

		// mySimpleXYPlot.getLegendWidget().getTextPaint().setTextSize(30);
		// setHeight(300, SizeLayoutType.ABSOLUTE);

		mySimpleXYPlot.getDomainLabelWidget().getLabelPaint().setTextSize(15);
		mySimpleXYPlot.getDomainLabelWidget().setWidth(100);
		mySimpleXYPlot.getDomainLabelWidget().setHeight(18);
		mySimpleXYPlot.getDomainLabelWidget().getLabelPaint()
				.setColor(Color.BLACK);

		mySimpleXYPlot.getRangeLabelWidget().getLabelPaint().setTextSize(15);
		mySimpleXYPlot.getRangeLabelWidget().setWidth(50);
		mySimpleXYPlot.getRangeLabelWidget().setHeight(500);
		mySimpleXYPlot.getRangeLabelWidget().getLabelPaint()
				.setColor(Color.BLACK);

		/*
		 * // Create a couple arrays of y-values to plot: Number[]
		 * series1Numbers = {40, 40, 40, 40, 40, 40}; Number[] series2Numbers =
		 * {40, 40, 40, 40, 40, 40};
		 * 
		 * // Turn the above arrays into XYSeries': XYSeries series1 = new
		 * SimpleXYSeries( Arrays.asList(series1Numbers), // SimpleXYSeries
		 * takes a List so turn our array into a List
		 * SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the
		 * element index as the x value "Right Ear"); // Set the display title
		 * of the series
		 * 
		 * // same as above XYSeries series2 = new SimpleXYSeries(
		 * Arrays.asList(series2Numbers),
		 * SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Left Ear");
		 * 
		 * // Create a formatter to use for drawing a series using
		 * LineAndPointRenderer // and configure it from xml:
		 * LineAndPointFormatter series1Format = new LineAndPointFormatter();
		 * series1Format.setPointLabelFormatter(new PointLabelFormatter());
		 * series1Format.configure(getApplicationContext(),
		 * R.xml.line_point_formatter_with_plf1);
		 * 
		 * // add a new series' to the xyplot: mySimpleXYPlot.addSeries(series1,
		 * series1Format);
		 * 
		 * // same as above: LineAndPointFormatter series2Format = new
		 * LineAndPointFormatter(); series2Format.setPointLabelFormatter(new
		 * PointLabelFormatter());
		 * series2Format.configure(getApplicationContext(),
		 * R.xml.line_point_formatter_with_plf2);
		 * 
		 * // add a new series' to the xyplot: mySimpleXYPlot.addSeries(series2,
		 * series2Format);
		 * 
		 * // reduce the number of range labels
		 * mySimpleXYPlot.setTicksPerRangeLabel(3);
		 * mySimpleXYPlot.getGraphWidget().setDomainLabelOrientation(0);
		 * 
		 * // series = (SimpleXYSeries) getSeries(); //
		 * mySimpleXYPlot.addSeries(series, series1Format); //
		 * mySimpleXYPlot.redraw();
		 */
	}

	public void startUp() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(
				HearingTestActivity.this);
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

	// Method for alert dialog, when user exits
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

	public void updatePlot() {
		// Remove all current series from each plot
		Iterator<XYSeries> iterator1 = mySimpleXYPlot.getSeriesSet().iterator();
		while (iterator1.hasNext()) {
			XYSeries setElement = iterator1.next();
			mySimpleXYPlot.removeSeries(setElement);

		}

		RectF rect = mySimpleXYPlot.getGraphWidget().getGridRect();
		BitmapShader myShader = new BitmapShader(Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(),
						R.drawable.audiogram_background), 1, (int) rect
						.height(), false), Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);
		Matrix m = new Matrix();
		m.setTranslate(rect.left, rect.top);
		myShader.setLocalMatrix(m);

		mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint()
				.setShader(myShader);

		if (leftEarButton.isChecked()) {
			// Create a formatter to use for drawing a series using
			// LineAndPointRenderer
			// and configure it from xml:
			LineAndPointFormatter series1Format = new LineAndPointFormatter();
			series1Format.setPointLabelFormatter(new PointLabelFormatter());
			series1Format.configure(getApplicationContext(),
					R.xml.line_point_formatter_with_plf1);

			series1 = (SimpleXYSeries) getSeries();
			mySimpleXYPlot.addSeries(series1, series1Format);
			mySimpleXYPlot.redraw();
		}
		/*
		 * else if (rightEarButton.isChecked()) { LineAndPointFormatter
		 * series2Format = new LineAndPointFormatter();
		 * series2Format.setPointLabelFormatter(new PointLabelFormatter());
		 * series2Format.configure(getApplicationContext(),
		 * R.xml.line_point_formatter_with_plf2);
		 * 
		 * mySimpleXYPlot.redraw();
		 * 
		 * // add a new series' to the xyplot: mySimpleXYPlot.addSeries(series2,
		 * series2Format); }
		 */

		// Setup our Series with the selected number of elements
		// series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
		// SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Us");
		// series2 = new SimpleXYSeries(Arrays.asList(series2Numbers),
		// SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Them");

	}

	// onClick method gets called each time a button is pressed
	@Override
	public void onClick(View v) {
		// audio manager provides access to volume control
		// Context is an interface to flobal info about app environment
		// context implementatio provided by OS.
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// Run the function findViewById and pass it R.id.button1 as parameters
		// Find out which button was pushed based on its ID
		// Switch statement checks for which button was checked and changes that
		// text
		// get selected radio button
		switch (v.getId()) {

		case R.id.nextButton: {
			xVals[0] = 250;
			/*
			 * xVals[0] = 250; xVals[1] = 500; xVals[2] = 1000; xVals[3] = 2000;
			 * xVals[4] = 3000; xVals[5] = 4000; xVals[6] = 5000; xVals[7] =
			 * 8000;
			 */
			if (xVals[0] == 250)
				updatePlot();
			frequencygen.freqOfTone = 250;
			frequencygen.genTone();
			frequencygen.playSound();

			/*
			 * if(xVals[1] == 500) updatePlot(); frequencygen.freqOfTone = 250;
			 * frequencygen.genTone(); frequencygen.playSound();
			 */
		}

		case R.id.leftEarButton: {
			try {
				if (leftEarButton.isChecked())
					frequencygen.playTone(FrequencyGen.LEFT_EAR);

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
					frequencygen.playTone(FrequencyGen.RIGHT_EAR);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.canHearButton: {
			try {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.cannotHearButton: {
			try {
				setDecibels();
				audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		} // end of switch case
	} // end of onClick methodrightEarButton

	// nested class of SimpleXYSeries
	public SimpleXYSeries getSeries() {
		// for (int counter = 1; counter < SERIES_LEN; counter += 1){
		// xVals[counter] = xVals[counter-1] + (int)(Math.random() * counter);
		// yVals[counter] = (int)(Math.random() * 140);
		// }
		if (leftEarButton.isChecked()) {
			return new SimpleXYSeries(Arrays.asList(xVals),
					Arrays.asList(yVals), "Left Ear");
		} else {
			return new SimpleXYSeries(Arrays.asList(xVals),
					Arrays.asList(yVals), "Right Ear");
			// same as above:
		}
	}

	public void setDecibels() {

		int i = 0;
		i++;
		yVals[0] = i;
		updatePlot();

	}
} // end of class