package com.tappt.android.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class RestResponse<TType> extends AsyncHttpResponseHandler {
	
	private Class<TType> clazz; 
	
	public RestResponse(Class<TType> clazz){ 
		this.clazz = clazz;
	}
	
	@Override
	public void onSuccess(String json) {
		TType parsedObject = this.deserialize(json, clazz);
		
		RestResponse.this.onSuccess(parsedObject);
	}
	@Override
	public void onFailure(Throwable e, String response) {
         // Response failed :( -- We should log this :)
    }
	
	public void onSuccess(TType value){ 
		
	}
	
	private <T> T deserialize(String jsonString, Class<T> clazz) {
	    GsonBuilder builder = new GsonBuilder();
	    builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	    Gson gson = builder.create();
	    return gson.fromJson(jsonString, clazz);
	}
}