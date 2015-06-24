package com.malikjunaid.drhearing;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;


/**
 * A class used to create layout for results screen
 */
public class TestResults extends Activity {
	
	public XYPlot audiogram; // reference to XYPlot class
	public HearingTestActivity ht;
	
	@Override
	public void onCreate(Bundle savedInstanceState) { // activity is strarted
														// here
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_screen); // main menu view is loaded first
				
		
		audiogram = (XYPlot) findViewById(R.id.mySimpleXYPlotResult);
		
		graphSettings();// retrieve layout for audiogram
		ht.updatePlot();
		ht.getSeries();


	}
	
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
}
