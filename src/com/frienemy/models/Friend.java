package com.frienemy.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Friend")
public class Friend extends ActiveRecordBase<Friend> {
	
	private static final String TAG = Friend.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "frienemyStatus")
	public int frienemyStatus;

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
			}
		} catch (JSONException e) {
			Log.w(TAG, e);
		}
		return friend;
	}

}
