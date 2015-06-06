package com.malikjunaid.testtonegenv10;

import com.example.testtonegenv10.R;

import android.app.Activity;
import android.os.Bundle;


/**
 * A class used to create layout for results screen
 */
public class TestResults extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) { // activity is strarted
														// here
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_screen); // main menu view is loaded first
	}

}
