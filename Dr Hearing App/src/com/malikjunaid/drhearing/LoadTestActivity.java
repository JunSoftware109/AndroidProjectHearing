/*
 * Copyright 2012 AndroidPlot.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.malikjunaid.drhearing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

/**
 * Class loads simple listview of audiogram xyplots
 * 
 */
public class LoadTestActivity extends Activity {
	private static final int NUM_PLOTS = 10;
	private static final int NUM_POINTS_PER_SERIES = 10;
	private static final int NUM_SERIES_PER_PLOT = 2;
	public XYPlot audiogram; // reference to XYPlot class
	private ListView lv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadprev_test);
		lv = (ListView) findViewById(R.id.listaudiogram);
		lv.setAdapter(new MyViewAdapter(getApplicationContext(),
				R.layout.loadprev_test, null));
		
		loadInfo();
	}
	
	/**
	 * Class enables us to load View objects in arrayList of views
	 */
	class MyViewAdapter extends ArrayAdapter<View> {
		public MyViewAdapter(Context context, int resId, List<View> views) {
			super(context, resId, views);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			LayoutInflater inf = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View v = convertView;
			if (v == null) {
				v = inf.inflate(R.layout.loadtest_item, parent, false);
			}
			

			audiogram = (XYPlot) v.findViewById(R.id.xyplot);
			Random generator = new Random();

			graphSettings();

			for (int i = 0; i < 2; i++) {
				ArrayList<Number> nums = new ArrayList<Number>();
				for (int j = 0; j < 100; j++) {
					nums.add(generator.nextFloat());
				}

				double rl = Math.random();
				double gl = Math.random();
				double bl = Math.random();

				double rp = Math.random();
				double gp = Math.random();
				double bp = Math.random();

				XYSeries series = new SimpleXYSeries(nums,
						SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Left Ear" + i);
				audiogram.addSeries(
						series,
						new LineAndPointFormatter(Color.rgb(
								new Double(rl * 255).intValue(), new Double(
										gl * 255).intValue(), new Double(
										bl * 255).intValue()), Color.rgb(
								new Double(rp * 255).intValue(), new Double(
										gp * 255).intValue(), new Double(
										bp * 255).intValue()), null, null));
			}

			return v;
		}
	}
	
	/**
	 * Method to load info at activity start up
	 */
	public void loadInfo() {
		
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setIcon(R.drawable.ic_launcher);
		alertbox.setMessage("This service is currently unavailable"
				+ "\nand will be implemented in a future update");
		alertbox.setTitle("Load test");
		alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// finish used for destroyed activity
			}
		});
		alertbox.show();	
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
		audiogram.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
		audiogram.getGraphWidget().getGridBackgroundPaint()
				.setColor(Color.WHITE);
		audiogram.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
		audiogram.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
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
		audiogram.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);

		audiogram.getRangeLabelWidget().getLabelPaint().setTextSize(13);
		audiogram.getRangeLabelWidget().setWidth(23);
		audiogram.getRangeLabelWidget().setHeight(500);
		audiogram.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
	}
}