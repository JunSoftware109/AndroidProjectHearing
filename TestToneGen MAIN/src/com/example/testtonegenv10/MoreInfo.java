/**
 * {This class runs the info activity and launches new intent based on what user chooses}
 *
 * @version Build {0.6} (26 March 2015)
 * @author Junaid Malik
 */
package com.example.testtonegenv10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MoreInfo extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moreinfo_activity);
	}

	public void loadView1(View v) {
		Intent intent = new Intent(MoreInfo.this, InfoView1.class);
		startActivity(intent);
	}

	public void loadView2(View v) {
		Intent intent = new Intent(MoreInfo.this, InfoView2.class);
		startActivity(intent);
	}

	public void loadView3(View v) {
		Intent intent = new Intent(MoreInfo.this, InfoView3.class);
		startActivity(intent);
	}

	public void loadView4(View v) {
		Intent intent = new Intent(MoreInfo.this, InfoView4.class);
		startActivity(intent);
	}

}
