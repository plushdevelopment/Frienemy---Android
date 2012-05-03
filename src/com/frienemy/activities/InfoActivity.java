package com.frienemy.activities;


import java.net.URL;

import com.frienemy.models.Friend;
import com.frienemy.tasks.LoadImageAsyncTask;
import com.frienemy.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

public class InfoActivity extends GDActivity implements LoadImageAsyncTaskResponder{
	private AsyncTask<URL, Void, Drawable> latestLoadTask;
	ImageView profileImageView=null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.friend_info);
		this.getActionBar().removeViewAt(0);
		addActionBarItem(Type.GoHome);
		setTitle("Info");
		
		fillFields();
		
		
	}
	
	public void fillFields() {
		
		long friendId=getIntent().getLongExtra("friendId", 0);
		
		Friend friend = Friend.load(getApplicationContext(), Friend.class, friendId);
		TextView v = (TextView) findViewById(R.id.name);
		String name =friend.name;
		v.setText(name);
		
		v = (TextView) findViewById(R.id.relationship);
		String relationship =friend.relationshipStatus;
		String sigOther =friend.significantOther;
		if(sigOther!=null)
		{
			v.setText("Relationship Status: " +relationship + " with " +sigOther);
		}
		else
		{
			v.setText("Relationship Status: " +relationship);
		}
		
		v = (TextView) findViewById(R.id.frienemy);
		String frienemy =getIntent().getStringExtra("frienemy".trim());
		v.setText("Frienemy Status: " +frienemy);
		
		v = (TextView) findViewById(R.id.About);
		String about =friend.bio;
		if(about==null)about="";
		v.setText("About: " +about);
		
		v = (TextView) findViewById(R.id.Email);
		String email =friend.email;
		if(email==null)email="";
		v.setText("Email: " +email);
		
		v = (TextView) findViewById(R.id.quotes);
		String quotes =friend.quotes;
		if(quotes==null)quotes="";
		v.setText("Quotes: " +quotes);
		
		v = (TextView) findViewById(R.id.Birthdate);
		String birthdate =friend.birthday;
		if(birthdate==null)birthdate="";
		v.setText("Birthdate: " +birthdate);
		
		v = (TextView) findViewById(R.id.Religion);
		String religion =friend.religion;
		if(religion==null)religion="";
		v.setText("Religion: " +religion);
		
		v = (TextView) findViewById(R.id.Hometown);
		String hometown =friend.hometown;
		if(hometown==null)hometown="";
		v.setText("Hometown: " +hometown);

		 profileImageView = (ImageView) findViewById(R.id.image);
		if (null != latestLoadTask) {
			latestLoadTask.cancel(true);
		}
		latestLoadTask = new LoadImageAsyncTask(this).execute(friend.getLargeProfileImageURL());
		//String image = getIntent().getStringExtra("image".trim());
		
		//Uri myUri = Uri.parse(image);
		//k.setImageURI(myUri);
		
	}
	
	public void imageLoading() {
		profileImageView.setImageDrawable(null);
	}

	public void imageLoadCancelled() {
		// do nothing
	}

	public void imageLoaded(Drawable drawable) {
		profileImageView.setImageDrawable(drawable);
	}
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (position) {
        case 0:
            startActivity(new Intent(this, FriendsActivity.class));
            break;
	}
		return true;
	}


	public void addTask(AsyncTask<URL, Void, Drawable> task, URL url) {
		// TODO Auto-generated method stub
		
	}

}
