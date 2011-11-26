package com.frienemy.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.frienemy.models.Friend;

public class FriendsRequestListener implements RequestListener {

	private static final String TAG = FriendsRequestListener.class.getSimpleName();
	private Context context;
	private static ArrayList<Friend> friends;

	public FriendsRequestListener(Context context) {
		this.context = context;
	}
	
	public static synchronized ArrayList<Friend> getFriends() {
		return friends;
	}

	public static synchronized void setFriends(ArrayList<Friend> friends) {
		FriendsRequestListener.friends = friends;
	}

	public void onComplete(String response, Object state) {
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			Log.d(TAG, "Friend Array length(): " + l);
			ArrayList<Friend> array = new ArrayList<Friend>();
			for (int i=0; i<l; i++) {
				JSONObject o = d.getJSONObject(i);
				Friend friend = Friend.friendInContextForJSONObject(context, o);
				array.add(friend);
			}
			setFriends(array);
		} catch (JSONException e) {
			Log.w(TAG, "JSON Error in response");
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
