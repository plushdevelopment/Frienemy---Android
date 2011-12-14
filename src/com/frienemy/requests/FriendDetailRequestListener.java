package com.frienemy.requests;

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
import com.frienemy.models.Friend;

public class FriendDetailRequestListener implements RequestListener {

	public interface FriendDetailRequestListenerResponder {
		public void friendDetailRequestDidFinish();
		public void friendDetailRequestDidFail();
	}

	private static final String TAG = FriendDetailRequestListener.class.getSimpleName();
	private Context context;
	private FriendDetailRequestListenerResponder responder;

	public FriendDetailRequestListener(Context context, FriendDetailRequestListenerResponder responder) {
		this.context = context;
		this.responder = responder;
	}

	public FriendDetailRequestListener(Context context) {
		this.context = context;
	}

	public void parseResponse(String response) {
		Log.d(TAG, response);
		try {
			final JSONArray a = new JSONArray(response);
			for (int i=0; i < a.length(); i++) {
				try {
					JSONObject responseItem = a.getJSONObject(i);
					try {
						String friendString = responseItem.getString("body");
						try {
							JSONObject o = new JSONObject(friendString);
							Friend friend = Friend.friendInContextForJSONObject(context, o);
							String relationshipStatus = o.getString("relationship_status");
							if (relationshipStatus.equals(friend.relationshipStatus) == false) {
								friend.relationshipStatusChanged = true;
							}
							friend.relationshipStatus = relationshipStatus;
							friend.save();
							if (responder != null) {
								responder.friendDetailRequestDidFinish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.w(TAG, "JSON Error in response" + e.getMessage());
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Log.w(TAG, "JSON Error in response" + e.getMessage());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.w(TAG, "JSON Error in response" + e.getMessage());
				}
			}
		} catch (JSONException e) {
			Log.w(TAG, "JSON Error in response");
			reportFailure();
		}
	}

	public void onComplete(String response, Object state) {
		try {
			final JSONObject o = new JSONObject(response);
			Friend friend = Friend.friendInContextForJSONObject(context, o);
			String relationshipStatus = o.getString("relationship_status");
			if (relationshipStatus.equals(friend.relationshipStatus) == false) {
				friend.relationshipStatusChanged = true;
			}
			friend.relationshipStatus = relationshipStatus;
			friend.save();
			if (responder != null) {
				responder.friendDetailRequestDidFinish();
			}
		} catch (JSONException e) {
			Log.w(TAG, "JSON Error in response");
			reportFailure();
		}
	}

	private void reportFailure() {
		if (responder != null) {
			responder.friendDetailRequestDidFail();
		}
	}

	public void onIOException(IOException e, Object state) {
		reportFailure();
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		reportFailure();
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		reportFailure();
	}

	public void onFacebookError(FacebookError e, Object state) {
		reportFailure();
	}

}
