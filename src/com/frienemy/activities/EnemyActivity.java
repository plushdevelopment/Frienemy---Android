package com.frienemy.activities;

import java.util.ArrayList;

import com.frienemy.adapters.FriendAdapter;
import com.frienemy.models.Friend;
import com.frienemy.services.FrienemyServiceAPI;
import com.frienemy.services.FrienemyServiceListener;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class EnemyActivity extends ListActivity implements OnClickListener { 

	private static final String TAG = FrienemyActivity.class.getSimpleName();
	FriendAdapter adapter;
	ListView list;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        updateView();
        setUpListeners();
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
				i = new Intent(EnemyActivity.this, FrienemyActivity.class);
				startActivity(i);
				break;
			case R.id.btnFrienemies:
				
				break;
			case R.id.stalkers:
				i = new Intent(EnemyActivity.this, StalkerActivity.class);
				startActivity(i);
				break;
			//case R.id.postbutton:
			//	post();
				//break;
			default:
		
	}
}
}
