package com.tappt.android;

import android.R.string;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * @author Tappt
 *
 * A class to connect to the Tappt Rest service.
 */
public class TapptRestClient {
	
	/**
	 * The base URL for the service.
	 */
	private static final String BASE_URL = "http://192.168.1.122/";
	
	/**
	 * The client used for making Rest requests.
	 */
	private static final AsyncHttpClient Client = new AsyncHttpClient();
	
	private static final SyncHttpClient SyncClient = new SyncHttpClient() {
		
		@Override
		public String onRequestFailed(Throwable arg0, String arg1) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	private static String UserName;
	
	private static String Password;
	
	/**
	 * Function to authenticate the user using a user name and password.
	 * 
	 * @param userName The user name.
	 * @param password The password.
	 * @param rememberUserName Remember the user name the next time they want to log in.
	 * @param callback The callback to run
	 * @return True if the user is authenticated.
	 */
	public static <TType> boolean Authenticate(String userName, String password, boolean rememberUserName, IFunction<TType> callback) {
		UserName = userName;
		Password = password;
		
		RequestParams params = new RequestParams();
		params.put("UserName", UserName);
		params.put("Password", Password);
		
		/*Client.post(getAbsoluteUrl("api/account"), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
            	@SuppressWarnings("unused")
				int bleh = 0;
            	bleh++;
                // Pull out the first event on the public timeline
                //JSONObject firstEvent = timeline.get(0);
                //String tweetText = firstEvent.getString("text");

                // Do something with the response
                //System.out.println(tweetText);
            }
        });*/
		
		String result = SyncClient.post(getAbsoluteUrl("api/account"), params);
	    
		return Boolean.parseBoolean(result);
	}
	
	public static String Tap() {
		SyncClient.setBasicAuth(UserName, Password);
		
		RequestParams params = new RequestParams();
		params.put("AuthorizeType", "Pour");
		
		return SyncClient.post(getAbsoluteUrl("api/authorize"), params);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
