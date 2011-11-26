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

	public FriendsRequestListener(Context context) {
		this.context = context;
	}

	public void onComplete(String response, Object state) {
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			Log.d(TAG, "Friend Array length(): " + l);
			ArrayList<Friend> fetchedFriends = Friend.query(context, Friend.class, null);
			for (int f=0; f<fetchedFriends.size(); f++) {
				boolean exists = false;
				Friend friend = fetchedFriends.get(f);
				for (int i=0; i<l; i++) {
					JSONObject o = d.getJSONObject(i);
					if (friend.uid.matches(o.getString("id"))) {
						exists = true;
						friend.frienemyStatus = 0;
						break;
					}
				}
				if (!exists) {
					friend.frienemyStatus = 1;
					friend.save();
				}
			}
			
			for (int i=0; i<l; i++) {
				JSONObject o = d.getJSONObject(i);
				Friend friend = Friend.friendInContextForJSONObject(context, o);
				friend.save();
			}
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
