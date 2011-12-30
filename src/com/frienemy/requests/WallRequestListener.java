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
				/*
				String uid = o.getString("id");
				Post post = Post.querySingle(context, Post.class, null, String.format("uid == \"%s\"", uid));
				if (post == null) {
					post = new Post(context);
					post.uid = uid;
					post.save();
				} else {
					for (Like existingLike : post.likes()) {
						existingLike.delete();
					}
					for (Comment existingComment : post.comments()) {
						existingComment.delete();
					}
				}
				try {
					post.attribution = o.getString("attribution");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.caption = o.getString("caption");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.createdTime = o.getString("created_time");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.description = o.getString("description");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.icon = o.getString("icon");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.link = o.getString("link");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.message = o.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.name = o.getString("name");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.objectId = o.getString("object_id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.picture = o.getString("picture");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.source = o.getString("source");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.story = o.getString("story");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.type = o.getString("type");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					post.updatedTime = o.getString("updated_time");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					JSONObject toObject = o.getJSONObject("to");
					Friend toFriend = Friend.friendInContextForJSONObject(context, toObject);
					if (null != toFriend) {
						post.toFriend = toFriend;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				 */
				try {
					JSONObject fromObject = o.getJSONObject("from");
					Friend fromFriend = Friend.friendInContextForJSONObject(context, fromObject);
					if (null != fromFriend) {
						fromFriend.stalkerRank += 1;
						fromFriend.save();
						StalkerRelationship relationship = StalkerRelationship.querySingle(context, StalkerRelationship.class, null, "toFriend==" + user.getId() + " AND fromFriend==" + fromFriend.getId());
						if (null == relationship)
							relationship = new StalkerRelationship(context, user, fromFriend);
						relationship.rank += 1;
						relationship.save();
						//post.fromFriend = fromFriend;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					JSONObject likesObject = o.getJSONObject("likes");
					/*
					try {
						post.likesCount = likesObject.getInt("count");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					 */
					try {
						JSONArray likesArray = likesObject.getJSONArray("data");

						for (int x=0; x<likesArray.length(); x++) {
							//Like like = new Like(context);
							Friend likeFriend = Friend.friendInContextForJSONObject(context, likesArray.getJSONObject(x));
							if (null != likeFriend) {
								likeFriend.stalkerRank += 1;
								likeFriend.save();
								StalkerRelationship relationship = StalkerRelationship.querySingle(context, StalkerRelationship.class, null, "toFriend==" + user.getId() + " AND fromFriend==" + likeFriend.getId());
								if (null == relationship)
									relationship = new StalkerRelationship(context, user, likeFriend);
								relationship.rank += 1;
								relationship.save();
								//like.friend = likeFriend;
							}
							//like.post = post;
							//like.save();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						JSONObject commentsObject = o.getJSONObject("comments");
						/*
						try {
							post.commentsCount = commentsObject.getInt("count");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						 */
						try {
							JSONArray commentsArray = commentsObject.getJSONArray("data");
							for (int x=0; x<commentsArray.length(); x++) {
								//Comment comment = new Comment(context);
								try {
									JSONObject commentFromFriend = commentsArray.getJSONObject(x).getJSONObject("from");
									Friend commentFriend = Friend.friendInContextForJSONObject(context, commentFromFriend);
									if (null != commentFriend) {
										//commentFriend.stalkerRank += 1;
										//commentFriend.save();
										StalkerRelationship relationship = StalkerRelationship.querySingle(context, StalkerRelationship.class, null, "toFriend==" + user.getId() + " AND fromFriend==" + commentFriend.getId());
										if (null == relationship)
											relationship = new StalkerRelationship(context, user, commentFriend);
										relationship.rank += 1;
										relationship.save();
										//comment.fromFriend = commentFriend;
									}
									/*
									if (null != post.fromFriend) {
										comment.toFriend = post.fromFriend;
									}
									comment.post = post;
									try {
										comment.message = commentsArray.getJSONObject(x).getString("message");
									} catch (JSONException e) {
										e.printStackTrace();
									}
									try {
										comment.createdTime = commentsArray.getJSONObject(x).getString("created_time");
									} catch (JSONException e) {
										e.printStackTrace();
									}
									try {
										comment.uid = commentsArray.getJSONObject(x).getString("id");
									} catch (JSONException e) {
										e.printStackTrace();
									}
									comment.save();
									 */
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
				//post.save();
				Log.d(TAG, o.toString());
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
