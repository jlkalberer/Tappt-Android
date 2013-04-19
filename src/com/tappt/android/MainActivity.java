package com.tappt.android;

import com.tappt.android.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private EditText emailAddress;
	private EditText password;
	
	private TapptRestClient client;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.emailAddress = (EditText)this.findViewById(id.email);
        this.password = (EditText)this.findViewById(id.password);
        this.client = new TapptRestClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    /**
     * The click event fired when you want to login.
     * 
     * @param view
     */
    public void OnLoginClick(View view)
    {
    	String emailAddress = this.emailAddress.getText().toString();
    	if (emailAddress == null || emailAddress.isEmpty()) {
    		this.ShowError("Email must not be empty.");
    		return;
    	}
    	
    	String password = this.password.getText().toString();
    	if (password == null || password.isEmpty()) {
    		this.ShowError("Password must not be empty.");
    		return;
    	}
    	
    	this.client.Authenticate(emailAddress, password, true, null);
    }
    
    private void ShowError(String error) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(error)
    	       .setCancelable(false)
    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.dismiss();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}
