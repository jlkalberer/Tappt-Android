package com.tappt.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Helper {
	public static void ShowError(Context context, String title, String errorMessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder
			.setTitle(title)
		 	.setMessage(errorMessage)
	        .setCancelable(false)
	        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            }
	        });
		 AlertDialog alert = builder.create();
	     alert.show();
	}
}
