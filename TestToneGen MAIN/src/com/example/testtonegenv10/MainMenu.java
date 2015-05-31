/*
 * {This class is used to run methods based on what user chooses
 * 	for the main menu }
 *
 * @version Build {0.6} (26 March 2015)
 * @author Junaid Malik
 */
package com.example.testtonegenv10;

import com.kskkbys.rate.RateThisApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) { // activity is strarted
														// here
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
	}

	public void beginTest(View v) {
		Intent intent = new Intent(this, StartTest.class);
		startActivity(intent);
	}

	public void moreInfo(View v) {
		Intent intent = new Intent(MainMenu.this, MoreInfo.class);
		startActivity(intent);
	}

	public void loadTest(View v) {
		Intent intent = new Intent(MainMenu.this, LoadTestActivity.class);
		startActivity(intent);
	}
	
	public void moreInfo2(View v) {
		Intent intent = new Intent(MainMenu.this, MoreInfo2.class);
		startActivity(intent);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    // Monitor launch times and interval from installation
	    RateThisApp.onStart(this);
	    // If the criteria is satisfied, "Rate this app" dialog will be shown
	    RateThisApp.showRateDialogIfNeeded(this);
	}
}
