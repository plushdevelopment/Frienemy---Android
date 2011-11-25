package com.frienemy.models;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Friend")
public class Friend extends ActiveRecordBase<Friend> {
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "name")
	public String name;

	public Friend(Context context) {
		super(context);
	}

}
