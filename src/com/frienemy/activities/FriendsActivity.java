package com.frienemy.activities;

import greendroid.app.GDActivity;
import greendroid.app.GDListActivity;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.flurry.android.FlurryAgent;
import com.frienemy.adapters.FriendAdapter;
import com.frienemy.models.Friend;
import com.frienemy.requests.FriendsRequestListener;
import com.frienemy.requests.UserRequestListener;
import com.frienemy.requests.FriendsRequestListener.FriendRequestListenerResponder;
import com.frienemy.requests.UserRequestListener.UserRequestListenerResponder;
import com.frienemy.services.FrienemyService;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;



public class FriendsActivity extends GDActivity implements OnClickListener, UserRequestListenerResponder, FriendRequestListenerResponder, OnCreateContextMenuListener, OnQuickActionClickListener {

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
	private long friendId;
	String name ="";
	String relationshipStatus="";
	String frienemyStatus="";
	public static URL image= null;

	private QuickActionWidget mBar;

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
		setActionBarContentView(R.layout.main);
		setTitle("Friends");
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		mBar = new QuickActionBar(this);
		mBar.addQuickAction(new QuickAction(this, R.drawable.info, "Info"));
		mBar.addQuickAction(new QuickAction(this, R.drawable.stalkers, "Stalk"));
		mBar.addQuickAction(new QuickAction(this, R.drawable.stalking, "Stalkers"));
		this.getActionBar().removeViewAt(0);


		setUpListeners();

		asyncRunner = new AsyncFacebookRunner(facebook);
		userRequestListener = new UserRequestListener(getApplicationContext(), this);
		friendsRequestListener = new FriendsRequestListener(getApplicationContext(), this);

		Intent intent = new Intent(FrienemyService.class.getName()); 
		startService(intent);
		bindService(intent, serviceConnection, 0);
		

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
					FriendsActivity.this,
					"Loading...",
					"Loading your friends list from Facebook");
			// Get the user's friend list
			Bundle parameters = new Bundle();
			parameters.putString("fields", "id,name,first_name,middle_name,last_name,gender,link,username,bio,birthday,education,email,hometown,interested_in,location,political,quotes,relationship_status,religion,significant_other,website,work");
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
		mBar.setOnQuickActionClickListener(this);

		ListView k =(ListView) findViewById(android.R.id.list);
		k.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				
				friendId = friends.get(position).getId();
				
				name =friends.get(position).name;

				relationshipStatus = friends.get(position).relationshipStatus;

				image = friends.get(position).getLargeProfileImageURL();

				if(friends.get(position).frienemyStatus==1)
				{
					frienemyStatus = "Frienemy";
				}
				else
				{
					frienemyStatus = "Friend";
				}
				// AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)v;

				mBar.show(v);
			}
		});
	}

	protected void updateView() {
		try{
			friends = Friend.query(getApplicationContext(), Friend.class, null, "isCurrentUser==0 AND frienemyStatus==0 AND isCurrentUsersFriend==1", "name ASC");
			list=(ListView)findViewById(android.R.id.list);
			adapter=new FriendAdapter(this, friends);
			list.setAdapter(adapter);
			registerForContextMenu(list);
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
		// Preferences
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("access_token", null);
		editor.commit();

		facebook.setAccessToken(null);

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
			Friend user = Friend.querySingle(getApplicationContext(), Friend.class, null, "isCurrentUser==1");
			i.putExtra("id", user.getId());
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

	public void onQuickActionClicked(QuickActionWidget widget, int position) {
		Intent k;
		switch (position)
		{
		case 0:
			k = new Intent(FriendsActivity.this, InfoActivity.class);
			k.putExtra("name".trim(), name);
			k.putExtra("relationship".trim(), relationshipStatus);
			k.putExtra("frienemy".trim(), frienemyStatus);
			k.putExtra("image".trim(), image.toString());
			startActivity(k);
			break;
		case 1:
			Friend friend = Friend.load(getApplicationContext(), Friend.class, friendId);
			friend.stalking = !friend.stalking;
			friend.save();
			updateView();
			break;
		case 2:
			k = new Intent(FriendsActivity.this, StalkerActivity.class);
			k.putExtra("id", friendId);
			startActivity(k);
			break;

		default:
		}

	}

	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==android.R.id.list) {

		 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	  	mBar.show(v);
	  	/*
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(friends.get(info.position).name);
	    String[] menuItems = {"Stalk","User's Stalkers"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  }
	}*/


}