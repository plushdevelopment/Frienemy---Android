package com.frienemy.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.frienemy.activities.EnemyActivity;
import com.frienemy.models.Friend;
import com.frienemy.requests.FriendDetailRequestListener;
import com.frienemy.requests.FriendsRequestListener;
import com.frienemy.requests.FriendDetailRequestListener.FriendDetailRequestListenerResponder;
import com.frienemy.requests.FriendsRequestListener.FriendRequestListenerResponder;
import com.frienemy.requests.UserRequestListener;
import com.frienemy.requests.UserRequestListener.UserRequestListenerResponder;
import com.frienemy.tasks.FacebookBatchRequest;
import com.frienemy.tasks.FacebookBatchRequest.FacebookBatchRequestResponder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class FrienemyService extends Service implements UserRequestListenerResponder, FriendRequestListenerResponder, FriendDetailRequestListenerResponder {

	private NotificationManager notificationManager;

	private static final String TAG = FrienemyService.class.getSimpleName();
	private Timer timer;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	private Context context;
	private UserRequestListener userRequestListener;
	private FriendsRequestListener friendsRequestListener;
	private List<FrienemyServiceListener> listeners = new ArrayList<FrienemyServiceListener>();

	private FrienemyServiceAPI.Stub apiEndpoint = new FrienemyServiceAPI.Stub() {
		public void addListener(FrienemyServiceListener listener)
				throws RemoteException {
			synchronized (listeners) {
				listeners.add(listener);
			}
		}

		public void removeListener(FrienemyServiceListener listener)
				throws RemoteException {
			synchronized (listeners) {
				listeners.remove(listener);
			}
		}

	};

	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			Log.i(TAG, "Timer task doing work");
			refreshPreferences();
			String access_token = mPrefs.getString("access_token", null);
			long expires = mPrefs.getLong("access_expires", 0);
			if(access_token != null) {
				facebook.setAccessToken(access_token);
			}
			if(expires != 0) {
				facebook.setAccessExpires(expires);
			}
			if (facebook.isSessionValid()) {
				// First, lets get the info about the current user
				asyncRunner.request("me", userRequestListener);
			}
			notifyListeners();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		if (FrienemyService.class.getName().equals(intent.getAction())) {
			Log.d(TAG, "Bound by intent " + intent);
			return apiEndpoint;
		} else {
			return null;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");
		refreshPreferences();
		context = this.getBaseContext();
		asyncRunner = new AsyncFacebookRunner(facebook);
		userRequestListener = new UserRequestListener(context, this);
		friendsRequestListener = new FriendsRequestListener(context, this);
		timer = new Timer("FrienemyServiceTimer");
		timer.schedule(updateTask, 10 * 60000L, 60 * 60000L);
	}

	public void refreshPreferences() {
		mPrefs = this.getSharedPreferences(FILENAME, MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");

		timer.cancel();
		timer = null;
	}

	private void notifyListeners() {
		synchronized (listeners) {
			Log.d(TAG, "Notify Listeners");
			for (FrienemyServiceListener listener : listeners) {
				try {
					listener.handleFriendsUpdated();
				} catch (RemoteException e) {
					Log.w(TAG, "Failed to notify listener " + listener, e);
				}
			}
		}
	}

	/*To update notification bar call this method
		Notification type is to be used to separate 
		from different notifications, Notifications with 
		the same type will replace and old notification 
		with the same type.
	 */

	private void showNotification(String title, String message, int iconId, int notificationType) {

		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Intent enemyIntent = new Intent(FrienemyService.this,EnemyActivity.class);

		// Set the icon, scrolling text and time stamp
		Notification notification = new Notification(iconId, message, System.currentTimeMillis());
		PendingIntent contentIntent;

		// The PendingIntent to launch our activity if the user selects this notification
		contentIntent = PendingIntent.getActivity(this, 0, enemyIntent, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, title, message, contentIntent);

		// Send the notification.
		notificationManager.notify(notificationType, notification);
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


	public void userRequestDidFinish() {
		// Get the user's friend list
		asyncRunner.request("me/friends", friendsRequestListener);
	}

	public void userRequestDidFail() {
		Log.e(TAG, "Failed to get user");
	}

	public void friendRequestDidFinish(int totalFriends) {
		//Check to see if there are any new frienemies and notifies the user
		if(!FriendsRequestListener.getList().equalsIgnoreCase(""))
		{
			this.showNotification("New Frenemies",FriendsRequestListener.getList(), com.frienemy.activities.R.drawable.icon, 0);
		}
		try {
			// Get the details for each friend in the list
			ArrayList<Friend> friends = Friend.query(context, Friend.class, new String[]{"uid"}, "isCurrentUser==0");
			ArrayList<JSONArray>batchFriends = batchFriendDetailRequests(friends);
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
				FriendDetailRequestListener friendDetailRequestListener = new FriendDetailRequestListener(context, this);
				friendDetailRequestListener.parseResponse(ret);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void friendRequestDidFail() {
		Log.e(TAG, "Failed to get friends list");
	}

	public void friendDetailRequestDidFinish() {
		Log.d(TAG, "FriendDetailRequestListener did finish");
	}

	public void friendDetailRequestDidFail() {
		Log.d(TAG, "FriendDetailRequestListener did finish");
	}

}
