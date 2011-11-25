package com.frienemy.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.frienemy.activities.FrienemyActivity;
import com.frienemy.models.Friend;

public class FriendsRequestListener implements RequestListener {

	private static final String TAG = FriendsRequestListener.class.getSimpleName();
	private Context context;

	public FriendsRequestListener(Context context) {
		this.context = context;
	}

	public void onComplete(String response, Object state) {
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			Log.d("Facebook-Example-Friends Request", "d.length(): " + l);

			for (int i=0; i<l; i++) {
				JSONObject o = d.getJSONObject(i);
				String n = o.getString("name");
				String id = o.getString("id");
				Friend f = new Friend(context);
				f.uid = id;
				f.name = n;
				f.save();
			}
		} catch (JSONException e) {
			Log.w("Facebook-Example", "JSON Error in response");
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
