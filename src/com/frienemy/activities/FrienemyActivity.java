package com.frienemy.activities;

import java.util.ArrayList;

import com.flurry.android.FlurryAgent;
import com.frienemy.adapters.FriendAdapter;
import com.frienemy.models.Friend;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FrienemyActivity extends ListActivity implements OnClickListener { 

	private static final String TAG = FrienemyActivity.class.getSimpleName();
	FriendAdapter adapter;
	ListView list;
	protected ProgressDialog progressDialog;


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frienemy);
		TextView v = (TextView) findViewById(R.id.title);
		v.setText("Frienemies");
		setUpListeners();
		loadFriendsIfNotLoaded();
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
		if (null == getListAdapter()) {
			progressDialog = ProgressDialog.show(
					FrienemyActivity.this,
					"Loading...",
					"Loading your frienemies");
			updateView();
		}

	}

	private void setUpListeners() {
		View v;
		v = findViewById( R.id.btnFriends );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );

		v = findViewById( R.id.btnFrienemies );
		v.setBackgroundResource( R.drawable.gray_gradient);
		v.setOnClickListener( this );

		v = findViewById( R.id.stalkers );
		v.setBackgroundResource( R.drawable.button_selector );
		v.setOnClickListener( this );
	}
	
	protected void updateView() {
		try{
			ArrayList<Friend> friends = Friend.query(this, Friend.class, null, "frienemyStatus==1 AND isCurrentUser==0", "name ASC");

			list=(ListView)findViewById(android.R.id.list);
			adapter=new FriendAdapter(this, friends);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			Log.i(TAG, "Friends count: " + friends.size());
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		if((progressDialog != null) && (progressDialog.isShowing()))
		{
			progressDialog.dismiss();
		}
	}

	public void onClick(View v) {
		Intent i;
		switch (v.getId())
		{
		case R.id.btnFriends:
			i = new Intent(FrienemyActivity.this, FriendsActivity.class);
			startActivity(i);
			break;
		case R.id.btnFrienemies:

			break;
		case R.id.stalkers:
			i = new Intent(FrienemyActivity.this, StalkerActivity.class);
			startActivity(i);
			break;
			//case R.id.postbutton:
			//	post();
			//break;
		default:

		}
	}
}
