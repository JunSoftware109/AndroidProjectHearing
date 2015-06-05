/*
 * {Simple class loads infoView1.java}
 *
 * @version Build {0.6} (26 March 2015)
 * @author Junaid Malik
 */
package com.malikjunaid.testtonegenv10;

import com.example.testtonegenv10.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class InfoView1 extends Activity implements OnClickListener {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infoview1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
