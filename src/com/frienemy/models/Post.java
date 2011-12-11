package com.frienemy.models;

import java.util.ArrayList;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Post")
public class Post extends ActiveRecordBase<Post> implements Likeable {

	private static final String TAG = Post.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "type")
	public String type;
	
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
	
	@Column(name = "description")
	public String description;
	
	@Column(name = "source")
	public String source;
	
	@Column(name = "objectId")
	public String objectId;
	
	@Column(name = "story")
	public String story;
	
	@Column(name = "updatedTime")
	public String updatedTime;
	
	@Column(name = "fromFriend")
	public Friend fromFriend;
	
	@Column(name = "toFriend")
	public Friend toFriend;
	
	@Column(name = "likesCount")
	public int likesCount;
	
	@Column(name = "commentsCount")
	public int commentsCount;
	
	public Post(Context context) {
		super(context);
	}
	
	public ArrayList<Like> likes() {
		ArrayList<Like> likes = Like.query(getContext(), Like.class, null, "post = " + getId());
		return likes;
	}

	public ArrayList<Comment> comments() {
		ArrayList<Comment> posts = Comment.query(getContext(), Comment.class, null, "post = " + getId());
		return posts;
	}

}
