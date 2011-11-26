package com.frienemy.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;
import com.frienemy.models.Friend;
import com.frienemy.services.FrienemyService;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;

public class FrienemyActivity extends ListActivity {
    
	private static final String TAG = FrienemyActivity.class.getSimpleName();
	private static final String[] PERMS = new String[] { "read_stream", "offline_access", "friends_relationships", "friends_relationship_details", "user_relationships", "user_relationship_details", "friends_likes", "user_likes", "publish_stream" };
	Facebook facebook = new Facebook("124132700987915");
	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
	private FrienemyServiceAPI api;
	
	private FrienemyServiceListener.Stub collectorListener = new FrienemyServiceListener.Stub() {
		public void handleFriendsUpdated() throws RemoteException {
			updateView();
		}
	};
	
    private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "Service connection established");
	
			// that's how we get the client side of the IPC connection
			api = FrienemyServiceAPI.Stub.asInterface(service);
			try {
				api.addListener(collectorListener);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to add listener", e);
			}
			
			updateView();
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "Service connection closed");			
		}
	};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*
         * Get existing access_token if any
         */
        mPrefs = getSharedPreferences(FILENAME, MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
        	facebook.authorize(this, PERMS, new LoginDialogListener());
        }
        
        Intent intent = new Intent(FrienemyService.class.getName()); 
        startService(intent);
        bindService(intent, serviceConnection, 0);
    }
    
    protected void updateView() {
		ArrayList<Friend> friends = Friend.query(this, Friend.class);
		Log.i(TAG, "Friends count: " + friends.size());
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    private class LoginDialogListener implements DialogListener {
    	
        public void onComplete(Bundle values) {
    			SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("access_token", facebook.getAccessToken());
                editor.putLong("access_expires", facebook.getAccessExpires());
                editor.commit();
                Intent intent = new Intent(FrienemyService.class.getName()); 
                startService(intent);
                bindService(intent, serviceConnection, 0);
        }

        public void onFacebookError(FacebookError e) {
                Log.d(TAG, "FacebookError: " + e.getMessage());
        }

        public void onError(DialogError e) {
                Log.d(TAG, "Error: " + e.getMessage());
        }

        public void onCancel() {
                Log.d(TAG, "OnCancel");
        }
}
}