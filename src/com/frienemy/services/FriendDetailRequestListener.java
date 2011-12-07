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

public class FriendDetailRequestListener implements RequestListener {

	private static final String TAG = FriendDetailRequestListener.class.getSimpleName();
	private Context context;

	public FriendDetailRequestListener(Context context) {
		this.context = context;
	}

	public void onComplete(String response, Object state) {
		//Log.d(TAG, response);
		try {
			final JSONObject o = new JSONObject(response);
			Friend friend = Friend.friendInContextForJSONObject(context, o);
			String relationshipStatus = o.getString("relationship_status");
			if (relationshipStatus.equals(friend.relationshipStatus) == false) {
				friend.relationshipStatusChanged = true;
			}
			friend.relationshipStatus = relationshipStatus;
			friend.save();
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
