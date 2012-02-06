package com.frienemy.activities;

import com.flurry.android.FlurryAgent;

import android.os.Bundle;
import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

public class DashboardActivity extends GDActivity {

	public DashboardActivity() {
		super(ActionBar.Type.Dashboard);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.main_activity);
		addActionBarItem(Type.AllFriends);
		addActionBarItem(Type.Group);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		return super.onHandleActionBarItemClick(item, position);
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
}
