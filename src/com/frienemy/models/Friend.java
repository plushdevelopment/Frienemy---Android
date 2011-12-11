package com.frienemy.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.frienemy.encryption.SimpleCrypto;

@Table(name = "Friend")
public class Friend extends ActiveRecordBase<Friend> {
	
	private static final String TAG = Friend.class.getSimpleName();
	private static final String KEY = "MAHMOUD";
	
	@Column(name = "uid")
	public String uid;
	public String encryptedUid;
	
	@Column(name = "name")
	public String name;
	public String encryptedName;
	
	@Column(name = "relationshipStatus")
	public String relationshipStatus = "NA";
	public String encryptedRelationshipStatus;
	
	@Column(name = "frienemyStatus")
	public int frienemyStatus;
	
	@Column(name = "relationshipChanged")
	public Boolean relationshipStatusChanged;
	
	@Column(name = "frienemyStatusChanged")
	public Boolean frienemyStatusChanged;
	
	@Column(name = "stalkerRank")
	public int stalkerRank;
	
	public ArrayList<Comment> comments() {
		ArrayList<Comment> comments = Comment.query(getContext(), Comment.class, null, "friend = " + getId(), "createdTime ASC");
		return comments;
	}
	
	public ArrayList<Post> posts() {
		ArrayList<Post> posts = Post.query(getContext(), Post.class, null, "from = " + getId(), "updatedTime ASC");
		return posts;
	}
	
	public ArrayList<Status> statuses() {
		ArrayList<Status> statuses = Status.query(getContext(), Status.class, null, "friend = " + getId(), "updatedTime ASC");
		return statuses;
	}
	
	public void encrypt() {
			try {
				encryptedUid = SimpleCrypto.encrypt(KEY, uid);
				encryptedName = SimpleCrypto.encrypt(KEY, name);
				encryptedRelationshipStatus = SimpleCrypto.encrypt(KEY, relationshipStatus);
			} catch (Exception e) {
				Log.e(TAG, "Failed to encrypt friend");
				e.printStackTrace();
			}
	}
	
	public void decrypt() {
			try {
				uid = SimpleCrypto.decrypt(KEY, encryptedUid);
				name = SimpleCrypto.decrypt(KEY, encryptedName);
				relationshipStatus = SimpleCrypto.decrypt(KEY, encryptedRelationshipStatus);
			} catch (Exception e) {
				Log.e(TAG, "Failed to decrypt friend");
				e.printStackTrace();
			}
	}

	public Friend(Context context) {
		super(context);
	}
	
	public static Friend friendInContextForKeyWithStringValue(Context context, String key, String value) {
		return querySingle(context, Friend.class, null, String.format("%s = %s", key, value));
	}
	
	public static Friend friendInContextForJSONObject(Context context, JSONObject object) {
		Friend friend = null;
		try {
			String n = object.getString("name");
			String uid = object.getString("id");
			friend = friendInContextForKeyWithStringValue(context, "uid", uid);
			if (friend == null) {
				friend = new Friend(context);
				friend.uid = uid;
				friend.name = n;
				friend.frienemyStatus = 2;
				friend.save();
			}
		} catch (JSONException e) {
			Log.w(TAG, e);
		}
		return friend;
	}

	public URL getProfileImageURL() {
		URL url = null;
		try {
			url = new URL(String.format("https://graph.facebook.com/%s/picture", uid));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

}
