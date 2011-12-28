package com.frienemy.activities;

import greendroid.app.GDActivity;

import java.util.ArrayList;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.flurry.android.FlurryAgent;
import com.frienemy.FrienemyApplication;
import com.frienemy.FrienemyApplication.StalkerListener;
import com.frienemy.adapters.FriendAdapter;
import com.frienemy.models.Friend;
import com.frienemy.requests.WallRequestListener;
import com.frienemy.requests.WallRequestListener.WallRequestListenerResponder;
import com.frienemy.services.FrienemyService;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StalkerActivity extends GDActivity implements OnClickListener, WallRequestListenerResponder { 

	private static final String TAG = StalkerActivity.class.getSimpleName();
	FriendAdapter adapter;
	ListView list;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	ArrayList<Friend> friends;
	protected ProgressDialog progressDialog;
	private FrienemyServiceAPI api;

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
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.main);
		setTitle("Stalkers");
		setUpListeners();
		loadFriendsIfNotLoaded();

		Intent intent = new Intent(FrienemyService.class.getName()); 
		startService(intent);
		bindService(intent, serviceConnection, 0);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "EB7H7EBXI7Z7CM21DJSM");
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	private void loadFriendsIfNotLoaded() {
		updateView();
		if ((null == friends) || (friends.size() < 1)) {
			progressDialog = ProgressDialog.show(
					StalkerActivity.this,
					"Loading...",
					"Loading your stalkers from Facebook");
		}
		getFacebookWall();

	}

	private void getFacebookWall() {
		mPrefs = this.getSharedPreferences(FILENAME, MODE_PRIVATE);
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
			// Get the user's wall
			asyncRunner.request("me/feed", new WallRequestListener(getBaseContext(), this));
		}
	}

	private void setUpListeners() {
		View v;
		v = findViewById( R.id.btnFriends );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		v = findViewById( R.id.btnFrienemies );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		v = findViewById( R.id.stalkers );
		v.setBackgroundResource( R.drawable.gray_gradient);
		v.setOnClickListener( this );

	}

	protected void updateView() {
		try{
			friends = Friend.query(getBaseContext(), Friend.class, null, "isCurrentUser==0 AND stalkerRank > 0", "stalkerRank DESC");
			list=(ListView)findViewById(android.R.id.list);
			adapter=new FriendAdapter(this, friends);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			Log.i(TAG, "Friends count: " + friends.size());
		}catch(Exception e) {
			e.printStackTrace();
		}
		if((progressDialog != null) && (progressDialog.isShowing())) {
			progressDialog.dismiss();
		}
	}

	public void onClick(View v) {
		Intent i;
		switch (v.getId())
		{
		case R.id.btnFriends:
			i = new Intent(StalkerActivity.this, FriendsActivity.class);
			startActivity(i);
			break;
		case R.id.btnFrienemies:
			i = new Intent(StalkerActivity.this, FrienemyActivity.class);
			startActivity(i);

			break;
		case R.id.stalkers:

			break;
			//case R.id.postbutton:
			//	post();
			//break;
		default:

		}
	}

	public void wallRequestDidFinish() {
		runOnUiThread(new Runnable() {
			public void run() {
				updateView();
			}
		});
	}

	public void wallRequestDidFail() {
		// TODO Auto-generated method stub
	}
}

