package com.frienemy;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.frienemy.models.Friend;
import com.frienemy.requests.FriendsRequestListener;
import com.frienemy.requests.UserRequestListener;
import com.frienemy.requests.WallRequestListener;
import com.frienemy.requests.FriendsRequestListener.FriendRequestListenerResponder;
import com.frienemy.requests.UserRequestListener.UserRequestListenerResponder;
import com.frienemy.requests.WallRequestListener.WallRequestListenerResponder;
import greendroid.app.GDApplication;

public class FrienemyApplication extends GDApplication implements UserRequestListenerResponder, FriendRequestListenerResponder, WallRequestListenerResponder  {

	public interface FriendListener {
		public void friendsUpdated();
		public void friendsUpdating();
		public void friendsUpdateFailed();
	}
	
	public interface StalkerListener {
		public void stalkersUpdated();
		public void stalkersUpdating();
		public void stalkersUpdateFailed();
	}
	
	private static final String TAG = FrienemyApplication.class.getSimpleName();
	private static final Facebook facebook = new Facebook("124132700987915");
	private static final AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(facebook);
	private static UserRequestListener userRequestListener;
	private static FriendsRequestListener friendsRequestListener;
	private static WallRequestListener wallRequestListener;
	public ArrayList<Friend> friends;
	public ArrayList<Friend> frienemies;
	public ArrayList<Friend> stalkers;
	public Friend user;
	public static int asyncTaskQueue=0;
	
	private List<FriendListener> friendListeners = new ArrayList<FriendListener>();
	private List<StalkerListener> stalkerListeners = new ArrayList<StalkerListener>();
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		userRequestListener = new UserRequestListener(this, this);
		friendsRequestListener = new FriendsRequestListener(this, this);
		user = Friend.querySingle(getApplicationContext(), Friend.class, null, "isCurrentUser==1");
		if (null != user)
			wallRequestListener = new WallRequestListener(this, user, this);
	}
	
	public void addFriendListener(FriendListener listener) {
		synchronized (friendListeners) {
			friendListeners.add(listener);
		}
	}

	public void removeFriendListener(FriendListener listener) {
		synchronized (friendListeners) {
			friendListeners.remove(listener);
		}
	}
	
	private void notifyFriendListeners() {
		synchronized (friendListeners) {
			for (FriendListener listener : friendListeners) {
				listener.friendsUpdated();
			}
		}
	}
	
	public void addStalkerListener(StalkerListener listener) {
		synchronized (stalkerListeners) {
			stalkerListeners.add(listener);
		}
	}

	public void removeStalkerListener(StalkerListener listener) {
		synchronized (stalkerListeners) {
			stalkerListeners.remove(listener);
		}
	}
	
	private void notifyStalkerListeners() {
		synchronized (friendListeners) {
			for (StalkerListener listener : stalkerListeners) {
				listener.stalkersUpdated();
			}
		}
	}

	public void requestFriends() {
		// Get the user's friend list
		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,name,relationship_status");
		asyncRunner.request("me/friends", parameters, friendsRequestListener);
	}

	public void requestStalkers() {
		Friend currentUser = Friend.querySingle(this, Friend.class, null, "isCurrentUser==1");
		if (null == currentUser) {
			// First, lets get the info about the current user
			asyncRunner.request("me", userRequestListener);
		} else {
			// Get the user's wall
			asyncRunner.request("me/feed", wallRequestListener);
		}
	}

	public void userRequestDidFinish() {
		
		// Get the user's wall
		asyncRunner.request("me/feed", wallRequestListener);
	}

	public void userRequestDidFail() {

	}

	public void friendRequestDidFinish(int totalFriends) {
		friends = Friend.query(this, Friend.class, null, "isCurrentUser==0", "name ASC");
		notifyFriendListeners();
	}

	public void friendRequestDidFail() {

	}

	public void wallRequestDidFinish() {
		stalkers = Friend.query(this, Friend.class, null, "isCurrentUser==0 AND stalkerRank > 0", "stalkerRank DESC");
		notifyStalkerListeners();
	}

	public void wallRequestDidFail() {

	}

	public ArrayList<Friend> getFrienemies() {
		frienemies = Friend.query(this, Friend.class, null, "frienemyStatus==1 AND isCurrentUser==0", "name ASC");
		return frienemies;
	}

}
