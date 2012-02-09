package com.frienemy.activities;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.flurry.android.FlurryAgent;
import com.frienemy.adapters.StalkerAdapter;
import com.frienemy.models.Friend;
import com.frienemy.models.StalkerRelationship;
import com.frienemy.requests.WallRequestListener;
import com.frienemy.requests.WallRequestListener.WallRequestListenerResponder;
import com.frienemy.services.FrienemyService;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
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

public class StalkerActivity extends GDActivity implements OnClickListener, WallRequestListenerResponder { 

	private static final String TAG = StalkerActivity.class.getSimpleName();
	StalkerAdapter adapter;
	ListView list;
	Facebook facebook = new Facebook("124132700987915");
	private AsyncFacebookRunner asyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	ArrayList<StalkerRelationship> stalkerRelationships;
	protected ProgressDialog progressDialog;
	private FrienemyServiceAPI api;
	public Friend user;
	private WallRequestListener wallRequestListener;

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
		setActionBarContentView(R.layout.stalkers);
		this.getActionBar().removeViewAt(0);
		setTitle("Stalkers");
		setUpListeners();

		Intent intent = getIntent();
		if (intent.hasExtra("id")) 
		{
			user = Friend.load(getApplicationContext(), Friend.class, intent.getLongExtra("id", 0));
			if (false == user.isCurrentUser) 
			{
				View v = (View) findViewById(R.id.tabs);
				v.setVisibility(View.GONE);
				addActionBarItem(Type.GoHome);
			} 
			else 
			{
				View v = (View) findViewById(R.id.tabs);
				v.setVisibility(View.VISIBLE);
				addActionBarItem(Type.Share);
			}
		}
		else 
		{
			user = Friend.querySingle(getApplicationContext(), Friend.class, null, "isCurrentUser==1");
			View v = (View) findViewById(R.id.tabs);
			v.setVisibility(View.VISIBLE);
		}

		Intent serviceIntent = new Intent(FrienemyService.class.getName()); 
		startService(serviceIntent);
		bindService(serviceIntent, serviceConnection, 0);

		wallRequestListener = new WallRequestListener(getApplicationContext(), user, this);

		loadFriendsIfNotLoaded();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

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
		if ((null == stalkerRelationships) || (stalkerRelationships.size() < 1)) {
			progressDialog = ProgressDialog.show(
					StalkerActivity.this,
					"Loading...",
					"Loading " + user.name + "'s stalkers from Facebook");
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
			asyncRunner.request(user.uid + "/feed", wallRequestListener);
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
			stalkerRelationships = user.stalkers();
			list=(ListView)findViewById(android.R.id.list);
			adapter=new StalkerAdapter(this, stalkerRelationships);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			Log.i(TAG, "Stalkers count: " + stalkerRelationships.size());
		}catch(Exception e) {
			e.printStackTrace();
		}
		if((progressDialog != null) && (progressDialog.isShowing())) {
			progressDialog.dismiss();
		}
	}
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (position) {
		case 0:
			if (false == user.isCurrentUser) 
			{
				startActivity(new Intent(this, FriendsActivity.class));
			}
			else
			{

				String post = "Post Stalkers To Wall?";
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(post)
				.setTitle("Post")
				.setCancelable(true)
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String stalkerList="My Top Stalkers on Frienemy: \n";
						for( int i =0; i<StalkerAdapter.stalkers.length ; i++)
						{
							if(StalkerAdapter.stalkers[i]!=null)
							{
								stalkerList+= i+1 + "-" +StalkerAdapter.stalkers[i] + "\n";
							}
							
						}
						stalkerList+="\nDownload App: \n " + "https://market.android.com/details?id=com.frienemy.activities";

						postOnWall(stalkerList);
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				break;
			}
		}
		return true;

	}
	
	public void postOnWall(String msg) {
        Log.d("Tests", "Testing graph API wall post");
         try {
                String response = facebook.request("me");
                Bundle parameters = new Bundle();
                parameters.putString("message", msg);
                parameters.putString("description", "test test test");
                response = facebook.request("me/feed", parameters, 
                        "POST");
                Log.d("Tests", "got response: " + response);
                if (response == null || response.equals("") || 
                        response.equals("false")) {
                   Log.v("Error", "Blank response");
                }
         } catch(Exception e) {
             e.printStackTrace();
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

