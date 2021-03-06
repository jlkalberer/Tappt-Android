package com.tappt.android.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.tappt.android.IFunction;
import com.tappt.android.models.KegeratorList;
import com.tappt.android.models.RequestAuthentication;

/**
 * @author Tappt
 *
 * A class to connect to the Tappt Rest service.
 */
public class TapptRestClient {
	
	/**
	 * The base URL for the service.
	 */
	private static final String BASE_URL = "http://192.168.1.144/";
	
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
	
	public static String UserName = "test";
	
	public static String Password = "password";
	
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
		RequestParams params = new RequestParams();
		params.put("UserName", userName);
		params.put("Password", password);
		
		String result = SyncClient.post(getAbsoluteUrl("api/account"), params);
	    
		return Boolean.parseBoolean(result);
	}
	
	public static String Tap() {
		SyncClient.setBasicAuth(UserName, Password);
		
		RequestParams params = new RequestParams();
		params.put("AuthorizeType", "Pour");
		
		return SyncClient.post(getAbsoluteUrl("api/authorize"), params);
	}
	
	public static void GetKegerators(RestResponse<KegeratorList> callback) {
		Client.setBasicAuth(UserName, Password);
		Client.get(getAbsoluteUrl("api/kegerator"), callback);
	}
	
	public static String AuthorizeTag(RequestAuthentication authentication) {		
		SyncClient.setBasicAuth(UserName, Password);
		
		RequestParams params = new RequestParams();
		params.put("UserName", authentication.UserName);
		params.put("Password", authentication.Password);
		params.put("AuthorizeType", authentication.AuthorizeType.toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
		
		params.put("ExpiresDate", sdf.format(authentication.ExpiresDate));
		
		ArrayList<String> kegerators = new ArrayList<String>();
		for (Integer value : authentication.Kegerators) { 
			kegerators.add(String.valueOf(value)); 
		}
		params.put("Kegerators", kegerators);
		
		return SyncClient.post(getAbsoluteUrl("api/authorize"), params);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
