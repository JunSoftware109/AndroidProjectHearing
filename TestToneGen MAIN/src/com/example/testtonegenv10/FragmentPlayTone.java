package com.example.testtonegenv10;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentPlayTone extends Fragment   {

	public FragmentPlayTone() { }

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     // audiogram.setContentView(R.layout.audiogram_screen);
    // setContentView(R.layout.audiogram_fragment);
    // this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    	//---Inflate the layout for this fragment---    	
    	
        return inflater.inflate(
            R.layout.playtone_fragment, container, false); 
 
    }
}
    
   
  
	
	  	
 

		
    		  
   
