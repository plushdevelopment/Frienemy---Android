package com.frienemy.activities;


import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import greendroid.app.GDActivity;

public class InfoActivity extends GDActivity{
	
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
			
		ImageView k = (ImageView) findViewById(R.id.image);
		
		//String image = getIntent().getStringExtra("image".trim());
		
		//Uri myUri = Uri.parse(image);
		//k.setImageURI(myUri);
		
	}

}
