/*
 * {This class shows a short splashscreen image of app loading}
 *
 * @version Build {0.6} (26 March 2015)
 * @author Junaid Malik
 */
package com.example.testtonegenv10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
	
	private long ms = 0;
	private long splashTime = 2000; //duration of  2 seconds
	private boolean splashActive = true;
	private boolean paused = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread myThread = new Thread() {
			public void run() {
				try {
					while(splashActive && ms < splashTime) {
						if(!paused)
							ms = ms + 100;
						sleep(100);
					}
				} catch(Exception e) {}
				finally {
					Intent i = new Intent (SplashScreen.this, MainMenu.class);
					SplashScreen.this.startActivity(i);
					startActivity(i);
				}
			}
		};
		myThread.start();
	}
}
