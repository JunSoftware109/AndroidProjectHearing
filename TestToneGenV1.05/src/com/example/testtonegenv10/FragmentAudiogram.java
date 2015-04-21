package com.example.testtonegenv10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAudiogram extends Fragment {
		
		public FragmentAudiogram () {}
	  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	    
	     
	    	  return inflater.inflate(
	    	            R.layout.audiogram_fragment, container, false); 

	    }	    
}