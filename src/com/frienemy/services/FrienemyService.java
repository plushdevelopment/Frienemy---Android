package com.frienemy.services;

import java.util.Timer;
import java.util.TimerTask;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class FrienemyService extends Service {
	
	private static final String TAG = FrienemyService.class.getSimpleName();
	private Timer timer;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
	private Context context;

	private TimerTask updateTask = new TimerTask() {
	    @Override
	    public void run() {
	      Log.i(TAG, "Timer task doing work");
	      if (facebook.isSessionValid()) {
	    	  asyncRunner = new AsyncFacebookRunner(facebook);
	    	  asyncRunner.request("me/friends", new FriendsRequestListener(context));  
	      }
	    }
	  };

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	  public void onCreate() {
	    super.onCreate();
	    Log.i(TAG, "Service creating");
	    
	    mPrefs = this.getSharedPreferences(FILENAME, MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
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
	  
}
