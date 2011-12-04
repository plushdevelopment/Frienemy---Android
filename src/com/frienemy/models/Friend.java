package com.frienemy.models;

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
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "frienemyStatus")
	public int frienemyStatus;
	
	@Column(name = "relationshipStatus")
	public String relationshipStatus;
	
	@Column(name = "relationshipChanged")
	public Boolean relationshipStatusChanged;
	
	@Column(name = "frienemyStatusChanged")
	public Boolean frienemyStatusChanged;

	public Friend(Context context) {
		super(context);
	}
	
	@Override
	public void save() {
		
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

}
