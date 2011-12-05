package com.frienemy.models;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Comment")
public class Comment extends ActiveRecordBase<Comment> {
	
	private static final String TAG = Comment.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "createdTime")
	public String createdTime;
	
	@Column(name = "message")
	public String message;
	
	@Column(name = "friend")
	public Friend friend;

	public Comment(Context context) {
		super(context);
	}

}
