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

public class UserRequestListener implements RequestListener {
	
	public interface UserRequestListenerResponder {
		public void userRequestDidFinish();
		public void userRequestDidFail();
	}

	private static final String TAG = UserRequestListener.class.getSimpleName();
	private Context context;
	private UserRequestListenerResponder responder;

	public UserRequestListener(Context context, UserRequestListenerResponder responder) {
		this.context = context;
		this.responder = responder;
	}

	public void onComplete(String response, Object state) {
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			if (d.length() > 0) {
				JSONObject o = d.getJSONObject(0);
				Friend friend = Friend.friendInContextForJSONObject(context, o);
				friend.isCurrentUser = true;
				friend.save();
			}
			responder.userRequestDidFinish();
		} catch (JSONException e) {
			Log.w(TAG, "JSON Error in response");
			responder.userRequestDidFail();
		}
	}

	public void onIOException(IOException e, Object state) {
		responder.userRequestDidFail();
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		responder.userRequestDidFail();
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		responder.userRequestDidFail();
	}

	public void onFacebookError(FacebookError e, Object state) {
		responder.userRequestDidFail();
	}

}
