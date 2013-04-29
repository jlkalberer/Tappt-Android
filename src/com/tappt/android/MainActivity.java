package com.tappt.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
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