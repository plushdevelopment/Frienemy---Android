package com.frienemy.models;

import java.util.ArrayList;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Comment")
public class Comment extends ActiveRecordBase<Comment> implements Likeable {
	
	@SuppressWarnings("unused")
	private static final String TAG = Comment.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "createdTime")
	public String createdTime;
	
	@Column(name = "message")
	public String message;
	
	@Column(name = "fromFriend")
	public Friend fromFriend;
	
	@Column(name = "toFriend")
	public Friend toFriend;
	
	@Column(name = "post")
	public Post post;
	
	@Column(name = "likesCount")
	public int likesCount;
	
	public ArrayList<Like> likes() {
		ArrayList<Like> likes = Like.query(getContext(), Like.class, null, "comment = " + getId());
		return likes;
	}

	public Comment(Context context) {
		super(context);
	}

}
