package com.frienemy.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FrienemyService extends Service {
	
	private static final String TAG = FrienemyService.class.getSimpleName();
	private Timer timer;
	
	private TimerTask updateTask = new TimerTask() {
	    @Override
	    public void run() {
	      Log.i(TAG, "Timer task doing work");
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
