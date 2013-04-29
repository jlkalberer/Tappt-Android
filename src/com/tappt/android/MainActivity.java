package com.tappt.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "Tappt";
	
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CheckLogin();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void CheckLogin(){ 
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		
		Intent login = new Intent(this, LoginActivity.class);
		if (preferences == null) {
			startActivity(login);
		}
		
		startActivity(login);
	}
}
