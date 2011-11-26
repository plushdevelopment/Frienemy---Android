package com.frienemy.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class FrienemyService extends Service {

	private static final String TAG = FrienemyService.class.getSimpleName();
	private Timer timer;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	private Context context;

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
			String access_token = mPrefs.getString("access_token", null);
			long expires = mPrefs.getLong("access_expires", 0);
			if(access_token != null) {
				facebook.setAccessToken(access_token);
			}
			if(expires != 0) {
				facebook.setAccessExpires(expires);
			}
			if (facebook.isSessionValid()) {
				asyncRunner = new AsyncFacebookRunner(facebook);
				asyncRunner.request("me/friends", new FriendsRequestListener(context));  
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

		mPrefs = this.getSharedPreferences(FILENAME, MODE_PRIVATE);
		context = this.getBaseContext();
		timer = new Timer("FrienemyServiceTimer");
		timer.schedule(updateTask, 1000L, 60 * 1000L);
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
			for (FrienemyServiceListener listener : listeners) {
				try {
					listener.handleFriendsUpdated();
				} catch (RemoteException e) {
					Log.w(TAG, "Failed to notify listener " + listener, e);
				}
			}
		}
	}

}
