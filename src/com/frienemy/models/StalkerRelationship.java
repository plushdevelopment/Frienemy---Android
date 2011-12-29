package com.frienemy.models;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "StalkerRelationship")
public class StalkerRelationship extends ActiveRecordBase<StalkerRelationship> {
	
	private static final String TAG = StalkerRelationship.class.getSimpleName();
	
	@Column(name = "rank")
	public int rank;
	
	@Column(name = "toFriend")
	public Friend toFriend;
	
	@Column(name = "fromFriend")
	public Friend fromFriend;

	public StalkerRelationship(Context context) {
		super(context);
	}
	
	public StalkerRelationship(Context context, Friend toFriend, Friend fromFriend) {
		super(context);
		this.toFriend = toFriend;
		this.fromFriend = fromFriend;
	}

}
