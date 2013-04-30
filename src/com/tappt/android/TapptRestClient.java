package com.tappt.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.BasicScheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.tappt.android.models.Kegerator;

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
	
	private static String UserName = "test";
	
	private static String Password = "password";
	
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
		
		String result = SyncClient.post(getAbsoluteUrl("api/account"), params);
	    
		return Boolean.parseBoolean(result);
	}
	
	public static String Tap() {
		SyncClient.setBasicAuth(UserName, Password);
		
		RequestParams params = new RequestParams();
		params.put("AuthorizeType", "Pour");
		
		return SyncClient.post(getAbsoluteUrl("api/authorize"), params);
	}
	
	public static void GetKegerators(IFunction<List<Kegerator>> callback) {
		ArrayList<Kegerator> output = new ArrayList<Kegerator>();
		
		///Client.setBasicAuth(UserName, Password);
		Header header = BasicAuth();
		Client.addHeader(header.getName(), header.getValue());
		
		Client.get(getAbsoluteUrl("api/kegerator"), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				Gson gson = new Gson();
				List<Kegerator> kegerators = gson.fromJson(arg0, new TypeToken<List<Kegerator>>(){}.getType());
				// callback(kegerators);
			}
			@Override
			public void onFailure(Throwable e, String response) {
		         // Response failed :(
		     }
		});
		
		//String jsonString = SyncClient.post(getAbsoluteUrl("api/kegerator"), params);
		
		return output;
	}
	
	private static Header BasicAuth() {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(UserName, Password);
	    return BasicScheme.authenticate(credentials, "UTF-8", false);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
