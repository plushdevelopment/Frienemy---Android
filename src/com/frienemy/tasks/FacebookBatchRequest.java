package com.frienemy.tasks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.frienemy.models.Friend;
import com.frienemy.requests.FriendDetailRequestListener;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FacebookBatchRequest extends AsyncTask <ArrayList<Friend>, Void, Void> {
	
	public interface FacebookBatchRequestResponder {
	    public void friendDetailsLoading();
	    public void friendDetailsLoadCancelled();
	    public void friendDetailsLoaded();
	  }

	private static final String TAG = FacebookBatchRequest.class.getSimpleName();
	private Context context;
	private Facebook facebook;
	private FacebookBatchRequestResponder responder;
	
	public FacebookBatchRequest(Context context, Facebook facebook, FacebookBatchRequestResponder responder) {
		this.context = context;
		this.facebook = facebook;
		this.responder = responder;
	}
	
	@Override
	protected Void doInBackground(ArrayList<Friend>... params) {
		
		ArrayList<JSONArray>batchFriends = batchFriendDetailRequests(params[0]);
		for (int i=0; i < batchFriends.size(); i++) {
			/* URL */
			String url = "https://graph.facebook.com/";

			/* Arguments */
			Bundle args = new Bundle();
			args.putString("access_token", facebook.getAccessToken());
			args.putString("batch", batchFriends.get(i).toString());
			String ret = "";
			try {
				ret = Util.openUrl(url, "POST", args);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FriendDetailRequestListener friendDetailRequestListener = new FriendDetailRequestListener(context);
			friendDetailRequestListener.parseResponse(ret);
		}
		return null;
	}
	
	@Override
	  protected void onPreExecute() {
	    super.onPreExecute();
	    responder.friendDetailsLoading();
	  }

	  @Override
	  protected void onCancelled() {
	    super.onCancelled();
	    responder.friendDetailsLoadCancelled();
	  }
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		responder.friendDetailsLoaded();
	}

	private ArrayList<JSONArray> batchFriendDetailRequests(ArrayList<Friend> arrayList) {
		ArrayList<JSONArray> arrayOfBatchArrays = new ArrayList<JSONArray>();
		for (int l=0; l < arrayList.size(); l+= 50) {
			JSONArray batchArray = new JSONArray();
			int loopStopValue = (l + 50);
			if (loopStopValue > arrayList.size()) {
				loopStopValue = arrayList.size();
			}
			for (int i=l; i < loopStopValue; i++) {
				JSONObject friendDetails = new JSONObject();
				try {
					friendDetails.put("method", "GET");
					friendDetails.put("relative_url", arrayList.get(i).uid);
					friendDetails.put("parameters", "relationship_status");
					batchArray.put(friendDetails);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, e.getMessage());
				}
			}
			arrayOfBatchArrays.add(batchArray);
		}
		return arrayOfBatchArrays;
	}

}
