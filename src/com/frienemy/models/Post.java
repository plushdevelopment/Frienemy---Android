package com.frienemy.models;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Post")
public class Post extends ActiveRecordBase<Post> {

	private static final String TAG = Comment.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "attribution")
	public String attribution;
	
	@Column(name = "caption")
	public String caption;
	
	@Column(name = "createdTime")
	public String createdTime;
	
	@Column(name = "icon")
	public String icon;
	
	@Column(name = "link")
	public String link;
	
	@Column(name = "message")
	public String message;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "picture")
	public String picture;
	
	@Column(name = "postDescription")
	public String postDescription;
	
	@Column(name = "source")
	public String source;
	
	@Column(name = "updatedTime")
	public String updatedTime;
	
	@Column(name = "friend")
	public Friend friend;
	
	public Post(Context context) {
		super(context);
	}

}
