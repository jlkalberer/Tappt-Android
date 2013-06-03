package com.tappt.android;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tappt.android.models.AuthorizeType;
import com.tappt.android.models.Kegerator;
import com.tappt.android.models.KegeratorList;
import com.tappt.android.models.RequestAuthentication;
import com.tappt.android.util.Helper;
import com.tappt.android.util.RestResponse;
import com.tappt.android.util.TapptRestClient;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class AuthenticateUserForTag extends Activity {
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private WriteNFCTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private ListView mKegeratorView;
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private TextView dateLabel;
	private Button mDateButton;
	private String initialDate;
	private String initialMonth;
	private String initialYear;
	private DatePickerDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_authenticate_user_for_tag);
		setupActionBar();

		// Set up the login form.
		mKegeratorView = (ListView) findViewById(R.id.my_list);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);
		mDateButton = (Button)findViewById(R.id.dateButton);
		dateLabel = (TextView)findViewById(R.id.dateLabel);
		
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		TapptRestClient.GetKegerators(
				new RestResponse<KegeratorList>(KegeratorList.class) {
							@Override
							public void onSuccess(KegeratorList kegerators) {
								AuthenticateUserForTag self = AuthenticateUserForTag.this;
								final ListView listview = (ListView) self.findViewById(R.id.my_list);
				
							    final ArrayAdapter<Kegerator> adapter = new ArrayAdapter<Kegerator>(self,
							        android.R.layout.simple_list_item_multiple_choice, kegerators);
							    listview.setAdapter(adapter);
							}
						});
	}

	public void onClick_dateButton(View view) {
		Calendar dtTxt = null;

        String preExistingDate = dateLabel.getText().toString();
        
        if(preExistingDate != null && !preExistingDate.equals("")){
            StringTokenizer st = new StringTokenizer(preExistingDate,"/");
                initialMonth = st.nextToken();
                initialDate = st.nextToken();
                initialYear = st.nextToken();
                if(dialog == null)
                dialog = new DatePickerDialog(view.getContext(),
                                 new PickDate(),Integer.parseInt(initialYear),
                                 Integer.parseInt(initialMonth)-1,
                                 Integer.parseInt(initialDate));
                dialog.updateDate(Integer.parseInt(initialYear),
                                 Integer.parseInt(initialMonth)-1,
                                 Integer.parseInt(initialDate));
                
        } else {
            dtTxt = Calendar.getInstance();
            if(dialog == null)
            dialog = new DatePickerDialog(view.getContext(),new PickDate(),dtTxt.getTime().getYear(),dtTxt.getTime().getMonth(),
                                                dtTxt.getTime().getDay());
            dialog.updateDate(dtTxt.getTime().getYear(),dtTxt.getTime().getMonth(),
                                                dtTxt.getTime().getDay());
        }
          
          dialog.show();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.write_chip, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (mKegeratorView.getCheckedItemCount() == 0) {
			 Helper.ShowError(this, "Sorry", "You need to select at least one Kegerator to give the user access to.");
		     cancel =  true;
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} /* else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}*/

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			if (focusView != null) {
				focusView.requestFocus();
			}
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new WriteNFCTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class WriteNFCTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			RequestAuthentication requestAuthentication = new RequestAuthentication();
			requestAuthentication.UserName = mEmail;
			requestAuthentication.Password = mPassword;
			requestAuthentication.AuthorizeType = AuthorizeType.Tag;
			
			Calendar cal = Calendar.getInstance(); // creates calendar
		    cal.setTime(new Date()); // sets calendar time/date
		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
		    
		    requestAuthentication.Kegerators.add(1);
		    requestAuthentication.Kegerators.add(2);
		    
			requestAuthentication.ExpiresDate = cal.getTime(); 
						
			return TapptRestClient.AuthorizeTag(requestAuthentication);
		}

		@Override
		protected void onPostExecute(final String result) {
			mAuthTask = null;
			showProgress(false);

			if (result != null && !result.isEmpty() && result != "") {
				Intent writeTag = new Intent(AuthenticateUserForTag.this, WriteTag.class);
				writeTag.putExtra("AuthenticationID", result);
				startActivity(writeTag);
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	private class PickDate implements DatePickerDialog.OnDateSetListener {

	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {
	        view.updateDate(year, monthOfYear, dayOfMonth);
	        dateLabel.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
	        dialog.hide();
	    }
	}
}