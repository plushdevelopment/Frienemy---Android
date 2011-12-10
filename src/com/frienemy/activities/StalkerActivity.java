package com.frienemy.activities;

import java.util.ArrayList;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.frienemy.adapters.FriendAdapter;
import com.frienemy.models.Friend;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;
import com.frienemy.services.WallRequestListener;

import android.app.ListActivity;
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
import android.widget.ListView;

public class StalkerActivity extends ListActivity implements OnClickListener { 

	private static final String TAG = StalkerActivity.class.getSimpleName();
	FriendAdapter adapter;
	ListView list;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		updateView();
		setUpListeners();
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
			asyncRunner.request("me/feed", new WallRequestListener(this.getBaseContext()));
		}
	}

	private void setUpListeners()
	{
		View v;
		v = findViewById( R.id.btnFriends );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		v = findViewById( R.id.btnFrienemies );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		v = findViewById( R.id.stalkers );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );
	}
	protected void updateView() {
		ArrayList<Friend> friends = Friend.query(this, Friend.class);

		list=(ListView)findViewById(android.R.id.list);
		adapter=new FriendAdapter(this, friends);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		Log.i(TAG, "Friends count: " + friends.size());
	}

	public void onClick(View v) {
		Intent i;
		switch (v.getId())
		{
		case R.id.btnFriends:
			i = new Intent(StalkerActivity.this, FrienemyActivity.class);
			startActivity(i);
			break;
		case R.id.btnFrienemies:
			i = new Intent(StalkerActivity.this, EnemyActivity.class);
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
}

