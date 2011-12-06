package com.frienemy.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

public class WallRequestListener implements RequestListener {

	private static final String TAG = WallRequestListener.class.getSimpleName();
	private Context context;
	
	public WallRequestListener(Context context) {
		this.context = context;
	}
	
	public void onComplete(String response, Object state) {
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			Log.d(TAG, "Wall Array length(): " + l);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub
		
	}

}
