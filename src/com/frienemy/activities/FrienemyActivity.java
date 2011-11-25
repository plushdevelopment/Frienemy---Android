package com.frienemy.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.ListActivity;
import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;
import com.frienemy.services.FrienemyService;

public class FrienemyActivity extends ListActivity {
    
	private static final String TAG = FrienemyActivity.class.getSimpleName();
	private static final String[] PERMS = new String[] { "read_stream", "offline_access", "friends_relationships", "friends_relationship_details", "user_relationships", "user_relationship_details", "friends_likes", "user_likes", "publish_stream" };
	Facebook facebook = new Facebook("124132700987915");
	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
	
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
        
        startService(new Intent(FrienemyService.class.getName()));
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
                startService(new Intent(FrienemyService.class.getName()));
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