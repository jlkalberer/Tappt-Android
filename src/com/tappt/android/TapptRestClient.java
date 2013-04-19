package com.tappt.android;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.*;

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
	private final AsyncHttpClient client = new AsyncHttpClient();
	
	/**
	 * Function to authenticate the user using a user name and password.
	 * 
	 * @param userName The user name.
	 * @param password The password.
	 * @param rememberUserName Remember the user name the next time they want to log in.
	 * @param callback The callback to run
	 * @return True if the user is authenticated.
	 */
	public <TType> boolean Authenticate(String userName, String password, boolean rememberUserName, IFunction<TType> callback) {
		RequestParams params = new RequestParams();
		params.put("UserName", userName);
		params.put("Password", password);
		AsyncHttpResponseHandler responseHandler;
	
		client.get("http://www.google.com", new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        System.out.println(response);
		    }
		});
		
		this.client.post(getAbsoluteUrl("api/account"), params, new AsyncHttpResponseHandler() {
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
        });
		
		return false;
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
