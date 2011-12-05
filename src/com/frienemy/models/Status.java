package com.frienemy.models;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Status")
public class Status extends ActiveRecordBase<Status> {

private static final String TAG = Status.class.getSimpleName();
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "createdTime")
	public String createdTime;
	
	@Column(name = "message")
	public String message;
	
	@Column(name = "updatedTime")
	public String updatedTime;
	
	@Column(name = "friend")
	public Friend friend;
	
	public Status(Context context) {
		super(context);
	}

}
