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

public class FragmentDecibelMeter extends Fragment {
	
	private TextView mStatusView;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    final Runnable updater = new Runnable(){

        public void run(){          
        	updateTextView();
        };
    };
    
    final Handler mHandler = new Handler();

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);	
        mStatusView = (TextView) findViewById(R.id.decibelStatus);


        if (runner == null)
        { 
            runner = new Thread(){
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
		return inflater.inflate(R.layout.frequency_fragment, container, false);
    }

	private TextView findViewById(int decibelstatus) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null"); 
            try
            {           
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try
            {           
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();       
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTextView(){
        mStatusView.setText(Double.toString((getPeakAmplitude())) + " dB");
    }
    public double soundDb(double ampl){
        return  20 * Math.log10(getPeakAmplitude() / 32767.0);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
 
        else
            return 0;

    }
    public double getPeakAmplitude() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

}