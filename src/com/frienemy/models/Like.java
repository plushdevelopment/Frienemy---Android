package com.frienemy.models;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;

public class Like extends ActiveRecordBase<Like> {

	private static final String TAG = Like.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "friend")
	public Friend friend;
	
	@Column(name = "post")
	public Post post;
	
	@Column(name = "comment")
	public Comment comment;
	
	public Like(Context context) {
		super(context);
	}

}
