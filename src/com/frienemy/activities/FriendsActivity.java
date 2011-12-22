package com.frienemy.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;
import com.frienemy.adapters.FriendAdapter;
import com.frienemy.models.Friend;
import com.frienemy.requests.FriendsRequestListener;
import com.frienemy.requests.UserRequestListener;
import com.frienemy.requests.FriendsRequestListener.FriendRequestListenerResponder;
import com.frienemy.requests.UserRequestListener.UserRequestListenerResponder;
import com.frienemy.services.FrienemyService;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;



public class FriendsActivity extends ListActivity implements OnClickListener, UserRequestListenerResponder, FriendRequestListenerResponder {

	private static final String TAG = FriendsActivity.class.getSimpleName();
	private static final String[] PERMS = new String[] { "read_stream", "offline_access", "friends_relationships", "friends_relationship_details", "user_relationships", "user_relationship_details", "friends_likes", "user_likes", "publish_stream" };
	private static final int EXIT = 0;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	private FrienemyServiceAPI api;
	private UserRequestListener userRequestListener;
	private FriendsRequestListener friendsRequestListener;
	protected ProgressDialog progressDialog;
	private ArrayList<Friend> friends;
	FriendAdapter adapter;
	ListView list;

	private FrienemyServiceListener.Stub collectorListener = new FrienemyServiceListener.Stub() {
		public void handleFriendsUpdated() throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					updateView();
				}
			});
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
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "Service connection closed");			
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		TextView v = (TextView) findViewById(R.id.title);
		v.setText("Friends");
		setUpListeners();
		
		asyncRunner = new AsyncFacebookRunner(facebook);
		userRequestListener = new UserRequestListener(getBaseContext(), this);
		friendsRequestListener = new FriendsRequestListener(getBaseContext(), this);

		Intent intent = new Intent(FrienemyService.class.getName()); 
		startService(intent);
		bindService(intent, serviceConnection, 0);

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
		} else {
			loadFriendsIfNotLoaded();
		}
	}

	private void loadFriendsIfNotLoaded() {
		updateView();
		if ((null == friends) || (friends.size() < 1)) {
			progressDialog = ProgressDialog.show(
					FriendsActivity.this,
					"Loading...",
					"Loading your friends list from Facebook");
			// Get the user's friend list
			Bundle parameters = new Bundle();
			parameters.putString("fields", "id,name,relationship_status");
			asyncRunner.request("me/friends", parameters, friendsRequestListener);
			
			// First, lets get the info about the current user
			asyncRunner.request("me", userRequestListener);
		}
		
	}
	


	//Listeners should be implemented in onClick method
	private void setUpListeners()
	{
		View v;
		v = findViewById( R.id.btnFriends );
		v.setBackgroundResource( R.drawable.gray_gradient);
		v.setOnClickListener( this );

		v = findViewById( R.id.btnFrienemies );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		v = findViewById( R.id.stalkers );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		//v = findViewById(R.id.postbutton);
		//v.setOnClickListener(this);
	}

	protected void updateView() {
		try{
			friends = Friend.query(this, Friend.class, null, "isCurrentUser==0", "name ASC");
			list=(ListView)findViewById(android.R.id.list);
			adapter=new FriendAdapter(this, friends);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			Log.i(TAG, "Friends count: " + friends.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
		if((progressDialog != null) && (progressDialog.isShowing()))
		{
			progressDialog.dismiss();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, EXIT, 0, "Log Off");
		return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case EXIT:
			asyncRunner.logout(getBaseContext(), new RequestListener() {
				
				public void onMalformedURLException(MalformedURLException e, Object state) {
					// TODO Auto-generated method stub
					
				}
				
				public void onIOException(IOException e, Object state) {
					// TODO Auto-generated method stub
					
				}
				
				public void onFileNotFoundException(FileNotFoundException e, Object state) {
					// TODO Auto-generated method stub
					
				}
				
				public void onFacebookError(FacebookError e, Object state) {
					// TODO Auto-generated method stub
					
				}
				
				public void onComplete(String response, Object state) {
					runOnUiThread(new Runnable() {
						public void run() {
							logout();
						}
					});
				}
			});

			return true;
		}
		return false;
	}
	
	public void logout() {
		stopService(new Intent(this, FrienemyService.class));
		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
		if(list != null){
			for(int i=0;i<list.size();++i){
				if("com.frienemy.activities".matches(list.get(i).processName)){
					int pid = android.os.Process.getUidForName("com.frienemy.activities");
					android.os.Process.killProcess(pid);

				}
			}
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	private class LoginDialogListener implements DialogListener {


		public void onComplete(Bundle values) {
			// Preferences
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("access_token", facebook.getAccessToken());
			editor.putLong("access_expires", facebook.getAccessExpires());
			editor.commit();

			// Bind to service
			Intent intent = new Intent(FrienemyService.class.getName());
			startService(intent);
			bindService(intent, serviceConnection, 0);

			loadFriendsIfNotLoaded();
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

	public void onClick(View v) {
		Intent i;
		switch (v.getId())
		{
		case R.id.btnFriends:

			break;
		case R.id.btnFrienemies:
			i = new Intent(FriendsActivity.this, FrienemyActivity.class);
			startActivity(i);
			break;
		case R.id.stalkers:
			i = new Intent(FriendsActivity.this, StalkerActivity.class);
			startActivity(i);
			break;
			//case R.id.postbutton:
			//	post();
			//break;
		default:

		}
	}

	public void userRequestDidFinish() {
		
	}

	public void userRequestDidFail() {
		Log.e(TAG, "Failed to get user");
	}

	public void friendRequestDidFinish(int totalFriends) {
		runOnUiThread(new Runnable() {
			public void run() {
				updateView();
			}
		});
	}

	public void friendRequestDidFail() {
		Log.e(TAG, "Failed to get friends list");
	}

}