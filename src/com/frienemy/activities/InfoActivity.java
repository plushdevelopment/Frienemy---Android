package com.frienemy.activities;


import java.net.URL;

import com.frienemy.tasks.LoadImageAsyncTask;
import com.frienemy.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import greendroid.app.GDActivity;

public class InfoActivity extends GDActivity implements LoadImageAsyncTaskResponder{
	private AsyncTask<URL, Void, Drawable> latestLoadTask;
	ImageView k=null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.friend_info);
		setTitle("Info");
		//this.getActionBar().removeViewAt(0);
		
		TextView v = (TextView) findViewById(R.id.name);
		String name =getIntent().getStringExtra("name".trim());
		v.setText(name);
		
		 v = (TextView) findViewById(R.id.relationship);
		String relationship =getIntent().getStringExtra("relationship".trim());
		v.setText("Relationship Status: " +relationship);
		
		 v = (TextView) findViewById(R.id.frienemy);
			String frienemy =getIntent().getStringExtra("frienemy".trim());
			v.setText("Frienemy Status: " +frienemy);
			
		 k = (ImageView) findViewById(R.id.image);
		if (null != latestLoadTask) {
			latestLoadTask.cancel(true);
		}
		latestLoadTask = new LoadImageAsyncTask(this).execute(FriendsActivity.image);
		//String image = getIntent().getStringExtra("image".trim());
		
		//Uri myUri = Uri.parse(image);
		//k.setImageURI(myUri);
		
	}
	public void imageLoading() {
		k.setImageDrawable(null);
	}

	public void imageLoadCancelled() {
		// do nothing
	}

	public void imageLoaded(Drawable drawable) {
		k.setImageDrawable(drawable);
	}

}
