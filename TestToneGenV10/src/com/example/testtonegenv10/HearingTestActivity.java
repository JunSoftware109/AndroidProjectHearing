package com.example.testtonegenv10;
/* main class*/
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
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


public class HearingTestActivity extends android.support.v4.app.FragmentActivity implements OnClickListener {
	
	public HearingTestActivity() { }
	
	private Button canHearButton, cannotHearButton, nextButton, finishButton;
	private RadioGroup radioGroup;
	private RadioButton rightEarButton, leftEarButton;
	static int TRUE = 1;//static variable only exists in one copy and belongs to class
    static int newvalue = TRUE;//static variable only exists in one copy and belongs to class
    private boolean isLeftEar = true;
    private FrequencyGen frequencygen = new FrequencyGen();  // create FreqGen reference
	private FragmentAudiogram audiogram = new FragmentAudiogram(); // create AudioGram reference
	private AudioTrack audioTrack = null; // reference to AudioTrack class
	private AudioManager audioManager;	// reference to AudioManager class
	private XYPlot mySimpleXYPlot;	// reference to XYPlot class
	private MediaPlayer mediaPlayer;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hearingtest_view);
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		leftEarButton = (RadioButton)findViewById(R.id.leftEarButton);
		leftEarButton.setOnClickListener(this);//if button clicked onClickListener runs corresponding onClick() method
     	
		rightEarButton = (RadioButton)findViewById(R.id.rightEarButton);
		rightEarButton.setOnClickListener(this);//if button clicked onClickListener runs corresponding onClick() method
	     
		nextButton = (Button)findViewById(R.id.nextButton);
		nextButton.setOnClickListener(this);//if button clicked onClickListener runs corresponding onClick() method
	     
		canHearButton = (Button)findViewById(R.id.canHearButton);
		canHearButton.setOnClickListener(this);//if button clicked onClickListener runs corresponding onClick() method
	     
		cannotHearButton = (Button)findViewById(R.id.cannotHearButton);
		cannotHearButton.setOnClickListener(this);//if button clicked onClickListener runs corresponding onClick() method	     
	     

		// initialize our XYPlot reference:
		mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		mySimpleXYPlot.setTitle("Audiogram");
		mySimpleXYPlot.setRangeLabel("[dB] Hearing level");
		mySimpleXYPlot.setDomainLabel("Frequency Hz");
        	
		// mySimpleXYPlot.setDomainBoundaries(0, 8000, BoundaryMode.FIXED);
		mySimpleXYPlot.setRangeBoundaries(0, 120, BoundaryMode.FIXED);
		//  mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1000);
   	 	//mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
	      
		mySimpleXYPlot.getBackgroundPaint().setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
		mySimpleXYPlot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
		mySimpleXYPlot.getTitleWidget().getLabelPaint().setTextSize(15);
		mySimpleXYPlot.getTitleWidget().setHeight(30);
		mySimpleXYPlot.getTitleWidget().setWidth(400);
	       
		mySimpleXYPlot.getGraphWidget().setPaddingTop(8);
		mySimpleXYPlot.getGraphWidget().setPaddingBottom(12);
		mySimpleXYPlot.getGraphWidget().setPaddingLeft(-8);
     	mySimpleXYPlot.getGraphWidget().setPaddingRight(18);
	   
     	//  mySimpleXYPlot.getLegendWidget().setHeight(50);
     	//  mySimpleXYPlot.getLegendWidget().setBackgroundPaint(new Paint(Color.BLUE));
     	// mySimpleXYPlot.getLegendWidget().setVisible(false); 	

	    //mySimpleXYPlot.getLegendWidget().getTextPaint().setTextSize(30);
     	//setHeight(300, SizeLayoutType.ABSOLUTE);

     	mySimpleXYPlot.getDomainLabelWidget().getLabelPaint().setTextSize(15);        
     	mySimpleXYPlot.getDomainLabelWidget().setWidth(100);
     	mySimpleXYPlot.getDomainLabelWidget().setHeight(18);
     	mySimpleXYPlot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);

     	mySimpleXYPlot.getRangeLabelWidget().getLabelPaint().setTextSize(15);
     	mySimpleXYPlot.getRangeLabelWidget().setWidth(50);
     	mySimpleXYPlot.getRangeLabelWidget().setHeight(500);
     	mySimpleXYPlot.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);

     	// Create a couple arrays of y-values to plot:
     	Number[] series1Numbers = {40, 40, 40, 40, 40, 40};
     	Number[] series2Numbers = {40, 40, 40, 40, 40, 40};
     	

     	// Turn the above arrays into XYSeries':
     	XYSeries series1 = new SimpleXYSeries(
	           Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
	           SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
	           "Right Ear");                             // Set the display title of the series

     	// same as above
     	XYSeries series2 = new SimpleXYSeries(
     			Arrays.asList(series2Numbers),
     			SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, 
     			"Left Ear");

     	// Create a formatter to use for drawing a series using LineAndPointRenderer
     	// and configure it from xml:
     	LineAndPointFormatter series1Format = new LineAndPointFormatter();
      		series1Format.setPointLabelFormatter(new PointLabelFormatter());
      		series1Format.configure(getApplicationContext(),
      				R.xml.line_point_formatter_with_plf1);

        // add a new series' to the xyplot:
        mySimpleXYPlot.addSeries(series1, series1Format);

	    // same as above:
	    LineAndPointFormatter series2Format = new LineAndPointFormatter();
	  		series2Format.setPointLabelFormatter(new PointLabelFormatter());
	  		series2Format.configure(getApplicationContext(),
	  				R.xml.line_point_formatter_with_plf2);
	      
	  	// add a new series' to the xyplot:
	  	mySimpleXYPlot.addSeries(series2, series2Format);

	  	// reduce the number of range labels
	  	mySimpleXYPlot.setTicksPerRangeLabel(3);
	  	mySimpleXYPlot.getGraphWidget().setDomainLabelOrientation(0);
	  	
	}
	
	//Method for alert dialog, when user exits
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        AlertDialog.Builder alertbox = new AlertDialog.Builder(HearingTestActivity.this);
	        alertbox.setIcon(R.drawable.ic_launcher);
	        alertbox.setTitle("Cancel the test?");
	        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) { 
	               // finish used for destroyed activity
	                finish();
	            }
	        });
	        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	    			Toast.makeText(getApplicationContext(), 
                            "Test continue", Toast.LENGTH_SHORT).show(); 	
	          }
	        });
	        alertbox.show();
	    }
	    return super.onKeyDown(keyCode, event);
	}

	//onClick method gets called each time a button is pressed
	@Override
  	public void onClick(View v) {

  		//Run the function findViewById and pass it R.id.button1 as parameters	   
 		//Find out which button was pushed based on its ID
  		//Switch statement checks for which button was checked and changes that text 		
  		// get selected radio button from radioGroupCricket
  		switch(v.getId()) {
  		
  		  case R.id.nextButton: {
  			  	frequencygen.freqOfTone = 500; // generating freq at 2000 Hz
  			  
				frequencygen.genTone();
				frequencygen.playSound();
				
  		  }
  		  
  		  case R.id.leftEarButton: {
  				try {
  					if(leftEarButton.isChecked())
					frequencygen.playTone(FrequencyGen.LEFT_EAR);
					
  				} catch (Exception e) {
  					Toast.makeText(getApplicationContext(), 
                            "Error", Toast.LENGTH_SHORT).show(); 	  	  		
  				
  				}
				break;
  			}
  			
  		  case R.id.rightEarButton: {
  				
  				try {
  					//frequencygen.playTone(0.0f, 1.0f);
  					if(rightEarButton.isChecked())
					frequencygen.playTone(FrequencyGen.RIGHT_EAR);
  					
  				} catch (Exception e) {
  					Toast.makeText(getApplicationContext(), 
                            "Error", Toast.LENGTH_SHORT).show(); 	  	  		
  				}  				
				break;
  			}													
  			
  		  case R.id.canHearButton: {
  					
  				try {						 
  					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
  							AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
  				} catch (Exception e) {
  					Toast.makeText(getApplicationContext(), 
                            "Error", Toast.LENGTH_SHORT).show();  	  					
  				}  				
  					break;
  	  		}
  			
  		  case R.id.cannotHearButton: {
  					try {
  						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
  								AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI); 	
  					} catch (Exception e) {
  						Toast.makeText(getApplicationContext(), 
  	                             "Error", Toast.LENGTH_SHORT).show(); 	
  					} 			
  					break;
  					}
  			} // end of switch case	
  		} // end of onClick methodrightEarButton
 } // end of class