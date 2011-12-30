package com.frienemy.requests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.frienemy.models.Comment;
import com.frienemy.models.Friend;
import com.frienemy.models.Like;
import com.frienemy.models.Post;
import com.frienemy.models.StalkerRelationship;

public class WallRequestListener implements RequestListener {

	public interface WallRequestListenerResponder {
		public void wallRequestDidFinish();
		public void wallRequestDidFail();
	}

	private static final String TAG = WallRequestListener.class.getSimpleName();
	private Context context;
	private Friend user;
	private WallRequestListenerResponder responder;

	public WallRequestListener(Context context, Friend user, WallRequestListenerResponder responder) {
		this.context = context;
		this.user = user;
		this.responder = responder;
	}

	public void onComplete(String response, Object state) {
		resetStalkerRanks();
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			for (int i=0; i<l; i++) {
				JSONObject o = d.getJSONObject(i);
				try {
					JSONObject fromObject = o.getJSONObject("from");
					Friend fromFriend = Friend.friendInContextForJSONObject(context, fromObject);
					try {
						StalkerRelationship relationship = StalkerRelationship.querySingle(context, StalkerRelationship.class, null, "toFriend==" + user.getId() + " AND fromFriend==" + fromFriend.getId());
						if (null == relationship)
							relationship = new StalkerRelationship(context, user, fromFriend);
						relationship.rank += 1;
						try {
							relationship.save();
						} catch (Exception e) {
							e.printStackTrace();
						}
						relationship = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					fromFriend = null;
					fromObject = null;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					JSONObject likesObject = o.getJSONObject("likes");
					try {
						JSONArray likesArray = likesObject.getJSONArray("data");
						for (int x=0; x<likesArray.length(); x++) {
							Friend likeFriend = Friend.friendInContextForJSONObject(context, likesArray.getJSONObject(x));
							try {
								StalkerRelationship relationship = StalkerRelationship.querySingle(context, StalkerRelationship.class, null, "toFriend==" + user.getId() + " AND fromFriend==" + likeFriend.getId());
								if (null == relationship)
									relationship = new StalkerRelationship(context, user, likeFriend);
								relationship.rank += 1;
								try {
									relationship.save();
								} catch (Exception e) {
									e.printStackTrace();
								}
								relationship = null;
							} catch (Exception e) {
								e.printStackTrace();
							}
							likeFriend = null;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						JSONObject commentsObject = o.getJSONObject("comments");
						try {
							JSONArray commentsArray = commentsObject.getJSONArray("data");
							for (int x=0; x<commentsArray.length(); x++) {
								try {
									JSONObject commentFromFriend = commentsArray.getJSONObject(x).getJSONObject("from");
									Friend commentFriend = Friend.friendInContextForJSONObject(context, commentFromFriend);
									try {
										StalkerRelationship relationship = StalkerRelationship.querySingle(context, StalkerRelationship.class, null, "toFriend==" + user.getId() + " AND fromFriend==" + commentFriend.getId());
										if (null == relationship)
											relationship = new StalkerRelationship(context, user, commentFriend);
										relationship.rank += 1;
										try {
											relationship.save();
										} catch (Exception e) {
											e.printStackTrace();
										}
										relationship = null;
									} catch (Exception e) {
										e.printStackTrace();
									}
									commentFromFriend = null;
									commentFriend = null;
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				o = null;
			}
			responder.wallRequestDidFinish();
		} catch (JSONException e) {
			e.printStackTrace();
			responder.wallRequestDidFail();
		}
	}

	private void resetStalkerRanks() {
		try {
			StalkerRelationship.delete(context, StalkerRelationship.class, "toFriend==" + user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			responder.wallRequestDidFail();
		}
	}

	public void onIOException(IOException e, Object state) {
		responder.wallRequestDidFail();
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		responder.wallRequestDidFail();
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		responder.wallRequestDidFail();
	}

	public void onFacebookError(FacebookError e, Object state) {
		responder.wallRequestDidFail();
	}

}
