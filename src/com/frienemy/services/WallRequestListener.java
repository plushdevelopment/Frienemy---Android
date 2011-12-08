package com.frienemy.services;

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

public class WallRequestListener implements RequestListener {

	private static final String TAG = WallRequestListener.class.getSimpleName();
	private Context context;
	
	public WallRequestListener(Context context) {
		this.context = context;
	}
	
	public void onComplete(String response, Object state) {
		try {
			final JSONObject json = new JSONObject(response);
			JSONArray d = json.getJSONArray("data");
			int l = (d != null ? d.length() : 0);
			for (int i=0; i<l; i++) {
				JSONObject o = d.getJSONObject(i);
				String uid = o.getString("id");
				Post post = Post.querySingle(context, Post.class, null, String.format("uid = %s", uid));
				if (post == null) {
					post = new Post(context);
					post.uid = uid;
				} else {
					for (Like existingLike : post.likes()) {
						existingLike.delete();
					}
					for (Comment existingComment : post.comments()) {
						existingComment.delete();
					}
				}
				post.attribution = o.getString("attribution");
				post.caption = o.getString("caption");
				post.createdTime = o.getString("created_time");
				post.description = o.getString("description");
				post.icon = o.getString("icon");
				post.link = o.getString("link");
				post.message = o.getString("message");
				post.name = o.getString("name");
				post.objectId = o.getString("object_id");
				post.picture = o.getString("picture");
				post.source = o.getString("source");
				post.story = o.getString("story");
				post.type = o.getString("type");
				post.updatedTime = o.getString("updated_time");
				JSONObject fromObject = o.getJSONObject("from");
				Friend fromFriend = Friend.friendInContextForJSONObject(context, fromObject);
				post.fromFriend = fromFriend;
				JSONObject toObject = o.getJSONObject("to");
				Friend toFriend = Friend.friendInContextForJSONObject(context, toObject);
				post.toFriend = toFriend;
				JSONObject likesObject = o.getJSONObject("likes");
				post.likesCount = likesObject.getInt("count");
				JSONArray likesArray = likesObject.getJSONArray("data");
				for (int x=0; x<likesArray.length(); x++) {
					Like like = new Like(context);
					Friend likeFriend = Friend.friendInContextForKeyWithStringValue(context, "uid", likesArray.getJSONObject(x).getString("uid")); 
					like.friend = likeFriend;
					like.post = post;
				}
				
				JSONObject commentsObject = o.getJSONObject("comments");
				post.commentsCount = commentsObject.getInt("count");
				JSONArray commentsArray = commentsObject.getJSONArray("data");
				for (int x=0; x<commentsArray.length(); x++) {
					Comment comment = new Comment(context);
					JSONObject commentFromFriend = commentsArray.getJSONObject(x).getJSONObject("from");
					Friend commentFriend = Friend.friendInContextForKeyWithStringValue(context, "uid", commentFromFriend.getString("uid"));
					comment.fromFriend = commentFriend;
					comment.toFriend = post.fromFriend;
					comment.post = post;
					comment.message = commentsArray.getJSONObject(x).getString("message");
					comment.createdTime = commentsArray.getJSONObject(x).getString("created_time");
					comment.uid = commentsArray.getJSONObject(x).getString("id");
				}
				
				post.save();
				Log.d(TAG, o.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub
		
	}

}
