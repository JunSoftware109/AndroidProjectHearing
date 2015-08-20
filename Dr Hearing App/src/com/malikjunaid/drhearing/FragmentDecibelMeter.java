package com.malikjunaid.drhearing;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A class used to create a simple textview to display decibel values, 
 * while calculating amplitude
 * Note: This class is still under construction needs to implemented*
 */
public class FragmentDecibelMeter extends Fragment {
	
	private TextView statusView;
	FrequencyGenerator frequencyGen;
    MediaRecorder recorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    
    public FragmentDecibelMeter() {} // default constructor

    final Runnable updater = new Runnable() {

        public void run() {    
            getAmplitude();
            getPeakAmplitude();
        	//updateTextView();
        };
    };
    
    final Handler mHandler = new Handler();

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        statusView = (TextView) findViewById(R.id.decibelStatus);

        if (runner == null)
        { 
            runner = new Thread() {
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(1000);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
		return inflater.inflate(R.layout.decibel_fragment, container, false);
    }

	private TextView findViewById(int decibelstatus) {
		// TODO Auto-generated method stub
		return null;
	}

    
    public void stopRecorder() {
        if (recorder != null) {
            recorder.stop();       
            recorder.release();
            recorder = null;
        }
    }

    public void updateTextView() {
        statusView.setText(Double.toString((getPeakAmplitude())) + " dB");
    }
    
    public double soundDb(double ampl){
        return  20 * Math.log10 (getPeakAmplitude() / 32767.0);
    }
    
    public double getAmplitude() {
        if (recorder != null)
            return  (recorder.getMaxAmplitude());
        else
            return 0;
    }
    
    public double getPeakAmplitude() {
        double amp =  getAmplitude();
       // mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}